package com.example.weibo_duzhaoyang.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weibo_duzhaoyang.R;
import com.example.weibo_duzhaoyang.bean.WeiboInfo;

import java.io.IOException;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import me.simple.view.ImageAdapter;
import me.simple.view.NineGridView;

public class MultiItemAdapter extends RecyclerView.Adapter<MultiItemAdapter.ViewHolder> {
    private static final int VIDEO_TYPE = 0;
    private static final int SINGLE_IMAGE_TYPE = 1;
    private static final int MULTI_IMAGE_TYPE = 2;
    private static final int TEXT_TYPE = 3;

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
        holder.tvLike.setText("点赞");
        Glide.with(holder.itemView.getContext()).load(R.drawable.like).into(holder.ivLike);
        Glide.with(holder.itemView.getContext()).load(R.drawable.comment).into(holder.ivComment);
        Glide.with(holder.itemView.getContext()).load(weiboInfo.getAvatar()).into(holder.ivItemIcon);
        Glide.with(holder.itemView.getContext()).load(R.drawable.delete).into(holder.ivItemDelete);
        if (weiboInfo.getVideoUrl() != null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(weiboInfo.getVideoUrl());
                mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mediaPlayer.setOnPreparedListener(mp -> mp.pause());
            VideoView videoView = ((VideoVH)holder).videoView;
            ImageView ivPoster = ((VideoVH)holder).ivPoster;
            videoView.setVideoURI(Uri.parse(weiboInfo.getVideoUrl()));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Glide.with(ivPoster).load(weiboInfo.getPoster()).into(ivPoster);
                    ivPoster.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivPoster.setVisibility(View.GONE);
                            videoView.start();
                        }
                    });
                }
            });

            videoView.setOnClickListener(v -> {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            });
            videoView.setOnCompletionListener(mp -> {
                videoView.seekTo(0);
                videoView.start();
            });

            return;
        }
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() == 1) {
            Glide.with(holder.itemView.getContext()).load(weiboInfo.getImages().get(0)).into(((SingleImageVH)holder).ivSingleImage);
            return;
        }
        if (weiboInfo.getImages() != null && weiboInfo.getImages().size() > 1) {

            NineGridView ngv = ((MultiImageVH) holder).ngv;
            ImageAdapter<String> adapter = new ImageAdapter<>(weiboInfo.getImages(), (imageView, s, integer) -> {
                Glide.with(imageView).load(s).into(imageView);
                return null;
            });
            ngv.setAdapter(adapter);
            return;
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
        NineGridView ngv;
        public MultiImageVH(@NonNull View itemView) {
            super(itemView);
            ngv = itemView.findViewById(R.id.ngv);
        }
    }
    static class VideoVH extends ViewHolder{
        VideoView videoView;
        ImageView ivVideoPlay;
        SeekBar seekBar;
        ImageView ivPoster;
        public VideoVH(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            ivVideoPlay = itemView.findViewById(R.id.iv_video_play);
            seekBar = itemView.findViewById(R.id.seek_bar);
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
        }
    }
}
