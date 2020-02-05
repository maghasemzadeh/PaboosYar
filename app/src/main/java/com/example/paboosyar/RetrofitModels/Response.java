package com.example.paboosyar.RetrofitModels;

public class Response {

    public String message;
    public boolean ok;

    public Response(String message, boolean ok) {
        this.message = message;
        this.ok = ok;
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
