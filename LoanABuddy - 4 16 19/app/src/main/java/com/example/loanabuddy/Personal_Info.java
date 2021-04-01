package com.example.loanabuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Personal_Info extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
    }


    public void submit(View view){

        startActivity(new Intent(Personal_Info.this, Pay_Settings.class));
        Personal_Info.this.finish();

    }


    public void cancel(View view){

        startActivity(new Intent(Personal_Info.this, Pay_Settings.class));
        Personal_Info.this.finish();


    }


}