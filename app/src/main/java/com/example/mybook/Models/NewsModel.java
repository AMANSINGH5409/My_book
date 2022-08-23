package com.example.mybook.Models;

import java.util.ArrayList;

public class NewsModel {

    ArrayList<DataModel> articles;

    public NewsModel(ArrayList<DataModel> articles) {
        this.articles = articles;
    }

    public ArrayList<DataModel> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<DataModel> articles) {
        this.articles = articles;
    }
}
