package com.example.weibo_duzhaoyang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weibo_duzhaoyang.bean.BaseBean;
import com.example.weibo_duzhaoyang.bean.LoginBean;
import com.example.weibo_duzhaoyang.bean.UserInfo;
import com.example.weibo_duzhaoyang.retrofit.RetrofitManager;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "dzy   LoginActivity";
    TextView tvGetSms;
    EditText etPhone;
    EditText etSms;
    TextView tvLogin;
    SharedPreferences sharedPreferences;


    final int totalSecondCount = 60;
    int currentSecond = totalSecondCount;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100: {
                    int second = (int) msg.obj;
                    tvGetSms.setText("获取验证码（"+second+"s）");
                    Message message = Message.obtain();
                    message.what = 100;
                    message.obj = --currentSecond;
                    if (currentSecond >= 0) {
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        tvGetSms.setEnabled(true);
                        tvGetSms.setText("获取验证码");
                        currentSecond = totalSecondCount;
                    }
                    break;
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("dzy", MODE_PRIVATE);
        initView();
        initToolbar();
    }

    private void initView() {
        tvGetSms = findViewById(R.id.tv_get_sms);
        etPhone = findViewById(R.id.et_phone);
        etSms = findViewById(R.id.et_sms);
        tvLogin = findViewById(R.id.tv_login);
        tvGetSms.setText("获取验证码");
        tvLogin.setText("立即登录");
        tvGetSms.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        String phone = sharedPreferences.getString("phone", "");
        if (!phone.equals("")) {
            etPhone.setText(phone);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_get_sms: {
                tvGetSms.setEnabled(false);
                countDown();
                getSms();
                break;
            }
            case R.id.tv_login:{
                login();
                getUserinfo();
                break;
            }
            case R.id.toolbar_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    void initToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        TextView tvBack = findViewById(R.id.toolbar_back);
        tvBack.setText("返回");
        tvBack.setOnClickListener(this);
    }
    private void login() {
       String phone = etPhone.getText().toString();
       String sms = etSms.getText().toString();
       if (TextUtils.isEmpty(phone) || phone.length() != 11) {
           Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
           return;
       }
        if (TextUtils.isEmpty(sms) || sms.length() != 6) {
            Toast.makeText(LoginActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<BaseBean<String>> call = RetrofitManager.getInstance("").createApi().login(new LoginBean(phone, sms));
        call.enqueue(new Callback<BaseBean<String>>() {
            @Override
            public void onResponse(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                Log.i(TAG, "onResponse1: "+response.body());
                Log.i(TAG, "onResponse1: "+response.body().getData());
                if (response.body().getCode() == 200) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String token = response.body().getData();
                    editor.putString("token", token);
                    editor.putString("phone", phone);
                    editor.apply();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseBean<String>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }
        });
    }
    void getUserinfo() {
        String token = sharedPreferences.getString("token", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Call<BaseBean<UserInfo>> userInfo = RetrofitManager.getInstance(token).createApi().getUserInfo();
        userInfo.enqueue(new Callback<BaseBean<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseBean<UserInfo>> call, Response<BaseBean<UserInfo>> response) {
                if(response.body().getCode() == 200) {
                    Log.i(TAG, "onResponse: "+response.body().getData());
                    UserInfo data = response.body().getData();
                    editor.putString("username", data.getUsername());
                    editor.putString("avatar", data.getAvatar());
                    editor.putLong("id", data.getId());
                    editor.putBoolean("logged", true);
                    editor.apply();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<BaseBean<UserInfo>> call, Throwable t) {

            }
        });
    }

    void getSms() {
        String phone = etPhone.getText().toString();
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            Call<BaseBean<Boolean>> call = RetrofitManager.getInstance("").createApi().getSms(phone);
            call.enqueue(new Callback<BaseBean<Boolean>>() {
                @Override
                public void onResponse(Call<BaseBean<Boolean>> call, Response<BaseBean<Boolean>> response) {

                    Toast.makeText(LoginActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone", phone);
                    editor.apply();
                }

                @Override
                public void onFailure(Call<BaseBean<Boolean>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        }
    }

    void countDown() {
        Message message = Message.obtain();
        message.obj = currentSecond;
        message.what = 100;
        handler.sendMessageDelayed(message, 1000);
    }

}