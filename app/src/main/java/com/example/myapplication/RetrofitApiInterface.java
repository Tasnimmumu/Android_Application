package com.example.myapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApiInterface {

    @Multipart
    @POST("registers")
    Call<registrationpostapi> createPost(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("password2") RequestBody confirmpassword,
            @Part("first_name") RequestBody firstname,
            @Part("Last_name") RequestBody lastname,
            @Part("Email") RequestBody email,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("registert")
    Call<TeacherRegistrationPostApi> TeacherPostRegister(
            @Field("username") String username,
            @Field("first_name") String firstname,
            @Field("Last_name") String lastname,
            @Field("Email") String email,
            @Field("password") String password,
            @Field("password2") String confirmpassword
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginPostApi> loginPost(
            @Field("username") String username,
            @Field("password") String password
    );
}
