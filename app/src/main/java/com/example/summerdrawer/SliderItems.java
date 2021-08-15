package com.example.summerdrawer;

public class SliderItems {
    private int image;
    private String title;
    private String category;
    private String author;
    private String desc;
    private String tag;

    SliderItems (int image, String title, String category, String author, String desc, String tag) {
        this.image = image;
        this.title = title;
        this.category = category;
        this.author = author;
        this.desc = desc;
        this.tag = tag;
    }

    public int getImage() {
        return image;
    }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getAuthor() { return author; }
    public String getDesc() { return desc; }
    public String getTag() { return tag;}
}