const mongoose = require("mongoose");

function connect() {
  try {
    mongoose.set("strictQuery", false);
    const connectionParams = {
      useNewUrlParser: true,
    };
    mongoose.connect(process.env.DB, connectionParams);

    mongoose.connection.on("connected", () => {
      console.log("Connected to database successfully");
    });
    mongoose.connection.on("error", (err) => {
      console.log("Error while connect to database: " + err);
    });
    mongoose.connection.on("disconnected", () => {
      console.log("Mongodb connection disconnected");
    });
  } catch (error) {}
}

module.exports = { connect };
