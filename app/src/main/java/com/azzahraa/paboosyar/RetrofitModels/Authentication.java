package com.azzahraa.paboosyar.RetrofitModels;

import com.google.gson.annotations.SerializedName;

public class Authentication {

    @SerializedName("auth_token")
    String token;

    public Authentication(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
