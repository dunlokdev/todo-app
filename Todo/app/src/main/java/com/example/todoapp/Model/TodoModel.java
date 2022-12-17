package com.example.todoapp.Model;

import java.io.Serializable;

public class TodoModel implements Serializable {
    private String _id;
    private int status;
    private String todoTitle;
    private String todoDate;
    private String todoDes;
    private String todoType;

    public TodoModel() {
    }

    public TodoModel(int status) {
        this.status = status;
    }

    public TodoModel(String _id, int status, String todoTitle, String todoDate, String todoDes, String todoType) {
        this._id = _id;
        this.status = status;
        this.todoTitle = todoTitle;
        this.todoDate = todoDate;
        this.todoDes = todoDes;
        this.todoType = todoType;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
    }

    public String getTodoDes() {
        return todoDes;
    }

    public void setTodoDes(String todoDes) {
        this.todoDes = todoDes;
    }

    public String getTodoType() {
        return todoType;
    }

    public void setTodoType(String todoType) {
        this.todoType = todoType;
    }
}
