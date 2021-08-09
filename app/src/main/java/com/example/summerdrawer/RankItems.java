package com.example.summerdrawer;

public class RankItems {
    private int image;
    private String title;
    private String category;
    private String author;
    private String desc;
    private int heart; // 하트
    private int scarp; // 스크랩

    RankItems (int image, String title, String category, String author, String desc) {
        this.image = image;
        this.title = title;
        this.category = category;
        this.author = author;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getAuthor() { return author; }
    public String getDesc() { return desc; }
}
