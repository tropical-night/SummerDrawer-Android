package com.example.summerdrawer;

import java.io.Serializable;

public class LikeScrap implements Serializable {
    private String id;
    private String category;
    private int like;
    private int scrap;

    LikeScrap(String id, String category, int like, int scrap){
        this.id = id;
        this.category = category;
        this.like = like;
        this.scrap = scrap;
    }

    public String getId() {
        return id;
    }
    public String getCategory() { return category; }
    public int getLike() { return like; }
    public int getScrap() { return scrap; }
}
