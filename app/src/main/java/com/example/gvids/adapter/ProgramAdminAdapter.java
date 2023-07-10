package com.example.gvids.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gvids.R;
import com.example.gvids.admin.CreateProgramActivity;

import com.example.gvids.admin.VideoShowActivity;
import com.example.gvids.model.Program;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramAdminAdapter extends RecyclerView.Adapter<ProgramAdminAdapter.ViewHolder> {
    private Context context;
    private List<Program> programList;

    public ProgramAdminAdapter(Context context, List<Program> programList) {
        this.context = context;
        this.programList = programList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Program program = programList.get(position);

        String videoId = program.getUrl().substring(program.getUrl().lastIndexOf("/") + 1);
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
        holder.textViewVideoTitle.setText(program.getTitle());
        holder.textViewVideoDescription.setText(program.getDescription());
        holder.textViewProgram.setText(program.getProgram());
        holder.textViewSubProgram.setText(program.getSubProgram());
        Glide.with(context).load(thumbnailUrl).into(holder.imageViewThumbnail);

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Edit diklik
                Intent intent = new Intent(context, CreateProgramActivity.class);
                intent.putExtra("programId", program.getProgramId());
                intent.putExtra("title", program.getTitle());
                intent.putExtra("description", program.getDescription());
                intent.putExtra("url", program.getUrl());
                intent.putExtra("program", program.getProgram());
                intent.putExtra("subProgram", program.getSubProgram());
                context.startActivity(intent);

            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Hapus diklik
                // Lakukan tindakan yang sesuai, seperti menghapus program dari database
                // Misalnya, panggil method deleteProgram(program.getProgramId())
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Program program = programList.get(adapterPosition);
                    deleteProgram(program.getProgramId(), adapterPosition);
                }
            }
        });

        holder.buttonViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol Lihat Video diklik
                Intent intent = new Intent(context, VideoShowActivity.class);
                intent.putExtra("url", videoId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewVideoTitle, textViewVideoDescription, textViewProgram, textViewSubProgram;
        Button buttonEdit, buttonDelete, buttonViewVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewVideoTitle = itemView.findViewById(R.id.textViewVideoTitle);
            textViewVideoDescription = itemView.findViewById(R.id.textViewVideoDescription);
            textViewProgram = itemView.findViewById(R.id.textViewProgram);
            textViewSubProgram = itemView.findViewById(R.id.textViewSubProgram);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonViewVideo = itemView.findViewById(R.id.buttonViewVideo);
        }
    }

    private void deleteProgram(String programId, int poss) {
        // Dapatkan referensi ke koleksi "programs" dalam Firestore
        CollectionReference programsRef = FirebaseFirestore.getInstance().collection("programs");

        // Hapus dokumen program dengan ID yang diberikan
        programsRef.document(programId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Hapus berhasil
                        // Tambahkan tindakan yang sesuai, seperti menampilkan pesan sukses
                        programList.remove(poss);
                        notifyItemRemoved(poss);
                        notifyItemRangeChanged(poss, programList.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Hapus gagal
                        // Tambahkan tindakan yang sesuai, seperti menampilkan pesan error
                    }
                });
    }

}
