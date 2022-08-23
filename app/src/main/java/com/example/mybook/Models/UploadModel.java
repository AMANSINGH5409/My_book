package com.example.mybook.Models;

import java.util.Date;

public class UploadModel {
    String userName,postTitle,postDescription,photo,time;

    public UploadModel(String userName, String postTitle, String postDescription, String photo, String time) {
        this.userName = userName;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.photo = photo;
        this.time = time;
    }

    public UploadModel(String userName, String postTitle, String postDescription,String time) {
        this.userName = userName;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.time = time;
    }

    public UploadModel(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
