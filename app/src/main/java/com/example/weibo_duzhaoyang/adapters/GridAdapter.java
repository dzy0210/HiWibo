package com.example.weibo_duzhaoyang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.weibo_duzhaoyang.R;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    List<String> images;
    Context context;
    public GridAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public String getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setAdjustViewBounds(false);//设置边界对齐
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(8, 8, 8, 8);//设置间距
        }
        else {
            imageView = (ImageView) convertView;
        }
        Glide.with(context).load(images.get(position)).into(imageView);
        return imageView;
    }
}
