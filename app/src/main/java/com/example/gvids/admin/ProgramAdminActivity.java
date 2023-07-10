package com.example.gvids.admin;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gvids.R;
import com.example.gvids.adapter.ProgramAdminAdapter;
import com.example.gvids.model.Program;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrograms;
    private ProgramAdminAdapter programAdminAdapter;
    private List<Program> programList;

    private Button buttonAddProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_admin);

        recyclerViewPrograms = findViewById(R.id.recyclerViewPrograms);
        buttonAddProgram = findViewById(R.id.buttonAddProgram);

        programList = new ArrayList<>();
        programAdminAdapter = new ProgramAdminAdapter(this, programList);

        recyclerViewPrograms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPrograms.setAdapter(programAdminAdapter);

        buttonAddProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Tambah Program diklik
                Intent intent = new Intent(ProgramAdminActivity.this, CreateProgramActivity.class);
                startActivity(intent);
            }
        });

        // Method untuk mengambil data program dari Firestore
        fetchPrograms();
    }


    private void fetchPrograms() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference programsRef = db.collection("programs");

        programsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                programList.clear(); // Bersihkan daftar program sebelum memperbarui dengan data terbaru

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Dapatkan data program dari documentSnapshot
                    String programId = documentSnapshot.getId();
                    String videoTitle = documentSnapshot.getString("title");
                    String videoDescription = documentSnapshot.getString("description");
                    String program = documentSnapshot.getString("program");
                    String subProgram = documentSnapshot.getString("subProgram");
                    String thumbnailUrl = documentSnapshot.getString("thumbnailUrl");
                    String videoUrl = documentSnapshot.getString("url");

                    // Buat objek Program dari data yang diperoleh
                    Program program2 = new Program(programId, videoTitle, videoDescription, program, subProgram, thumbnailUrl, videoUrl);

                    // Tambahkan program ke daftar program
                    programList.add(program2);
                }

                programAdminAdapter.notifyDataSetChanged(); // Perbarui tampilan RecyclerView dengan data terbaru
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error saat mengambil data program dari Firestore
                Toast.makeText(ProgramAdminActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
