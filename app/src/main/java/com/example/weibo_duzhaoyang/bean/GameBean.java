/**
 * Copyright 2024 json.cn
 */
package com.example.weibo_duzhaoyang.bean;

public class GameBean {

    private Integer id;
    private String gameName;
    private String packageName;
    private String appId;
    private String icon;
    private String introduction;
    private String brief;
    private String versionName;
    private String apkUrl;
    private String tags;
    private Double score;
    private String playNumFormat;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getPlayNumFormat() {
        return playNumFormat;
    }

    public void setPlayNumFormat(String playNumFormat) {
        this.playNumFormat = playNumFormat;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GameBean{" +
                "id=" + id +
                ", gameName='" + gameName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appId='" + appId + '\'' +
                ", icon='" + icon + '\'' +
                ", introduction='" + introduction + '\'' +
                ", brief='" + brief + '\'' +
                ", versionName='" + versionName + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", tags='" + tags + '\'' +
                ", score=" + score +
                ", playNumFormat='" + playNumFormat + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}