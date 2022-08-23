package com.example.mybook.Models;

import java.security.SecureRandom;

public class DataModel {

    String title;
    String description;
    String url;
    String urlToImage;
    String PublishedAt;
    String IconPic;

    public DataModel(String title, String description, String url, String urlToImage, String publishedAt) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.PublishedAt = publishedAt;
    }

    public DataModel(String title,String description,String IconPic, String urlToImage,String publishedAt,String url){
        this.title = title;
        this.description = description;
        this.PublishedAt = publishedAt;
        this.url = url;
        this.urlToImage = urlToImage;
        this.IconPic = IconPic;
    }

    public DataModel(){}

    public String getIconPic() {
        return IconPic;
    }

    public void setIconPic(String iconPic) {
        IconPic = iconPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        PublishedAt = publishedAt;
    }
}
