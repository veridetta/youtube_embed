package com.example.gvids.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gvids.R;
import com.example.gvids.adapter.PodcastAdapter;
import com.example.gvids.model.Podcast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PodcastAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPodcasts;
    private PodcastAdapter podcastAdapter;
    private List<Podcast> podcastList;
    private FirebaseFirestore firestore;
    private CollectionReference podcastRef;

    Button buttonAddPodcast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_admin);

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance();
        podcastRef = firestore.collection("podcasts");
        buttonAddPodcast = findViewById(R.id.buttonAddPodcast);
        buttonAddPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Tambah Program diklik
                Intent intent = new Intent(PodcastAdminActivity.this, CreatePodcastActivity.class);
                startActivity(intent);
            }
        });
        // Inisialisasi RecyclerView dan podcastList
        recyclerViewPodcasts = findViewById(R.id.recyclerViewPodcasts);
        podcastList = new ArrayList<>();
        podcastAdapter = new PodcastAdapter(this, podcastList, firestore, podcastRef, new PodcastAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Podcast podcast) {
                // Buka CreatePodcastActivity dan kirim data podcast
                Intent intent = new Intent(PodcastAdminActivity.this, EditPodcastActivity.class);
                intent.putExtra("podcastId", podcast.getPodcastId());
                intent.putExtra("title", podcast.getTitle());
                intent.putExtra("description", podcast.getDescription());
                intent.putExtra("thumbnailUrl", podcast.getThumbnailUrl());
                intent.putExtra("audioUrl", podcast.getAudioUrl());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Podcast podcast) {
                // Hapus data podcast dari Firestore
                podcastRef.document(podcast.getPodcastId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Hapus data berhasil
                                Toast.makeText(PodcastAdminActivity.this, "Podcast dihapus", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Hapus data gagal
                                Toast.makeText(PodcastAdminActivity.this, "Gagal menghapus podcast", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Set layout manager dan adapter pada RecyclerView
        recyclerViewPodcasts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPodcasts.setAdapter(podcastAdapter);

        // Ambil data podcast dari Firestore
        podcastRef.orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot querySnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("PodcastAdminActivity", "Error getting podcast documents: " + e.getMessage());
                            return;
                        }

                        // Bersihkan daftar podcast sebelumnya
                        podcastList.clear();

                        // Tambahkan podcast baru dari Firestore ke daftar podcast
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Podcast podcast = document.toObject(Podcast.class);
                            podcastList.add(podcast);
                        }

                        // Refresh tampilan RecyclerView
                        podcastAdapter.notifyDataSetChanged();
                    }
                });
    }

    // ...
}
