package com.example.gvids;

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

import com.example.gvids.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword;
    private Button buttonRegister, buttonLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi FirebaseAuth, FirebaseFirestore, dan SharedPreferences
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Inisialisasi elemen UI
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set onClickListener pada tombol Daftar
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Set onClickListener pada tombol Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method untuk proses pendaftaran
    private void register() {
        // Mendapatkan nama, email, dan password dari EditText
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Masukkan nama!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Masukkan email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Masukkan password!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Proses pendaftaran menggunakan Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Pendaftaran berhasil, lanjutkan ke halaman selanjutnya
                            String userId = mAuth.getCurrentUser().getUid();
                            saveUserData(userId, name, email, "user"); // Simpan juga ke Firestore
                        } else {
                            // Pendaftaran gagal, tampilkan pesan kesalahan
                            Toast.makeText(getApplicationContext(), "Pendaftaran gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method untuk menyimpan data pengguna ke SharedPreferences dan Firestore
    private void saveUserData(String userId, String name, String email, String role) {
        // Simpan UID, nama, email, dan role ke SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", userId);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("role", role);
        editor.apply();

        // Simpan data pengguna ke Firestore
        DocumentReference userRef = db.collection("users").document(userId);
        User user = new User(userId, name, email, role);
        userRef.set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Tampilkan pesan sukses
                            Toast.makeText(getApplicationContext(), "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();

                            // Lanjutkan ke halaman selanjutnya
                            Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Pendaftaran gagal, tampilkan pesan kesalahan
                            Toast.makeText(getApplicationContext(), "Pendaftaran gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
