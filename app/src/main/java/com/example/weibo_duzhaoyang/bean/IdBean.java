package com.example.weibo_duzhaoyang.bean;

public class IdBean {
    Long id;

    public IdBean(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdBean{" +
                "id=" + id +
                '}';
    }
}
