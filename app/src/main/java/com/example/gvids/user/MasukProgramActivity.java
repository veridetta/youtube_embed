package com.example.gvids.user;

import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gvids.R;
import com.example.gvids.adapter.ProgramUserAdapter;
import com.example.gvids.model.Program;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MasukProgramActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPrograms;
    private ProgramUserAdapter programUserAdapter;
    private List<Program> programList;
    private FirebaseFirestore firestore;
    private String selectedProgram,subProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk_program);

        // Mengambil program yang dipilih dari intent
        selectedProgram = getIntent().getStringExtra("program");
        subProgram = getIntent().getStringExtra("subProgram");
        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance();

        // Inisialisasi RecyclerView
        recyclerViewPrograms = findViewById(R.id.recyclerViewPrograms);
        // Mengatur GridLayoutManager dengan 2 kolom
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewPrograms.setLayoutManager(layoutManager);

        // Menginisialisasi daftar program
        programList = new ArrayList<>();

        // Mendapatkan referensi koleksi "programs" dari Firestore
        CollectionReference programsRef = firestore.collection("programs");

        // Membuat query untuk mengambil program berdasarkan program dan subprogram
        Query query = programsRef.whereEqualTo("program", selectedProgram);

        // Mengambil data program berdasarkan query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Mendapatkan daftar dokumen program dari hasil query
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Membuat objek Program dari data dokumen
                    Program program = documentSnapshot.toObject(Program.class);

                    // Filter berdasarkan subprogram untuk Program 1 - Daily Routine
                    if (selectedProgram.equals("Program 1 - Daily Routine") && program.getSubProgram().equals(subProgram)) {
                        programList.add(program);
                    }
                }

                // Inisialisasi dan atur adapter RecyclerView
                programUserAdapter = new ProgramUserAdapter(MasukProgramActivity.this, programList);
                recyclerViewPrograms.setAdapter(programUserAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Jika gagal mengambil data dari Firestore, tampilkan pesan error
                Log.e("MasukProgramActivity", "Error getting programs", e);
            }
        });
    }
}
