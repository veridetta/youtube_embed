package com.example.gvids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gvids.admin.PodcastAdminActivity;
import com.example.gvids.admin.ProgramAdminActivity;

public class AdminActivity extends AppCompatActivity {
    private Button buttonProgram, buttonPodcast, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inisialisasi elemen UI
        buttonProgram = findViewById(R.id.buttonProgram);
        buttonPodcast = findViewById(R.id.buttonPodcast);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus Shared Preferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Lakukan tindakan lain setelah logout, seperti memindahkan pengguna ke halaman login
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Menutup MainActivity agar tidak dapat dikembalikan dengan tombol back
            }
        });
        // Set onClickListener pada tombol Program
        buttonProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke ProgramAdminActivity
                Intent intent = new Intent(AdminActivity.this, ProgramAdminActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener pada tombol Podcast
        buttonPodcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke PodcastAdminActivity
                Intent intent = new Intent(AdminActivity.this, PodcastAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}
