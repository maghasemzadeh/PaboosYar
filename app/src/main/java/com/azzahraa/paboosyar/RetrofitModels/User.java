package com.azzahraa.paboosyar.RetrofitModels;

import com.google.gson.annotations.SerializedName;

public class User {
    private String username;
    private String password;

    private String name;
    private String mobile;
    private String gender;
    @SerializedName("father_name")
    private String fatherName;
    private String train;
    private String wagon;
    private String coupe;

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getTrain() {
        return train;
    }

    public String getWagon() {
        return wagon;
    }

    public String getCoupe() {
        return coupe;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
