package com.example.mybook.Models;

public class Notifications {

    String Token;

    public Notifications(String token) {
        Token = token;
    }
    public Notifications(){}

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
