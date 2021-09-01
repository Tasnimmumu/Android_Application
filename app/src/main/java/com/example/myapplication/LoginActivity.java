package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private Toast error;
    private Toast info;
    private RetrofitApiInterface apiinterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//for changing status bar icon color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiinterface = retrofit.create(RetrofitApiInterface.class);

        button = (Button) findViewById(R.id.cirLoginButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Get all text fields from Login page
                EditText em = (EditText) findViewById(R.id.editTextUsername);
                EditText pass = (EditText) findViewById(R.id.editTextPassword);

                // convert everything to string
                String username = em.getText().toString();
                String password = pass.getText().toString();

                boolean check = checkfields(username, password);

                if (check) {
                    createPost(username, password);
                }

            }
        });

    }

    public void onLoginClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    private boolean checkfields(String username, String password) {

        if (username.matches("") || password.matches("")) {
            error = Toast.makeText(getApplicationContext(), "One or more fields are missing!", Toast.LENGTH_SHORT);
            error.setGravity(Gravity.CENTER, 0, 0);
            error.show();
            return false;
        } else {
            return true;
        }

        //return false;
    }

    private void createPost(String username, String password) {
        Call<LoginPostApi> call = apiinterface.loginPost(username, password);

        call.enqueue(new Callback<LoginPostApi>() {
            @Override
            public void onResponse(Call<LoginPostApi> call, Response<LoginPostApi> response) {
                if (!response.isSuccessful()) {
                    Log.i("in post", "is not successful");
                    error = Toast.makeText(getApplicationContext(), "Username or Password is incorrect", Toast.LENGTH_SHORT);
                    error.setGravity(Gravity.CENTER, 0, 0);
                    error.show();
                    return;
                }
                LoginPostApi postResponse = response.body();

                String token = postResponse.getToken();

                if (!token.matches("")) {
                    try {
                        Thread.sleep(2000);
                        Intent intent = new Intent(LoginActivity.this,  com.example.myapplication.NavigationActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginPostApi> call, Throwable t) {
                error = Toast.makeText(getApplicationContext(), "Username or Password is incorrect", Toast.LENGTH_SHORT);
                error.setGravity(Gravity.CENTER, 0, 0);
                error.show();
                Log.i("Failed", "API request failed: ");
            }
        });
    }
}