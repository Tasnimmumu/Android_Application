package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeacherRegisterActivity extends AppCompatActivity {
    private Button button;
    private Toast error;
    private TextView textViewResult;
    private RetrofitApiInterface apiinterface;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_resigter);


        changeStatusBarColor();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiinterface = retrofit.create(RetrofitApiInterface.class);

        button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Get all text fields from registration page
                EditText user = (EditText) findViewById(R.id.editTextName);
                EditText em = (EditText) findViewById(R.id.editTextEmail);
                EditText first = (EditText) findViewById(R.id.editTextFirstName);
                EditText last = (EditText) findViewById(R.id.editTextLastName);
                EditText pass = (EditText) findViewById(R.id.editTextPassword);
                EditText confirmpass = (EditText) findViewById(R.id.editTextConfirmPassword);

                // convert everything to string
                String username = user.getText().toString();
                String email = em.getText().toString();
                String firstname = first.getText().toString();
                String lastname = last.getText().toString();
                String password = pass.getText().toString();
                String confirmpassword = confirmpass.getText().toString();

                boolean check = checkfields(username,email,firstname,lastname,password,confirmpassword);

                if (check) {
                    Log.i("success", "all info is correct");

                    createPost(username,firstname,lastname,email,password,confirmpassword);

                }
                else{
                    Log.i("Fail", "something is not right");
                }

                //Log.i("string", "name: " +username);
                //Log.i("string generate", "email: " +email);
            }
        });

    }

    @SuppressLint("ObsoleteSdkInt")
    public void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){

            Window window =getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //Window.setStatusBarColor (getResources().getColor(R.color.register_bk_color));

        }

    }

    public void onLoginClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private boolean checkfields(String username,String email,String firstname,String lastname,String password,String confirmpassword){

        if (username.matches("") || email.matches("") || firstname.matches("") || lastname.matches("") || password.matches("") || confirmpassword.matches("")){
            error = Toast.makeText(getApplicationContext(), "One or more fields are missing!", Toast.LENGTH_SHORT);
            error.setGravity(Gravity.CENTER, 0, 0);
            error.show();
            return false;
        }
        else if( !password.matches(confirmpassword) ){
            error = Toast.makeText(getApplicationContext(), "passwords doesn't match :(", Toast.LENGTH_SHORT);
            error.setGravity(Gravity.CENTER, 0, 0);
            error.show();
            return false;
        }

        else {
            error = Toast.makeText(getApplicationContext(), "If all info checks out, Please log in :)", Toast.LENGTH_SHORT);
            error.setGravity(Gravity.CENTER, 0, 0);
            error.show();
            return true;
        }

        //return false;
    }

    private void createPost(String username, String firstName, String lastName, String email, String password, String confirmPass){
        Call<TeacherRegistrationPostApi> call = apiinterface.TeacherPostRegister(username,firstName,lastName,email,password,confirmPass);
        call.enqueue(new Callback<TeacherRegistrationPostApi>() {
            @Override
            public void onResponse(Call<TeacherRegistrationPostApi> call, Response<TeacherRegistrationPostApi> response) {
                if(!response.isSuccessful()){
                    Log.i("in post", "is not successful");
                    error = Toast.makeText(getApplicationContext(), "Something went wrong, Try again!", Toast.LENGTH_SHORT);
                    error.setGravity(Gravity.CENTER, 0, 0);
                    error.show();
                    return;
                }
                TeacherRegistrationPostApi postResponse = response.body();
                String username = postResponse.getUsername();
                String token = postResponse.getToken();

                if (!username.matches("") && !token.matches("")){
                    try {
                        Thread.sleep(1000);
                        Intent intent = new Intent(TeacherRegisterActivity.this, com.example.myapplication.LoginActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<TeacherRegistrationPostApi> call, Throwable t) {
                error = Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT);
                error.setGravity(Gravity.CENTER, 0, 0);
                error.show();
                Log.i("Failed", "API request failed: ");

            }
        });
    }


}