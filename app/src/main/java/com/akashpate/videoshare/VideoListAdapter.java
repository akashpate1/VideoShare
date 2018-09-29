package com.akashpate.videoshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import java.util.LinkedList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private final LinkedList<String> mVideoList;
    private LayoutInflater mInflater;


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.video_list_item,parent,false);
        return new VideoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final String mCurrent = mVideoList.get(position);
        holder.videoItemView.setVideoURI(Uri.parse(mCurrent));
        holder.videoItemView.seekTo(100);

        /*holder.videoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mCurrent),"video/*");
                v.getContext().startActivity(intent);
            }
        });*/

        holder.videoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mCurrent),"video/*");
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public VideoListAdapter(Context context,LinkedList<String> videoList){
        mInflater = LayoutInflater.from(context);
        this.mVideoList = videoList;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        final VideoView videoItemView;
        final CardView videoCardView;
        final VideoListAdapter mAdapter;

        VideoViewHolder(View itemView,VideoListAdapter adapter){
            super(itemView);
            videoItemView = itemView.findViewById(R.id.video);
            videoCardView = itemView.findViewById(R.id.videoCard);
            this.mAdapter = adapter;

        }

    }


}
