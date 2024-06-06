package com.example.weibo_duzhaoyang.bean;

public class LoginBean {
    String phone;
    String smsCode;

    public LoginBean(String phone, String smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
