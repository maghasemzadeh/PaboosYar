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


    String ONLINE = "http://account.azzahraa.ir/api/";
    String LOCAL = "http://localhost:8080";

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
