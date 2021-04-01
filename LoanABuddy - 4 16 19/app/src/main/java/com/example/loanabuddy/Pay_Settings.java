package com.example.loanabuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Pay_Settings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_settings);
    }

    public void goToAddCard(View view){
        Intent intent = new Intent(Pay_Settings.this, Add_Credit_Card.class);
        startActivity(intent);
        Pay_Settings.this.finish();

    }

    public void goToPayout(View view){
        Intent intent = new Intent(Pay_Settings.this, Payout.class);
        startActivity(intent);
        Pay_Settings.this.finish();

    }

    public void goToPersonalInfo(View view){
        Intent intent = new Intent(Pay_Settings.this, Personal_Info.class);
        startActivity(intent);
        Pay_Settings.this.finish();
    }




}
