package com.azzahraa.paboosyar.RetrofitModels;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NetworkAPIService {

    String SADAT_FOOD = "dining/receipt/sadat/";
    String SHOHADA_FOOD = "dining/receipt/shohada/";
    String SADAT_FOOD_HISTORY = "dining/history/sadat/";
    String SHOHADA_FOOD_HISTORY = "dining/history/shohada/";
    String ENTITY = "account/details/";
    String BLANKET = "blanket/";
    String BLANKET_HISTORY = "blanket/history/";
    String BOOK = "book/";
    String BOOK_HISTORY = "book/history/";


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
