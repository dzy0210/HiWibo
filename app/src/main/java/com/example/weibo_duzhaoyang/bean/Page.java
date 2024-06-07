package com.example.weibo_duzhaoyang.bean;

import java.util.List;

public class Page {
    List<WeiboInfo> records;
    Integer total;
    Integer size;
    Integer current;
    Integer pages;


    public List<WeiboInfo> getRecords() {
        return records;
    }

    public void setRecords(List<WeiboInfo> records) {
        this.records = records;
    }

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

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Page{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", pages=" + pages +
                '}';
    }
}
