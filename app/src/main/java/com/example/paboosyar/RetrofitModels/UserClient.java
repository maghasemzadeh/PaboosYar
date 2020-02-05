package com.example.paboosyar.RetrofitModels;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("auth/token/login")
    Call<String> getUser(@Body User user);
}
