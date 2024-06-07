package com.example.weibo_duzhaoyang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weibo_duzhaoyang.adapters.MultiItemAdapter;
import com.example.weibo_duzhaoyang.bean.BaseBean;
import com.example.weibo_duzhaoyang.bean.Page;
import com.example.weibo_duzhaoyang.bean.WeiboInfo;
import com.example.weibo_duzhaoyang.retrofit.RetrofitManager;
import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendFragment extends Fragment {
    private final String TAG = "dzy    RecommendFragment";
    MaterialToolbar toolbar;
    View view;
    RecyclerView rv;
    SharedPreferencesUtil spu;
    List<WeiboInfo> list;
    MultiItemAdapter adapter;
    static int current = 1;
    SwipeRefreshLayout srl;
    TextView tvRetry;
    ConstraintLayout clFail;
    ConstraintLayout clLoading;
    boolean flag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recommend, container, false);
        initView();
        initToolbar();
        initRecyclerView();
        clLoading.setVisibility(View.VISIBLE);
        clFail.setVisibility(View.GONE);
        srl.setVisibility(View.GONE);
        search();
        return view;
    }

    private void search() {
        if (list.size()==0) {
//            clLoading.setVisibility(View.VISIBLE);
        }
        spu = MyApplication.getSp();
        String token = (String) spu.getData("token", "");
        Call<BaseBean<Page>> call = RetrofitManager.getInstance(token).createApi().getWeiboList(current, 10);
        call.enqueue(new Callback<BaseBean<Page>>() {
            @Override
            public void onResponse(Call<BaseBean<Page>> call, Response<BaseBean<Page>> response) {
                List<WeiboInfo> data = response.body().getData().getRecords();
                if (data.isEmpty()) {
                    Toast.makeText(getContext(), "没有更多内容了", Toast.LENGTH_SHORT).show();
                }
                if (current == 1) {
                    list.clear();
                }
                list.addAll(data);
                clLoading.setVisibility(View.GONE);
                clFail.setVisibility(View.GONE);
                srl.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                current++;
                flag = true;
            }

            @Override
            public void onFailure(Call<BaseBean<Page>> call, Throwable t) {
                if (!flag) {
                    clLoading.setVisibility(View.GONE);
                    srl.setVisibility(View.GONE);
                    clFail.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    void initView() {
        rv = view.findViewById(R.id.rv);
        list = new ArrayList<>();
        adapter = new MultiItemAdapter(list);
        srl = view.findViewById(R.id.srl);
        tvRetry = view.findViewById(R.id.tv_retry);
        clLoading = view.findViewById(R.id.cl_loading);
        clFail = view.findViewById(R.id.cl_fail);
        srl.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
//                list.clear();
                current = 1;
                search();
                srl.setRefreshing(false);
            }, 1000);
        });

        tvRetry.setOnClickListener(v -> {
            search();
        });
    }
    private void initRecyclerView() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
//                int childCount = layoutManager.getChildCount();
                int itemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 3 > itemCount) {
                    search();
                }
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    void initToolbar() {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("iH微博");
    }
}