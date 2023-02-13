package com.azzahraa.paboosyar.RetrofitModels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NetworkAPIService {

    String FOOD = "dining/receipt/";
    String FOOD_HISTORY = "dining/history/";
    String PACK = "pack/receive_pack/";
    String PACK_HISTORY = "pack/history/";
    String ENTITY = "account/details/";
    String BLANKET = "blanket/";
    String BLANKET_HISTORY = "blanket/history/";
    String BOOK = "book/";
    String BOOK_HISTORY = "book/history/";
    String OPEN_PROGRAMS = "program/open/";
    String PAYMENT = "account/details/";
    String SHOHADA = "cultural/shohada/";
    String SHOHADA_HISTORY = "cultural/shohada/history/";
    String JANBAZ = "cultural/janbaz/";
    String JANBAZ_HISTORY = "cultural/janbaz/history/";
    String MALOOL = "cultural/malool/";
    String MALOOL_HISTORY = "cultural/malool/history/";
    String POOL = "entertainment/pool/";
    String POOL_HISTORY = "entertainment/pool/history/";
    String PAINTBALL = "entertainment/paintball/";
    String PAINTBALL_HISTORY = "entertainment/paintball/history/";
    String BOWLING = "entertainment/bowling/";
    String BOWLING_HISTORY = "entertainment/bowling/history/";
    String FOOTSAL = "entertainment/footsal/";
    String FOOTSAL_HISTORY = "entertainment/footsal/history/";


    String ONLINE = "https://account.azzahraa.ir/api/";
//    String ONLINE = "http://172.27.165.116:8000";
    String LOCAL = "https://localhost:8080";

    @POST("auth/token/login")
    Call<Authentication> getToken(@Body User user);

    @POST()
    Call<Response> getResponse(@Body Username username,
                               @Header("Authorization") String authorization,
                               @Url String url,
                               @Query("Program-Id") int programID);

    @GET
    Call<Response> getHistory(@Header("Authorization") String authorization,
                              @Url String url,
                              @Query("Program-Id") int programID);
}
