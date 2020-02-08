package com.azzahraa.paboosyar.RetrofitModels;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NetworkAPIService {

    String MEN_FOOD = "dining/program/22/receipt/";
    String GRADUATED_FOOD = "dining/program/23/receipt/";
    String MEN_FOOD_HISTORY = "dining/program/22/";
    String GRADUATED_FOOD_HISTORY = "dining/program/23/";
    String ENTITY = "account/details/";
    String BLANKET = "blanket/";
    String BLANKET_HISTORY = "blanket/history/";
    String SOME_THING = "something/";
    String SOME_THING_HISTORY = "something/history/";


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
