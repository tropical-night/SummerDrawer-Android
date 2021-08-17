package com.example.summerdrawer;

import java.util.ArrayList;

public class SliderItems {
    private final String image;
    private final String title;
    private final String category;
    private final String author;
    private final String desc;
    private final String tag;

    SliderItems (String image, String title, String category, String author, String desc, String tag) {
        this.image = image;
        this.title = title;
        this.category = category;
        this.author = author;
        this.desc = desc;
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getAuthor() { return author; }
    public String getDesc() { return desc; }
    public String getTag() { return tag;}
}