package com.example.npod;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceHolderApi {
    @GET("planetary/apod")  //Relative URL
    Call<Post> getPosts(@Query("api_key") String key,   //Pass query for apikey
                            @Query("date") String date);    //Pass query for Date fro calendar
}
