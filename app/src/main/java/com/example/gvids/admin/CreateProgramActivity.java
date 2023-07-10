package com.example.gvids.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gvids.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateProgramActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextTitle;
    private Spinner spinnerProgram;
    private Spinner spinnerSubProgram;
    private EditText editTextUrl;
    private EditText editTextDescription;
    private Button buttonSubmit;

    private String selectedProgram;
    private String selectedSubProgram;

    private FirebaseFirestore db;
    private boolean isEditMode;
    private String programId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        editTextTitle = findViewById(R.id.editTextTitle);
        spinnerProgram = findViewById(R.id.spinnerProgram);
        spinnerSubProgram = findViewById(R.id.spinnerSubProgram);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set listener untuk spinner program
        spinnerProgram.setOnItemSelectedListener(this);

        // Inisialisasi ArrayAdapter untuk spinner program
        ArrayAdapter<CharSequence> programAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.programs_array,
                android.R.layout.simple_spinner_item
        );
        programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter untuk spinner program
        spinnerProgram.setAdapter(programAdapter);

        // Inisialisasi Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Periksa apakah dalam mode edit atau mode buat baru
        Intent intent = getIntent();
        if (intent.hasExtra("programId")) {
            isEditMode = true;
            programId = intent.getStringExtra("programId");

            // Ambil data program dari intent
            String title = intent.getStringExtra("title");
            String program = intent.getStringExtra("program");
            String subProgram = intent.getStringExtra("subProgram");
            String url = intent.getStringExtra("url");
            String description = intent.getStringExtra("description");

            // Set data program ke tampilan
            editTextTitle.setText(title);
            //spinnerProgram.setSelection(getIndex(spinnerProgram, program));
            if (subProgram != null) {
                spinnerSubProgram.setVisibility(View.VISIBLE);
                //spinnerSubProgram.setSelection(getIndex(spinnerSubProgram, subProgram));
            }
            editTextUrl.setText(url);
            editTextDescription.setText(description);
        } else {
            isEditMode = false;
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Submit diklik
                if (isEditMode) {
                    updateProgram();
                } else {
                    createProgram();
                }
            }
        });
    }

    private void createProgram() {
        String title = editTextTitle.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (selectedProgram.equals("Program 1 - Daily Routine")) {
            selectedSubProgram = spinnerSubProgram.getSelectedItem().toString();
        }else{
            selectedSubProgram = null;
        }
        if (title.isEmpty() || url.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat programId secara unik dengan UUID.randomUUID().toString()
        String programId = UUID.randomUUID().toString();

        // Buat objek program dengan data yang diisi
        Map<String, Object> program = new HashMap<>();
        program.put("programId", programId);
        program.put("title", title);
        program.put("program", selectedProgram);
        program.put("subProgram", selectedSubProgram);
        program.put("url", url);
        program.put("description", description);

        // Simpan data program ke Firestore
        CollectionReference programsRef = db.collection("programs");
        programsRef.document(programId)
                .set(program)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Program berhasil disimpan dengan ID: " + programId, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateProgramActivity.this, ProgramAdminActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProgram() {
        String title = editTextTitle.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (selectedProgram.equals("Program 1 - Daily Routine")) {
            selectedSubProgram = spinnerSubProgram.getSelectedItem().toString();
        }else{
            selectedSubProgram = null;
        }
        if (title.isEmpty() || url.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat objek program dengan data yang diisi
        Map<String, Object> program = new HashMap<>();
        program.put("title", title);
        program.put("program", selectedProgram);
        program.put("subProgram", selectedSubProgram);
        program.put("url", url);
        program.put("description", description);

        // Update data program di Firestore
        CollectionReference programsRef = db.collection("programs");
        programsRef.document(programId)
                .update(program)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Program berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Saat item dipilih pada spinner program
        selectedProgram = parent.getItemAtPosition(position).toString();

        if (selectedProgram.equals("Program 1 - Daily Routine")) {
            // Jika Program 1 dipilih, tampilkan spinner Sub Program dan sesuaikan adapter-nya
            spinnerSubProgram.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> subProgramAdapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.sub_programs_array,
                    android.R.layout.simple_spinner_item
            );
            subProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSubProgram.setAdapter(subProgramAdapter);
        } else {
            // Jika Program selain Program 1 dipilih, sembunyikan spinner Sub Program
            spinnerSubProgram.setVisibility(View.GONE);
            selectedSubProgram = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Metode ini perlu diimplementasikan karena kelas ini mengimplementasikan interface AdapterView.OnItemSelectedListener
    }


}

