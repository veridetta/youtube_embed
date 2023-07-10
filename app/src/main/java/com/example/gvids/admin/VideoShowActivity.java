package com.example.gvids.admin;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gvids.R;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoShowActivity extends AppCompatActivity {

    // ID video yang sedang diputar
    String videoId = "";
    private YouTubePlayerView youtubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);

        // Inisialisasi YouTubePlayerView
        youtubePlayerView = findViewById(R.id.videoPlayer);

        // Mendapatkan ID video dari Intent jika ada
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("url")) {
            videoId = intent.getStringExtra("url");
            // Mendapatkan ID video dari URL YouTube
        }

        // Menambahkan YouTubePlayerListener ke YouTubePlayerView
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
    }

}
