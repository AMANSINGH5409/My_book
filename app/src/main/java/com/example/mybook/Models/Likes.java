package com.example.mybook.Models;

public class Likes {
    String likedBy;

    public Likes(String likedBy) {
        this.likedBy = likedBy;
    }

    public Likes(){}

    public String getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy;
    }
}
