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

public class RegisterActivity extends AppCompatActivity {
    private Button button;
    private Toast error;
    private TextView textViewResult;
    private RetrofitApiInterface apiinterface;
    private Intent myFileIntent;

    //imageView
    private ImageView imageView;
    private Button btnSelect, btnUpload;
    private int IMG_REQUEST =21;
    private Bitmap bitmap;
    private String filepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//Srtimageview
        imageView = findViewById(R.id.imageView);
        btnSelect = findViewById(R.id.btnSelectImage);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("image/");
                startActivityForResult(myFileIntent,IMG_REQUEST);
            }
        });
        //endimageview

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
                    Log.i("file Path", filepath);

                    createPost(username,firstname,lastname,email,password,confirmpassword,filepath);

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

    private void createPost(String username, String firstname, String lastname, String email, String password, String ConfirmPassword,String filepath){


        File file = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("Image",file.getName(), requestFile);

        RequestBody RequestUserFName = RequestBody.create(MultipartBody.FORM, firstname);
        RequestBody RequestUserLName = RequestBody.create(MultipartBody.FORM, lastname);
        RequestBody RequestUserName = RequestBody.create(MultipartBody.FORM, username);
        RequestBody RequestUserEmail = RequestBody.create(MultipartBody.FORM, email);
        RequestBody RequestUserPass = RequestBody.create(MultipartBody.FORM, password);
        RequestBody RequestUserConfiremPass = RequestBody.create(MultipartBody.FORM, ConfirmPassword);

        Call<registrationpostapi> call = apiinterface.createPost(RequestUserName,RequestUserPass,RequestUserConfiremPass,RequestUserFName,RequestUserLName,RequestUserEmail,body);
        Log.i("in post", String.valueOf(RequestUserName));
        call.enqueue(new Callback<registrationpostapi>() {

            @Override
            public void onResponse(Call<registrationpostapi> call, Response<registrationpostapi> response) {
                if(!response.isSuccessful()){
                    Log.i("in post", "is not successful");
                    return;
                }
                registrationpostapi postResponse = response.body();

                //Log.i("in post", "registration body response");

                String responded = postResponse.getResponse();
                //Log.i("Response", responded);

                String username = postResponse.getUsername();
                String token = postResponse.getToken();

                if (!username.matches("") && !token.matches("")){
                    try {
                        Thread.sleep(2000);
                        Intent intent = new Intent(RegisterActivity.this, com.example.myapplication.LoginActivity.class);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                else{
                    error = Toast.makeText(getApplicationContext(), "something went wrong, please try again", Toast.LENGTH_SHORT);
                    error.setGravity(Gravity.CENTER, 0, 0);
                    error.show();
                }


            }

            @Override
            public void onFailure(Call<registrationpostapi> call, Throwable t) {
                Log.i("Failed", "API request failed: ");
            }
        });
    }

    //imageview


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 21:
                if(requestCode == IMG_REQUEST && data != null){

                    Uri path = data.getData();
                    filepath = getRealPathFromURI(path);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                }
                break;
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}