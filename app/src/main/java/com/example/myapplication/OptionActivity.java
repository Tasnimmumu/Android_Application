package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    private Button RegisterButton;
    private Button LoginButton;
    private static int Name = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        radioGroup = findViewById(R.id.radio);
        textView = findViewById(R.id.selected);
        RegisterButton = (Button)findViewById(R.id.registerButton);
        LoginButton = (Button)findViewById(R.id.LoginButton);

        //Button On click listeners
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Name == 1){
                    Intent intent = new Intent(OptionActivity.this, com.example.myapplication.RegisterActivity.class);
                    startActivity(intent);
                }
                else if(Name == 0){
                    Intent intent = new Intent(OptionActivity.this, com.example.myapplication.TeacherRegisterActivity.class);
                    startActivity(intent);                }
                else {
                    Toast.makeText(getApplicationContext(), "Not Selected yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, com.example.myapplication.LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void teacherbutton (View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
        Toast.makeText(this, " " + radioButton.getText(), Toast.LENGTH_SHORT).show();
        textView.setText("Teacher Selected");
        Name = 0;
    }


        public void studentbutton (View v)
        {
            int radioId = radioGroup.getCheckedRadioButtonId();

            radioButton = findViewById(radioId);
            Toast.makeText(this, " " + radioButton.getText(), Toast.LENGTH_SHORT).show();
            textView.setText("Student Selected");
            Name = 1;
        }


    }
