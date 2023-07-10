package com.example.gvids.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gvids.R;
import com.example.gvids.model.Podcast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreatePodcastActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_IMAGE = 101;
    private static final int REQUEST_AUDIO = 102;

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private ImageView imageViewCover;
    private Button buttonUploadCover;
    private ImageView imageViewAudio;
    private Button buttonUploadAudio;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSave;

    private FirebaseFirestore firestore;
    private CollectionReference podcastRef;
    private StorageReference storageRef;

    private ProgressDialog progressDialog;
    private Uri coverUri;
    private Uri audioUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_podcast);

        // Inisialisasi komponen UI
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);
        imageViewCover = findViewById(R.id.imageViewCover);
        buttonUploadCover = findViewById(R.id.buttonUploadCover);
        imageViewAudio = findViewById(R.id.imageViewAudio);
        buttonUploadAudio = findViewById(R.id.buttonUploadAudio);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance();
        podcastRef = firestore.collection("podcasts");

        // Inisialisasi Storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // Membuat dialog progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        // Mengatur OnClickListener untuk tombol "Upload Cover"
        buttonUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa izin akses penyimpanan eksternal
                if (ContextCompat.checkSelfPermission(CreatePodcastActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePodcastActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                } else {
                    // Memilih gambar cover dari galeri
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }
        });

        // Mengatur OnClickListener untuk tombol "Upload Audio"
        buttonUploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa izin akses penyimpanan eksternal
                if (ContextCompat.checkSelfPermission(CreatePodcastActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePodcastActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                } else {
                    // Memilih file audio podcast dari penyimpanan
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_AUDIO);
                }
            }
        });

        // Mengatur OnClickListener untuk tombol "Simpan"
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan nilai judul dan deskripsi podcast
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                // Memeriksa apakah cover dan audio sudah dipilih
                if (coverUri != null && audioUri != null) {
                    // Membuat podcastId secara acak
                    String podcastId = UUID.randomUUID().toString();

                    // Menyimpan data podcast ke Firestore
                    savePodcastData(podcastId, title, description);
                } else {
                    Toast.makeText(CreatePodcastActivity.this, "Mohon pilih cover dan audio podcast", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lanjutkan pemilihan gambar atau audio
                if (requestCode == REQUEST_IMAGE) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else if (requestCode == REQUEST_AUDIO) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_AUDIO);
                }
            } else {
                Toast.makeText(this, "Izin akses ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                // Mendapatkan URI gambar cover yang dipilih
                coverUri = data.getData();
                imageViewCover.setImageURI(coverUri);
            } else if (requestCode == REQUEST_AUDIO) {
                // Mendapatkan URI file audio podcast yang dipilih
                audioUri = data.getData();
                imageViewAudio.setImageResource(R.drawable.logo);
            }
        }
    }

    private void savePodcastData(String podcastId, String title, String description) {
        progressDialog.show();

        // Menyimpan cover podcast ke Firebase Storage
        StorageReference coverRef = storageRef.child("covers/" + podcastId);
        coverRef.putFile(coverUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Mendapatkan URL download cover podcast
                        coverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri coverUrl) {
                                // Menyimpan audio podcast ke Firebase Storage
                                StorageReference audioRef = storageRef.child("audios/" + podcastId);
                                audioRef.putFile(audioUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Mendapatkan URL download audio podcast
                                                audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri audioUrl) {
                                                        // Menyimpan data podcast ke Firestore
                                                        Podcast podcast = new Podcast(podcastId, title, description, coverUrl.toString(), audioUrl.toString());

                                                        podcastRef.document(podcastId)
                                                                .set(podcast)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(CreatePodcastActivity.this, "Podcast berhasil disimpan", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(CreatePodcastActivity.this, "Gagal menyimpan podcast", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(CreatePodcastActivity.this, "Gagal mengunggah audio podcast", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CreatePodcastActivity.this, "Gagal mengunggah cover podcast", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

