package com.example.weibo_duzhaoyang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weibo_duzhaoyang.bean.BaseBean;
import com.example.weibo_duzhaoyang.bean.UserInfo;
import com.example.weibo_duzhaoyang.retrofit.RetrofitManager;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineFragment extends Fragment {
    ImageView ivAvtar;
    TextView tvUsername;
    TextView tvFansNum;
    MaterialToolbar toolbar;
    boolean logged;
    SharedPreferences sharedPreferences;
    TextView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("dzy", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        initToolbar(view);

        return view;
    }
    void initView(View view) {
        ivAvtar = view.findViewById(R.id.iv_avatar);
        tvUsername = view.findViewById(R.id.tv_user_name);
        tvFansNum = view.findViewById(R.id.tv_fans_num);
        ivAvtar.setOnClickListener(this::login);
        tvUsername.setOnClickListener(this::login);
        tvFansNum.setOnClickListener(this::login);
        loadData();
    }

    private void login(View view) {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    void initToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的");
        if (logged) {
            back = toolbar.findViewById(R.id.toolbar_operation);
            back.setText("退出");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("dzy", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("logged", false);
                    editor.apply();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }
    void loadData() {
        logged = sharedPreferences.getBoolean("logged", false);
        if (!logged) {
            Glide.with(ivAvtar).load(R.drawable.null_avatar).apply(new RequestOptions().placeholder(R.drawable.null_avatar).circleCrop()).into(ivAvtar);
            tvUsername.setText("请先登录");
            tvFansNum.setText("点击头像去登陆");
//            back.setVisibility(View.INVISIBLE);

        } else {
            ivAvtar.setEnabled(false);
            tvUsername.setEnabled(false);
            tvFansNum.setEnabled(false);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("dzy", Context.MODE_PRIVATE);
            tvUsername.setText(sharedPreferences.getString("username", ""));
            Glide.with(ivAvtar).load(sharedPreferences.getString("avatar", "")).apply(new RequestOptions().placeholder(R.drawable.null_avatar).circleCrop()).into(ivAvtar);
            tvFansNum.setText("粉丝数：9999");
            tvUsername.setText(sharedPreferences.getString("username", ""));
        }
    }
}