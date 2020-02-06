package com.example.paboosyar.RetrofitModels;

public class Meal {
    public String title;
    public String food;
    public int total;
    public int receipt_count;

    public Meal(String title, String food, int total, int receipt_count) {
        this.title = title;
        this.food = food;
        this.total = total;
        this.receipt_count = receipt_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getReceipt_count() {
        return receipt_count;
    }

    public void setReceipt_count(int receipt_count) {
        this.receipt_count = receipt_count;
    }
}
