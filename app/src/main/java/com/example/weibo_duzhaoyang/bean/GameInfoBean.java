package com.example.weibo_duzhaoyang.bean;

import java.util.List;
public class GameInfoBean<T> {
    Integer total;
    Integer size;
    Integer current;
    List<Object> orders;
    Boolean searchCount;
    Integer pages;
    List<T>records;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

    public Boolean getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Boolean searchCount) {
        this.searchCount = searchCount;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "GameInfoBean{" +
                "total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", orders=" + orders +
                ", searchCount=" + searchCount +
                ", page=" + pages +
                ", records=" + records +
                '}';
    }
}
