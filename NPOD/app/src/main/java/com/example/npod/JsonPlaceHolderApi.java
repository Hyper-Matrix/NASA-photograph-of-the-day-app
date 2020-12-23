package com.example.npod;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("planetary/apod")  //Relative url
    Call<Post> getPosts(@Query("api_key") String key);      //Pass query for initial parameter or initial API without date

}
