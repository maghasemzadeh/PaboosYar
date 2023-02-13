package com.azzahraa.paboosyar.RetrofitModels;

public class Username {
    String username;
    int amount;

    public String getUsername() {
        return username;
    }
    public int getAmount(){return amount;}

    public void setUsername(String username) {
        this.username = username;
    }
    public void setAmount(int amount){this.amount = amount;}

    public Username(String username, int amount) {
        this.username = username;
        this.amount = amount;
    }
}
