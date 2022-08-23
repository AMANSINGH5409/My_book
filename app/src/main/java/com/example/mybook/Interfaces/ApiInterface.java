package com.example.mybook.Interfaces;

import com.example.mybook.Models.DataModel;
import com.example.mybook.Models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    String BASE_URL = "https://newsapi.org/v2/";

    @GET("everything")
    Call<NewsModel>getNewsDate(
            @Query("q") String q,
            @Query("apikey") String apikey
    );

}
