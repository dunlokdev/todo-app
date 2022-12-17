const express = require("express");
const router = express.Router();

const taskController = require("../app/controllers/TaskController");

router.get("/", taskController.getTasks);
router.post("/store", taskController.store);
router.put("/update/:id", taskController.update);
router.patch("/update/:id", taskController.switchStatus);
router.delete("/delete/:id", taskController.destroy);

module.exports = router;
