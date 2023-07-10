package com.example.gvids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // Waktu tunda dalam milidetik (2 detik)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengatur layout yang digunakan oleh SplashScreenActivity
        setContentView(R.layout.activity_splash_screen);

        // Mengecek SharedPreferences untuk peran pengguna
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("role", "");

        // Membuat handler untuk menangani penundaan
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (userRole.equals("admin")) {
                    // Jika peran pengguna adalah admin, pindah ke AdminActivity
                    intent = new Intent(SplashScreen.this, AdminActivity.class);
                } else {
                    // Jika peran pengguna adalah user, pindah ke UserActivity
                    intent = new Intent(SplashScreen.this, UserActivity.class);
                }
                startActivity(intent);

                // Menutup SplashScreenActivity agar tidak dapat dikembalikan dengan tombol back
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
