package com.example.gvids.user;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gvids.R;
import com.example.gvids.user.MasukProgramActivity;

public class Program1Fragment extends Fragment {

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private Button buttonJanuary, buttonFebruary, buttonMarch, buttonApril, buttonMay, buttonJune,
            buttonJuly, buttonAugust, buttonSeptember, buttonOctober, buttonNovember, buttonDecember;

    public Program1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program1, container, false);

        // Initialize views
        imageViewLogo = view.findViewById(R.id.imageViewLogo);
        textViewAppName = view.findViewById(R.id.textViewAppName);
        buttonJanuary = view.findViewById(R.id.buttonJanuary);
        buttonFebruary = view.findViewById(R.id.buttonFebruary);
        buttonMarch = view.findViewById(R.id.buttonMarch);
        buttonApril = view.findViewById(R.id.buttonApril);
        buttonMay = view.findViewById(R.id.buttonMay);
        buttonJune = view.findViewById(R.id.buttonJune);
        buttonJuly = view.findViewById(R.id.buttonJuly);
        buttonAugust = view.findViewById(R.id.buttonAugust);
        buttonSeptember = view.findViewById(R.id.buttonSeptember);
        buttonOctober = view.findViewById(R.id.buttonOctober);
        buttonNovember = view.findViewById(R.id.buttonNovember);
        buttonDecember = view.findViewById(R.id.buttonDecember);

        // Set click listeners for the buttons
        buttonJanuary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Januari");
            }
        });
        buttonFebruary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Februari");
            }
        });
        buttonMarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Maret");
            }
        });
        buttonApril.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("April");
            }
        });
        buttonMay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Mei");
            }
        });
        buttonJune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Juni");
            }
        });
        buttonJuly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Juli");
            }
        });
        buttonAugust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Agustus");
            }
        });
        buttonSeptember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("September");
            }
        });
        buttonOctober.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Oktober");
            }
        });
        buttonNovember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("November");
            }
        });
        buttonDecember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMasukProgramActivity("Desember");
            }
        });

        return view;
    }

    private void openMasukProgramActivity(String month) {
        Intent intent = new Intent(getActivity(), MasukProgramActivity.class);
        intent.putExtra("program", "Program 1 - Daily Routine");
        intent.putExtra("subProgram", month);
        startActivity(intent);
    }
}
