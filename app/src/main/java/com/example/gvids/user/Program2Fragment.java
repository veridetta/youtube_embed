package com.example.gvids.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Program2Fragment extends Fragment {

    private RecyclerView recyclerViewPrograms;
    private ProgramUserAdapter programUserAdapter;
    private List<Program> programList;
    private FirebaseFirestore firestore;
    private String selectedProgram, subProgram;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mengambil program yang dipilih dari argument
        //selectedProgram = getArguments().getString("program");
        selectedProgram = "Program 2 - Special Event";


        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program2, container, false);

        // Inisialisasi RecyclerView
        recyclerViewPrograms = view.findViewById(R.id.recyclerViewPrograms);

        // Mengatur GridLayoutManager dengan 2 kolom
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
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
                    programList.add(program);
                }

                // Inisialisasi dan atur adapter RecyclerView
                programUserAdapter = new ProgramUserAdapter(getContext(), programList);
                recyclerViewPrograms.setAdapter(programUserAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Jika gagal mengambil data dari Firestore, tampilkan pesan error
                Log.e("Program2Fragment", "Error getting programs", e);
            }
        });

        return view;
    }
}
