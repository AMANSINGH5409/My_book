package com.example.mybook.Models;

public class Users {
    String userName;
    String emailId;
    String password;
    String userId;
    String ProfilePic;
    String Description;
    String token;

    public Users(String userName, String emailId, String password, String userId,String ProfilePic) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
        this.userId = userId;
        this.ProfilePic = ProfilePic;
    }

    public Users(){ }

    //Sign Up Constractor
    public Users(String userName, String emailId, String password) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
    }

    public Users(String userName, String emailId, String password, String userId, String profilePic, String description) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
        this.userId = userId;
        ProfilePic = profilePic;
        Description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
