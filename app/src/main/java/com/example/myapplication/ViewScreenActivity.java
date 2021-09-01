package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;

public class ViewScreenActivity extends AppCompatActivity {

    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_screen);
        final int REQUEST_EXTERNAL_STORAGE = 1;
        if (ContextCompat.checkSelfPermission(ViewScreenActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        //isReadStoragePermissionGranted();
        //isWriteStoragePermissionGranted();

        button =(Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewScreenActivity.this, com.example.myapplication.OptionActivity.class);
                startActivity(intent);
            }
        });


    }


}


