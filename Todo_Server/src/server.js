require("dotenv").config();
const express = require("express");
const app = express();
const port = 3000;

const routes = require("./routes");
const db = require("./config/db");

db.connect();

app.use(
  express.urlencoded({
    extended: true,
  })
);
app.use(express.json());

routes(app);

app.listen(port, () => {
  console.log(`App listening on port ${port}`);
});
