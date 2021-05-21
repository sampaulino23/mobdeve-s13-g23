package com.mobdeve.group23.socialfitnessapp;

import java.util.ArrayList;

public class Program {

    private String name;
    private String type;
    private String description;
    private String date;
    private String time;
    private String link;
    private int photo; //not sure yet


    public Program(String name, String type, String description, String date, String time, String link, int photo) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.date = date;
        this.time = time;
        this.link = link;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
