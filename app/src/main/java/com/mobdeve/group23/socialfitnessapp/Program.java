package com.mobdeve.group23.socialfitnessapp;

import java.util.ArrayList;

public class Program {

    private String name;
    private String type;
    private String description;
    private String dateTime;
    private String link;
    private int photo; //not sure yet
    private String photoURL;

    public Program(String name, String type, String description, String dateTime, String link, int photo) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.dateTime = dateTime;
        this.link = link;
        this.photo = photo;
    }

    public Program() {
        this.name = getName();
        this.type = getType();
        this.description = getDescription();
        this.dateTime = getDateTime();
        this.link = getLink();
        this.photo = getPhoto();
        this.photoURL = getPhotoURL();
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

}