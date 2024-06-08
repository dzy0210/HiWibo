package com.example.weibo_duzhaoyang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weibo_duzhaoyang.adapters.ImageViewPagerAdapter;
import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadListener;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.android.downloader.exception.DownloadException;

import java.io.File;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private final String TAG = "dzy    ";
    ViewPager2 vpImage;
    List<String> images;
    TextView tvCurrent;
    TextView tvTotal;
    TextView tvDownload;
    TextView tvUsername;
    ImageView ivAvatar;
    String username;
    String avatar;
    int position = 0;

    SharedPreferencesUtil spu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
    }

    private void initView() {
        spu = MyApplication.getSp();
        tvCurrent = findViewById(R.id.tv_current);
        tvTotal = findViewById(R.id.tv_total);
        tvDownload = findViewById(R.id.tv_download);
        tvUsername = findViewById(R.id.tv_detail_username);
        ivAvatar = findViewById(R.id.iv_detail_avatar);
        Intent intent = getIntent();
        avatar = (String) spu.getData("avatar", "");
        username = (String) spu.getData("username", "");
        Bundle bundle = intent.getBundleExtra("imgBundle");
        images = bundle.getStringArrayList("images");

        position = intent.getIntExtra("position", 0);
        vpImage = findViewById(R.id.vp_image);
        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(images);
//        vpImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        vpImage.setAdapter(adapter);
        vpImage.setCurrentItem(position);
        tvCurrent.setText(String.valueOf(position));
        tvTotal.setText(String.valueOf(images.size()));
        tvDownload.setText("下载");
        tvUsername.setText(username);
        Glide.with(this).load(avatar).apply(new RequestOptions().circleCrop()).into(ivAvatar);
        vpImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(i, positionOffset, positionOffsetPixels);
                tvCurrent.setText(String.valueOf(i+1));
                position = i;
            }
        });
        tvDownload.setOnClickListener(v -> {
            Toast.makeText(ImageActivity.this, "点击了下载按钮", Toast.LENGTH_SHORT).show();
            DownloadManager downloadManager = DownloadService.getDownloadManager(getApplicationContext());
            String url = images.get(position);
            String filename = url.substring(url.lastIndexOf("/")+1);
            Log.i(TAG, "initView: "+getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            File targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
            DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url).setPath(targetFile.getAbsolutePath()).build();
            downloadInfo.setDownloadListener(new DownloadListener() {
                @Override
                public void onStart() {
                    Toast.makeText(ImageActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onWaited() {

                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onDownloading(long progress, long size) {

                }

                @Override
                public void onRemoved() {

                }

                @Override
                public void onDownloadSuccess() {
                    Toast.makeText(ImageActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDownloadFailed(DownloadException e) {

                }
            });
            downloadManager.download(downloadInfo);

        });
    }
}