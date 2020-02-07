package com.example.paboosyar.RetrofitModels;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NetworkAPIService {

    String MEN_FOOD = "dining/program/22/receipt/";
    String GRADUATED_FOOD = "dining/program/23/receipt/";
    String WOMEN_FOOD = "???";
    String MEN_FOOD_HISTORY = "dining/program/22/";
    String GRADUATED_FOOD_HISTORY = "dining/program/23/";
    String WOMEN_FOOD_HISTORY = "dining/program/24/";
    String ENTITY = "account/detail/";


    @POST("auth/token/login")
    Call<Authentication> getToken(@Body User user);

    @POST()
    Call<Response> getResponse(@Body Username username,
                               @Header("Authorization") String authorization,
                               @Url String url);

    @GET
    Call<Response> getHistory(@Header("Authorization") String authorization,
                              @Url String url);


}
