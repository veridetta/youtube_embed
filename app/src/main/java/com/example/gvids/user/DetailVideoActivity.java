package com.example.gvids.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gvids.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class DetailVideoActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private TextView textViewVideoTitle, textViewVideoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);

        youTubePlayerView = findViewById(R.id.videoPlayer);
        textViewVideoTitle = findViewById(R.id.textViewVideoTitle);
        textViewVideoDescription = findViewById(R.id.textViewVideoDescription);

        // Ambil data video (judul dan deskripsi) dari intent
        String videoTitle = getIntent().getStringExtra("title");
        String videoDescription = getIntent().getStringExtra("description");

        // Set judul dan deskripsi video
        textViewVideoTitle.setText(videoTitle);
        textViewVideoDescription.setText(videoDescription);

        // Inisialisasi YouTubePlayerView
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                // Ambil URL video dari intent
                String videoUrl = getIntent().getStringExtra("url");

                // Load video YouTube
                youTubePlayer.loadVideo(videoUrl, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
