package com.example.gvids.user;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arges.sepan.argmusicplayer.ArgMusicPlayer;
import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerLargeView;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;
import com.bumptech.glide.Glide;
import com.example.gvids.R;

public class DetailPodcastActivity extends AppCompatActivity {
    private TextView textViewPodcastTitle, textViewPodcastDescription;
    private ImageView imageViewPodcastCover;
    ArgAudio audio;
    ArgPlayerLargeView argMusicPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_podcast);

        // Mengambil data dari intent
        String title = getIntent().getStringExtra("podcastTitle");
        String description = getIntent().getStringExtra("podcastDescription");
        String audioUrl = getIntent().getStringExtra("audioUrl");
        String thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
        argMusicPlayer =  findViewById(R.id.podcastPlayer);
        // Inisialisasi komponen UI
        textViewPodcastTitle = findViewById(R.id.textViewPodcastTitle);
        textViewPodcastDescription = findViewById(R.id.textViewPodcastDescription);
        imageViewPodcastCover = findViewById(R.id.imageViewPodcastCover);

        // Mengatur judul dan deskripsi podcast
        textViewPodcastTitle.setText(title);
        textViewPodcastDescription.setText(description);

        // Memuat gambar cover menggunakan Glide
        Glide.with(this).load(thumbnailUrl).into(imageViewPodcastCover);

        // Inisialisasi dan konfigurasi ArgPlayer
         audio = ArgAudio.createFromURL(title, description, audioUrl);
        argMusicPlayer.play(audio);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Menghentikan pemutaran audio saat activity dihancurkan
        if (argMusicPlayer != null) {
            argMusicPlayer.stop();
        }
    }
}
