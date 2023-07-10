package com.example.gvids.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gvids.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPodcastActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonSave;

    private FirebaseFirestore firestore;
    private CollectionReference podcastRef;

    private String podcastId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_podcast);

        // Inisialisasi komponen UI
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance();
        podcastRef = firestore.collection("podcasts");

        // Mengambil data podcast dari intent
        podcastId = getIntent().getStringExtra("podcastId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        // Menampilkan data podcast pada form
        editTextTitle.setText(title);
        editTextDescription.setText(description);

        // Mengatur OnClickListener untuk tombol "Simpan"
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan nilai judul dan deskripsi podcast yang diubah
                String updatedTitle = editTextTitle.getText().toString();
                String updatedDescription = editTextDescription.getText().toString();

                // Mengupdate data podcast ke Firestore
                podcastRef.document(podcastId)
                        .update("title", updatedTitle, "description", updatedDescription)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Simpan perubahan berhasil
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Simpan perubahan gagal
                                Toast.makeText(EditPodcastActivity.this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
