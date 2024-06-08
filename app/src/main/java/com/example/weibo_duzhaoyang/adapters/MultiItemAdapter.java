package com.example.weibo_duzhaoyang.adapters;

import static com.example.weibo_duzhaoyang.MyApplication.getProxy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.example.weibo_duzhaoyang.ImageActivity;
import com.example.weibo_duzhaoyang.R;
import com.example.weibo_duzhaoyang.bean.WeiboInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import me.simple.view.ImageAdapter;
import me.simple.view.NineGridView;

public class MultiItemAdapter extends RecyclerView.Adapter<MultiItemAdapter.ViewHolder> {
    private final String TAG = "dzy   MultiItemAdapter";
    private static final int VIDEO_TYPE = 0;
    private static final int SINGLE_IMAGE_TYPE = 1;
    private static final int MULTI_IMAGE_TYPE = 2;
    private static final int TEXT_TYPE = 3;
    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    List<WeiboInfo> list;
    public MultiItemAdapter(List<WeiboInfo> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIDEO_TYPE:
                return new VideoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
            case SINGLE_IMAGE_TYPE:
                return new SingleImageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_image, parent, false));
            case MULTI_IMAGE_TYPE:
                return new MultiImageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_image, parent, false));
            default:
                return new TextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeiboInfo weiboInfo = list.get(position);
        holder.tvItemTitle.setText(weiboInfo.getTitle());
        holder.tvItemTitle.setMaxLines(6);
        holder.tvItemUsername.setText(weiboInfo.getUsername());
        holder.tvComment.setText("评论");
        holder.tvLike.setText(weiboInfo.getLikeFlag()?String.valueOf(weiboInfo.getLikeCount()):"点赞");
        holder.tvLike.setTextColor(weiboInfo.getLikeFlag()?Color.parseColor("#EA512F"):Color.parseColor("#B2000000"));
        if (onItemClickListener != null) {
            holder.clComment.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
            holder.clLike.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
            holder.ivItemDelete.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
        }

        Glide.with(holder.itemView.getContext()).load(weiboInfo.getLikeFlag()?R.drawable.liked:R.drawable.like).into(holder.ivLike);
        Glide.with(holder.itemView.getContext()).load(R.drawable.comment).into(holder.ivComment);
        Glide.with(holder.itemView.getContext()).load(weiboInfo.getAvatar()).apply(new RequestOptions().circleCrop()).into(holder.ivItemIcon);
        Glide.with(holder.itemView.getContext()).load(R.drawable.delete).into(holder.ivItemDelete);

        //视频item
        if (weiboInfo.getVideoUrl() != null) {
            PlayerView playerView = ((VideoVH)holder).playerView;
            ImageView ivPoster = ((VideoVH)holder).ivPoster;

            Glide.with(ivPoster).load(weiboInfo.getPoster()).into(ivPoster);
            ExoPlayer exoPlayer = new ExoPlayer
                    .Builder(holder.itemView.getContext())
                    .setMediaSourceFactory(new DefaultMediaSourceFactory(holder.itemView.getContext()))
                    .build();
            HttpProxyCacheServer proxy = getProxy(holder.itemView.getContext());
            //注意应采用来自代理的 url 而不是原始 url 来添加缓存
            String proxyUrl = proxy.getProxyUrl(weiboInfo.getVideoUrl());

            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (isPlaying) {
                        ivPoster.setVisibility(View.GONE);
                    }
                }
            });
            playerView.setPlayer(exoPlayer);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            exoPlayer.setMediaItem(MediaItem.fromUri(proxyUrl));
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.prepare();
            ivPoster.setOnClickListener(v -> exoPlayer.play());

            return;
        }

        //单图片item
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() == 1) {
            Glide.with(holder.itemView.getContext()).load(weiboInfo.getImages().get(0)).into(((SingleImageVH)holder).ivSingleImage);
            if (onItemClickListener != null) {
                ImageView ivSingleImage = ((SingleImageVH)holder).ivSingleImage;
                ivSingleImage.setOnClickListener(v->{
                    onItemClickListener.onItemClick(v, position);
                });
            }
            return;
        }

        //多图片item
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() > 1) {
            RecyclerView rvMultiImage = ((MultiImageVH)holder).rvMultiImage;

            rvMultiImage.setOnClickListener(v -> Toast.makeText(rvMultiImage.getContext(), "recycleview", Toast.LENGTH_SHORT).show());

            MultiImageRecyclerAdapter adapter = new MultiImageRecyclerAdapter(weiboInfo.getImages());
            adapter.setOnMultiImageItemClickListener((view, position1) -> {
                Intent intent = new Intent(holder.itemView.getContext(), ImageActivity.class);
                intent.putExtra("position", position1);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", (ArrayList<String>) weiboInfo.getImages());
                intent.putExtra("imgBundle", bundle);
                holder.itemView.getContext().startActivity(intent);
            });
            rvMultiImage.setLayoutManager(new GridLayoutManager(rvMultiImage.getContext(), 3));
            rvMultiImage.setAdapter(adapter);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof VideoVH) {
            if (((VideoVH) holder).playerView.getPlayer().isPlaying()) {
                ((VideoVH) holder).playerView.getPlayer().pause();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        WeiboInfo weiboInfo = list.get(position);
        if (weiboInfo.getVideoUrl() != null) {
            return VIDEO_TYPE;
        }
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() == 1) {
            return SINGLE_IMAGE_TYPE;
        }
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() > 1) {
            return MULTI_IMAGE_TYPE;
        }
        return TEXT_TYPE;
    }
    static class TextVH extends ViewHolder {
        public TextVH(@NonNull View itemView)  {
            super(itemView);
        }
    }
    static class SingleImageVH extends ViewHolder {
        ImageView ivSingleImage;
        public SingleImageVH(@NonNull View itemView) {
            super(itemView);
            ivSingleImage = itemView.findViewById(R.id.iv_single_image);
        }
    }

    static class MultiImageVH extends ViewHolder {
        RecyclerView rvMultiImage;
        public MultiImageVH(@NonNull View itemView) {
            super(itemView);
            rvMultiImage = itemView.findViewById(R.id.rv_multi_image);
        }
    }
    static class VideoVH extends ViewHolder{
        PlayerView playerView;
        SeekBar seekBar;
        ImageView ivPoster;
        public VideoVH(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.player_view);
            ivPoster = itemView.findViewById(R.id.iv_poster);
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivItemIcon;
        TextView tvItemUsername;
        ImageView ivItemDelete;
        TextView tvItemTitle;
        ImageView ivLike;
        ImageView ivComment;
        TextView tvLike;
        TextView tvComment;
        ConstraintLayout clLike;
        ConstraintLayout clComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemIcon = itemView.findViewById(R.id.iv_item_icon);
            tvItemUsername = itemView.findViewById(R.id.tv_item_username);
            ivItemDelete = itemView.findViewById(R.id.iv_item_delete);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvLike = itemView.findViewById(R.id.tv_like);
            ivComment = itemView.findViewById(R.id.iv_comment);
            ivLike = itemView.findViewById(R.id.iv_like);
            clLike = itemView.findViewById(R.id.cl_like);
            clComment = itemView.findViewById(R.id.cl_comment);
        }
    }
}
