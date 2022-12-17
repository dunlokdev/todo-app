const tasksRouter = require("./tasks");

function routes(app) {
  app.use("/api/v1/tasks", tasksRouter);
}

module.exports = routes;
