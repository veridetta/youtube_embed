package com.example.gvids.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gvids.R;
import com.example.gvids.model.Podcast;
import com.example.gvids.user.DetailPodcastActivity;

import java.util.List;

public class PodcastUserAdapter extends RecyclerView.Adapter<PodcastUserAdapter.ViewHolder> {
    private Context context;
    private List<Podcast> podcastList;

    public PodcastUserAdapter(Context context, List<Podcast> podcastList) {
        this.context = context;
        this.podcastList = podcastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Podcast podcast = podcastList.get(position);

        String thumbnailUrl = podcast.getThumbnailUrl();
        String podcastTitle = podcast.getTitle();
        String podcastDescription = podcast.getDescription();

        holder.textViewVideoTitle.setText(podcastTitle);
        holder.textViewVideoDescription.setText(podcastDescription);
        Glide.with(context).load(thumbnailUrl).into(holder.imageViewThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Card diklik
                Intent intent = new Intent(context, DetailPodcastActivity.class);
                intent.putExtra("podcastTitle", podcastTitle);
                intent.putExtra("audioUrl", podcast.getAudioUrl());
                intent.putExtra("podcastDescription", podcastDescription);
                intent.putExtra("thumbnailUrl", thumbnailUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewVideoTitle, textViewVideoDescription, textViewProgram, textViewSubProgram;
        Button buttonViewVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewVideoTitle = itemView.findViewById(R.id.textViewVideoTitle);
            textViewVideoDescription = itemView.findViewById(R.id.textViewVideoDescription);
            textViewProgram = itemView.findViewById(R.id.textViewProgram);
            textViewSubProgram = itemView.findViewById(R.id.textViewSubProgram);

        }
    }
}
