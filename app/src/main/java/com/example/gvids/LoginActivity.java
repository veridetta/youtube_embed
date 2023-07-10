package com.example.gvids;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister, buttonContact;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseAuth, FirebaseFirestore, dan SharedPreferences

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Inisialisasi elemen UI
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonContact = findViewById(R.id.buttonContactUs);
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener pada tombol Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Set onClickListener pada tombol Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method untuk proses login
    private void login() {
        // Mendapatkan email dan password dari EditText
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validasi email dan password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Masukkan email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Masukkan password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proses login menggunakan Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login berhasil, lanjutkan ke halaman selanjutnya
                            handleRole();
                        } else {
                            // Login gagal, tampilkan pesan kesalahan
                            Toast.makeText(getApplicationContext(), "Login gagal. Silakan cek email dan password Anda.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method untuk menentukan tindakan berdasarkan peran pengguna
    private void handleRole() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Mengakses data role dari Firestore
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String role = document.getString("role");
                                    if (role != null) {
                                        // Simpan UID, nama, email, dan role ke SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("uid", userId);
                                        editor.putString("name", document.getString("name"));
                                        editor.putString("email", document.getString("email"));
                                        editor.putString("role", role);
                                        editor.apply();

                                        // Tindakan sesuai dengan peran pengguna
                                        if (role.equals("admin")) {
                                            // Pengguna dengan peran admin
                                            Toast.makeText(getApplicationContext(), "Halo, Admin!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (role.equals("user")) {
                                            // Pengguna dengan peran user
                                            Toast.makeText(getApplicationContext(), "Halo, User!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Peran pengguna tidak valid
                                            Toast.makeText(getApplicationContext(), "Peran pengguna tidak valid.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Data role tidak ditemukan
                                        Toast.makeText(getApplicationContext(), "Data role tidak ditemukan.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                // Penanganan kesalahan saat mengakses Firestore
                                Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat mengakses database.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
