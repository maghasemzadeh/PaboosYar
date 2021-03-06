package com.azzahraa.paboosyar.RetrofitModels;

public class Response {

    public boolean ok;
    public String message;
    public String program;
    public Meal meal;
    public User user;

    public User getUser() {
        return user;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Response(boolean ok, String message, String program, Meal meal, User user) {
        this.ok = ok;
        this.message = message;
        this.program = program;
        this.meal = meal;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
