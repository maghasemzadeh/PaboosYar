package com.example.paboosyar.RetrofitModels;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface retrofitHandler {


    @POST("auth/token/login")
    Call<Authentication> getToken(@Body User user);

    @POST("dining/program/22/receipt/")
    Call<Response> getMenDinner(@Body Username username, @Header("Authorization") String authorization);


}
