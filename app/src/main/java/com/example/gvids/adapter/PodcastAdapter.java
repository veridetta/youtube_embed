package com.example.gvids.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;
import com.bumptech.glide.Glide;
import com.example.gvids.R;
import com.example.gvids.model.Podcast;
import com.example.gvids.user.DetailPodcastActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder> {

    private Context context;
    private List<Podcast> podcastList;
    private FirebaseFirestore firestore;
    private CollectionReference podcastRef;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Podcast podcast);
        void onDeleteClick(Podcast podcast);
    }

    public PodcastAdapter(Context context, List<Podcast> podcastList, FirebaseFirestore firestore, CollectionReference podcastRef, OnItemClickListener listener) {
        this.context = context;
        this.podcastList = podcastList;
        this.firestore = firestore;
        this.podcastRef = podcastRef;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_podcast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Podcast podcast = podcastList.get(position);

        holder.textViewTitle.setText(podcast.getTitle());
        holder.textViewDescription.setText(podcast.getDescription());
        String url = podcast.getAudioUrl();
        ArgAudio audio = ArgAudio.createFromURL(podcast.getTitle(), podcast.getDescription(), url);
        holder.argMusicPlayer.loadSingleAudio(audio);

        Glide.with(context)
                .load(podcast.getThumbnailUrl())
                .placeholder(R.drawable.logo)
                .into(holder.imageViewCover);

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(podcast);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(podcast);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Card diklik
                Intent intent = new Intent(context, DetailPodcastActivity.class);
                intent.putExtra("podcastTitle", podcast.getTitle());
                intent.putExtra("audioUrl", podcast.getAudioUrl());
                intent.putExtra("podcastDescription", podcast.getDescription());
                intent.putExtra("thumbnailUrl", podcast.getThumbnailUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;
        TextView textViewDescription;
        Button buttonEdit;
        Button buttonDelete;
        ArgPlayerSmallView argMusicPlayer ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
             argMusicPlayer = (ArgPlayerSmallView) itemView.findViewById(R.id.argmusicplayer);
        }
    }
}
