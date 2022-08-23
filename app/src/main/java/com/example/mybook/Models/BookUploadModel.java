package com.example.mybook.Models;

import java.util.ArrayList;
import java.util.List;

public class BookUploadModel {
    String UserID,Book_Name,pdfDownloadlink,BookId;
    String Time;
    int likes;
    String bookDBKey;

    public BookUploadModel(String userID, String book_Name, String pdfDownloadlink, String bookId, String time) {
        UserID = userID;
        Book_Name = book_Name;
        this.pdfDownloadlink = pdfDownloadlink;
        BookId = bookId;
        Time = time;
    }

    public BookUploadModel(){

    }

    public String getBookDBKey() {
        return bookDBKey;
    }

    public void setBookDBKey(String bookDBKey) {
        this.bookDBKey = bookDBKey;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBook_Name() {
        return Book_Name;
    }

    public void setBook_Name(String book_Name) {
        Book_Name = book_Name;
    }

    public String getPdfDownloadlink() {
        return pdfDownloadlink;
    }

    public void setPdfDownloadlink(String pdfDownloadlink) {
        this.pdfDownloadlink = pdfDownloadlink;
    }
}
