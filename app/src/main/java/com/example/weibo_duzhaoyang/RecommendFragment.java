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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weibo_duzhaoyang.adapters.MultiItemAdapter;
import com.example.weibo_duzhaoyang.bean.BaseBean;
import com.example.weibo_duzhaoyang.bean.IdBean;
import com.example.weibo_duzhaoyang.bean.Page;
import com.example.weibo_duzhaoyang.bean.WeiboInfo;
import com.example.weibo_duzhaoyang.retrofit.RetrofitManager;
import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    static int current;
    SwipeRefreshLayout srl;
    TextView tvRetry;
    ConstraintLayout clFail;
    ConstraintLayout clLoading;
    boolean flag = false;
    String token;

    boolean logged;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: 44444");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_recommend, container, false);
        current = 1;
        initView();
        initToolbar();
        initRecyclerView();
        clLoading.setVisibility(View.VISIBLE);
        clFail.setVisibility(View.GONE);
        srl.setVisibility(View.GONE);
        Log.i(TAG, "onCreateView: 33333");
        search();
        return view;
    }

    private void search() {
        Log.i(TAG, "search: "+token);
        Log.i(TAG, "search: "+current);
        Call<BaseBean<Page>> call = RetrofitManager.getInstance(token).createApi().getWeiboList(current, 10);
        call.enqueue(new Callback<BaseBean<Page>>() {
            @Override
            public void onResponse(Call<BaseBean<Page>> call, Response<BaseBean<Page>> response) {
                if (response.body() != null && response.body().getCode() == 403) {
                    spu.putData("token", "");
                    spu.putData("logged", false);
                    spu.putData("username", "");
                    spu.putData("id", 0);
                    spu.putData("avatar", "");
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                if (response.body() != null) {
                    Log.i(TAG, "onResponse: 5dada651"+response.body().getCode());
                    List<WeiboInfo> data = response.body().getData().getRecords();
                    if (data.isEmpty()) {
                        Toast.makeText(getContext(), "没有更多内容了", Toast.LENGTH_SHORT).show();
                        return;
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
        spu = MyApplication.getSp();
        token = (String) spu.getData("token", "");
        logged = (Boolean) spu.getData("logged", false);
        rv = view.findViewById(R.id.rv);
        list = new ArrayList<>();
        adapter = new MultiItemAdapter(list);
        adapter.setOnItemClickListener(new MultiItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.cl_comment) {
                    if (!logged) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(getContext(), "点击了评论按钮", Toast.LENGTH_SHORT).show();
                    }

                } else if (view.getId() == R.id.cl_like) {
                    if (!logged) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        if (list.get(position).getLikeFlag()) {
                            dislike(list.get(position).getId(), position, view);
                        } else {
                            like(list.get(position).getId(), position, view);
                        }
                    }
                } else if (view.getId() == R.id.iv_item_delete) {
                    list.remove(position);
                    adapter.notifyItemChanged(position);
                } else if (view.getId() == R.id.iv_single_image) {
                    Intent intent = new Intent(getContext(), ImageActivity.class);
                    intent.putExtra("position", position);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("images", (ArrayList<String>) list.get(position).getImages());
                    intent.putExtra("imgBundle", bundle);
                    startActivity(intent);
                }
            }
        });
        srl = view.findViewById(R.id.srl);
        tvRetry = view.findViewById(R.id.tv_retry);
        clLoading = view.findViewById(R.id.cl_loading);
        clFail = view.findViewById(R.id.cl_fail);
        srl.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
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
                int itemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 2 >= itemCount) {
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

    private void dislike(Long id, int position, View view) {
        Call<BaseBean<Boolean>> call = RetrofitManager.getInstance(token).createApi().dislike(new IdBean(id));
        call.enqueue(new Callback<BaseBean<Boolean>>() {
            @Override
            public void onResponse(Call<BaseBean<Boolean>> call, Response<BaseBean<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 403) {
                    spu.putData("token", "");
                    spu.putData("logged", false);
                    spu.putData("username", "");
                    spu.putData("id", 0);
                    spu.putData("avatar", "");
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                else if (response.body() != null &&response.body().code == 200 && response.body().getData()) {
                    list.get(position).setLikeFlag(false);
                    dislikeAnim(position, view.findViewById(R.id.iv_like));
                }
            }
            @Override
            public void onFailure(Call<BaseBean<Boolean>> call, Throwable t) {
            }
        });
    }




    private void like(Long id, int position, View view) {
        Call<BaseBean<Boolean>> call = RetrofitManager.getInstance(token).createApi().like(new IdBean(id));
        call.enqueue(new Callback<BaseBean<Boolean>>() {
            @Override
            public void onResponse(Call<BaseBean<Boolean>> call, Response<BaseBean<Boolean>> response) {
                if (response.body() != null) {
                    BaseBean<Boolean> body = response.body();
                    Log.i(TAG, "onResponse: "+body);
                    if (body.getCode() == 403) {
                        spu.putData("token", "");
                        spu.putData("logged", false);
                        spu.putData("username", "");
                        spu.putData("id", 0);
                        spu.putData("avatar", "");
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else if (body.getCode() == 200 && body.getData()) {
                        list.get(position).setLikeFlag(true);
                        likeAnim(position, view.findViewById(R.id.iv_like));
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseBean<Boolean>> call, Throwable t) {

            }
        });
    }
    private void likeAnim(int position, View view) {
        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(
                1.0f, 1.2f,
                1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(500);
        animSet.addAnimation(scaleAnim);

        ScaleAnimation scaleBackAnim = new ScaleAnimation(
                1.2f, 1.0f,
                1.2f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleBackAnim.setDuration(500); //
        scaleBackAnim.setStartOffset(500);
        animSet.addAnimation(scaleBackAnim);
        RotateAnimation rotateAnim = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(1000);
        rotateAnim.setStartOffset(0);
        animSet.addAnimation(rotateAnim);
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                adapter.notifyItemChanged(position);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(animSet);
    }
    private void dislikeAnim(int position, View view) {
        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(
                1.0f, 0.8f,
                1.0f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(500);
        animSet.addAnimation(scaleAnim);

        ScaleAnimation scaleBackAnim = new ScaleAnimation(
                0.8f, 1.0f,
                0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleBackAnim.setDuration(500); //
        scaleBackAnim.setStartOffset(500);
        animSet.addAnimation(scaleBackAnim);
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                adapter.notifyItemChanged(position);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(animSet);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: "+11111);
        token = (String) spu.getData("token", "");
        logged = (Boolean) spu.getData("logged", false);
    }

}