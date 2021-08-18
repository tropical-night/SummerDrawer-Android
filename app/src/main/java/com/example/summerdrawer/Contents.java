package com.example.summerdrawer;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Contents implements Serializable {
    private String title;
    private String category;
    private String author;
    private Timestamp date;
    private String summary;
    private String introduction;
    private String story;
    private String tag;
    private double rating;
    private int like;
    private int scrap;

    Contents(String title, String category, String author,
             String summary, String introduction, String story, String tag,
             double rating, int like, int scrap){
        this.title = title;
        this.category = category;
        this.author = author;
        //this.date = date;
        this.summary = summary;
        this.introduction = introduction;
        this.story = story;
        this.tag = tag;
        this.rating = rating;
        this.like = like;
        this.scrap = scrap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategoryAuthor(){
        return category+" | "+author;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLike() {
        return like+"";
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getScrap() {
        return scrap+"";
    }

    public void setScrap(int scrap) {
        this.scrap = scrap;
    }
}
