package com.example.summerdrawer;

import java.io.Serializable;

public class Contents implements Serializable {
    private String id;
    private String title;
    private String category;
    private String author;
    private String date;
    private String summary;
    private String introduction;
    private String story;
    private String tag;
    private String img1; // 썸네일 이미지
    private String img2; // top 이미지
    private String linkName;
    private String link;

    Contents(String id, String title, String category, String author, String date,
             String summary, String introduction, String story, String tag,
             String img1, String img2, String link, String linkName){
        this.id = id;
        this.title = title;
        this.category = category;
        this.author = author;
        this.date = date;
        this.summary = summary;
        this.introduction = introduction;
        this.story = story;
        this.tag = tag;
        this.img1 = img1;
        this.img2 = img2;
        this.link = link;
        this.linkName = linkName;
    }

    public String getId() { return id;}
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getImg1() { return img1; }
    public String getImg2() { return img2; }
    public String getLinkName() { return linkName; }
    public String getLink() { return  link; }

}
