const mongoose = require("mongoose");

const Schema = mongoose.Schema;
TaskScheme = new Schema({
  _id: {
    type: mongoose.Schema.Types.ObjectId,
    auto: true,
  },
  status: { type: Number, default: 0 },
  todoDate: { type: String },
  todoDes: { type: String },
  todoTitle: { type: String },
  todoType: { type: String },
});

module.exports = mongoose.model("Task", TaskScheme, "task");
