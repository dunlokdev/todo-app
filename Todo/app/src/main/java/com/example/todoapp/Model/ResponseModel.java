package com.example.todoapp.Model;

import java.util.List;

public class ResponseModel {
    private List<TodoModel> data;
    private PaginationModel pagination;

    public List<TodoModel> getData() {
        return data;
    }

    public void setData(List<TodoModel> data) {
        this.data = data;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    public void setPagination(PaginationModel pagination) {
        this.pagination = pagination;
    }
}
