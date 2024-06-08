package com.example.weibo_duzhaoyang.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weibo_duzhaoyang.R;

import java.util.List;
import java.util.zip.Inflater;

public class MultiImageRecyclerAdapter extends RecyclerView.Adapter<MultiImageRecyclerAdapter.ImageViewHolder> {

    OnMultiImageItemClickListener onMultiImageItemClickListener;
    List<String> images;
    public MultiImageRecyclerAdapter(List<String> images) {
        this.images = images;
    }

    public interface OnMultiImageItemClickListener {
        void onMultiImageItemClick(View view, int position);
    }

    public void setOnMultiImageItemClickListener(OnMultiImageItemClickListener onMultiImageItemClickListener) {
        this.onMultiImageItemClickListener = onMultiImageItemClickListener;
    }

    @NonNull
    @Override
    public MultiImageRecyclerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiImageRecyclerAdapter.ImageViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(images.get(position)).into(holder.ivImage);
        if (onMultiImageItemClickListener != null) {
            holder.ivImage.setOnClickListener(v -> onMultiImageItemClickListener.onMultiImageItemClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
        }
    }
}
