package com.example.gvids.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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

import com.example.gvids.model.Program;
import com.example.gvids.user.DetailVideoActivity;

import java.util.List;

public class ProgramUserAdapter extends RecyclerView.Adapter<ProgramUserAdapter.ViewHolder> {
    private Context context;
    private List<Program> programList;

    public ProgramUserAdapter(Context context, List<Program> programList) {
        this.context = context;
        this.programList = programList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Program program = programList.get(position);

        String videoId = program.getUrl().substring(program.getUrl().lastIndexOf("/") + 1);
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
        holder.textViewVideoTitle.setText(program.getTitle());
        holder.textViewVideoDescription.setText(program.getDescription());
        Glide.with(context).load(thumbnailUrl).into(holder.imageViewThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Card diklik
                Intent intent = new Intent(context, DetailVideoActivity.class);
                intent.putExtra("programId", program.getProgramId());
                intent.putExtra("title", program.getTitle());
                intent.putExtra("description", program.getDescription());
                intent.putExtra("url", videoId);
                intent.putExtra("program", program.getProgram());
                intent.putExtra("subProgram", program.getSubProgram());
                context.startActivity(intent);
            }
        });

        // Show More dan Show Less untuk deskripsi video
        holder.textViewVideoDescription.setMaxLines(2);
        holder.textViewVideoDescription.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewVideoDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewVideoDescription.getMaxLines() == 2) {
                    // Jika saat ini dalam mode Show More, ubah ke mode Show Less
                    holder.textViewVideoDescription.setMaxLines(Integer.MAX_VALUE);
                    holder.textViewVideoDescription.setEllipsize(null);
                } else {
                    // Jika saat ini dalam mode Show Less, ubah ke mode Show More
                    holder.textViewVideoDescription.setMaxLines(2);
                    holder.textViewVideoDescription.setEllipsize(TextUtils.TruncateAt.END);
                }
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
        Button buttonViewVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewVideoTitle = itemView.findViewById(R.id.textViewVideoTitle);
            textViewVideoDescription = itemView.findViewById(R.id.textViewVideoDescription);
            textViewProgram = itemView.findViewById(R.id.textViewProgram);
            textViewSubProgram = itemView.findViewById(R.id.textViewSubProgram);
            buttonViewVideo = itemView.findViewById(R.id.buttonViewVideo);
        }
    }
}
