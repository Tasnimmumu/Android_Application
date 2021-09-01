package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import  retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class LoginPostApi {


    @SerializedName("username")
    private String username;
    private String password;
    private String token;


    private LoginPostApi() {
        this.username = username;
        this.password = password;

    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

}
