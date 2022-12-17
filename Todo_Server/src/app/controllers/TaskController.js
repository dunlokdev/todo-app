const Task = require("../models/Task");

class TaskController {
  getTasks(req, res, next) {
    const _page = Number(req.query._page ?? 1);
    const _limit = Number(req.query._limit ?? 10);
    const _search = req.query.search || "";
    let _status = req.query.status ?? "All";
    let sort = req.query.sort || "status";
    let type = req.query.type || "All";

    const typeOptions = [
      "All",
      "Personal",
      "Work",
      "Playing",
      "Home",
      "Technical",
      "Studies",
      "School",
      "Market",
      "Others",
    ];

    _status === "All"
      ? (_status = [1, 0])
      : _status === "Success"
      ? (_status = 1)
      : (_status = 0);

    type === "All"
      ? (type = [...typeOptions])
      : (type = req.query.type.split(","));
    req.query.sort ? (sort = req.query.sort.split(",")) : (sort = [sort]);

    let sortBy = {};
    if (sort[1]) {
      sortBy[sort[0]] = sort[1];
    } else {
      sortBy[sort[0]] = "asc";
    }

    Promise.all([
      Task.find({ todoTitle: { $regex: _search, $options: "i" } })
        .where("todoType")
        .in([...type])
        .where("status", _status)
        .sort(sortBy)
        .skip((_page - 1) * _limit)
        .limit(_limit)
        .lean(),
      Task.countDocuments({
        todoType: { $in: [...type] },
        status: _status,
        todoTitle: { $regex: _search, $options: "i" },
      }),
    ])
      .then(([_tasks, _totalTask]) => {
        res.status(200).jsonp({
          data: _tasks,
          pagination: {
            _page,
            _limit,
            _totalPages: Math.ceil(_totalTask / _limit),
            _totalTask,
          },
        });
      })
      .catch((error) => {
        console.log(error);
        res.status(500).json({
          error: true,
          message: "Internal Server Error",
        });
      });
  }

  async store(req, res, next) {
    try {
      const task = new Task(req.body);
      const savedTask = await task.save();

      res.status(200).jsonp(savedTask);
    } catch (error) {
      console.log(error);
      res.status(500).json({
        error: true,
        message: "Internal Server Error",
      });
    }
  }

  async update(req, res, next) {
    try {
      await Task.updateOne({ _id: req.params.id }, req.body);

      res.status(200).jsonp(req.body);
    } catch (error) {
      console.log(error);
      res.status(500).json({
        error: true,
        message: "Internal Server Error",
      });
    }
  }

  async switchStatus(req, res, next) {
    try {
      await Task.updateOne({ _id: req.params.id }, req.body);

      res.status(200).jsonp(req.body);
    } catch (error) {
      console.log(error);
      res.status(500).json({
        error: true,
        message: "Internal Server Error",
      });
    }
  }

  async destroy(req, res, next) {
    try {
      const removedStatus = await Task.deleteOne({ _id: req.params.id });

      res.status(200).json(removedStatus);
    } catch (error) {
      console.log(error);
      res.status(500).json({
        error: true,
        message: "Internal Server Error",
      });
    }
  }
}

module.exports = new TaskController();
