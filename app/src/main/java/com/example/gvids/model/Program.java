package com.example.gvids.model;

public class Program {
    private String programId;
    private String title;
    private String description;
    private String program;
    private String subProgram;
    private String thumbnailUrl;
    private String url;

    public Program() {
        // Diperlukan konstruktor kosong untuk deserialisasi Firestore
    }

    public Program(String programId, String title, String description,
                   String program, String subProgram, String thumbnailUrl, String url) {
        this.programId = programId;
        this.title = title;
        this.description = description;
        this.program = program;
        this.subProgram = subProgram;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
    }

    public String getProgramId() {
        return programId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getProgram() {
        return program;
    }

    public String getSubProgram() {
        return subProgram;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }
}
