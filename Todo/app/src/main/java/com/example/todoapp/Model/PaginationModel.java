package com.example.todoapp.Model;

public class PaginationModel {
    private Integer _page;
    private Integer _limit;
    private Integer _totalPages;
    private Integer _totalTask;

    public PaginationModel(Integer _page, Integer _limit, Integer _totalPages, Integer _totalTask) {
        this._page = _page;
        this._limit = _limit;
        this._totalPages = _totalPages;
        this._totalTask = _totalTask;
    }

    public Integer get_page() {
        return _page;
    }

    public void set_page(Integer _page) {
        this._page = _page;
    }

    public Integer get_limit() {
        return _limit;
    }

    public void set_limit(Integer _limit) {
        this._limit = _limit;
    }

    public Integer get_totalPages() {
        return _totalPages;
    }

    public void set_totalPages(Integer _totalPages) {
        this._totalPages = _totalPages;
    }

    public Integer get_totalTask() {
        return _totalTask;
    }

    public void set_totalTask(Integer _totalTask) {
        this._totalTask = _totalTask;
    }
}
