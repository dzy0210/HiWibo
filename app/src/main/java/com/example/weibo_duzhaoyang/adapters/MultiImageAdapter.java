package com.example.weibo_duzhaoyang.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.weibo_duzhaoyang.R;

import java.util.List;

import me.simple.view.NineGridView;

public class MultiImageAdapter extends NineGridView.Adapter {
    List<String> images;
    OnBindViewCallback onBindView;
    OnItemClickCallback onItemViewClick;
    public MultiImageAdapter(List<String> images, OnBindViewCallback onBindView) {
        this.images = images;
        this.onBindView = onBindView;
    }
    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onBindItemView(@NonNull View view, int viewType, int position) {
        if (images == null || images.isEmpty())
            return;
        String image = images.get(position);
        if (image == null) {
            return;
        }
        view.setOnClickListener(v->{
            if (onItemViewClick != null) {
                onItemViewClick.onItemClick(image, position);
            }
        });
        if (onBindView != null) {
            onBindView.onBind((ImageView) view, image, position);
        }
    }

    @NonNull
    @Override
    public View onCreateItemView(@NonNull ViewGroup viewGroup, int i) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);
    }
    public interface OnBindViewCallback {
        void onBind(ImageView imageView, String item, int position);
    }
    public interface OnItemClickCallback {
        void onItemClick(String item, int position);
    }
    public void setOnItemViewClick(OnItemClickCallback onItemViewClick) {
        this.onItemViewClick = onItemViewClick;
    }
}
