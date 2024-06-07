package com.example.weibo_duzhaoyang.bean;

import java.util.List;

public class WeiboInfo {
    Long id;
    Long userId;
    String username;
    String phone;
    String avatar;
    String title;
    String videoUrl;
    String poster;
    List<String> images;
    Integer likeCount;
    Boolean likeFlag;
    String createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Boolean likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WeiboInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", poster='" + poster + '\'' +
                ", images=" + images +
                ", likeCount=" + likeCount +
                ", likeFlag=" + likeFlag +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
