package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class TeacherRegistrationPostApi {
    @SerializedName("username")
    private String username;

    private String password;
    private String password2;
    private String first_name;
    private String Last_name;
    private String Email;
    private String token;
    private String notify;

    public TeacherRegistrationPostApi(String username, String first_name, String last_name, String email, String password, String password2) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.first_name = first_name;
        Last_name = last_name;
        Email = email;
    }

    public String getResponse() {
        return notify;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public String getEmail() {
        return Email;
    }

    public String getToken() {
        return token;
    }
}
