package com.example.loanabuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

public class Create_Loan extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private boolean userAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__loan);



        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.loanborrow_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String selection = parent.getItemAtPosition(pos).toString();
        if(selection.equals("BORROW"))
        {
            Intent intent = new Intent(this, Create_Borrow.class);
            startActivity(intent);
            this.finish();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId())
        {
            case R.id.allUsers:
                if(checked)
                {
                    userAll = true;
                }
                break;

            case R.id.userRadio:
                if(checked)
                {
                    userAll = false;
                }
                break;
        }
    }


    public void createLoan(View view)
    {

        Intent intent = new Intent(this, Verify_Request.class);

        ArrayList<String> info = new ArrayList<>();
        info.add("loan");

        EditText dollars = findViewById(R.id.dollars);
        if(dollars.getText().toString().length() > 0)
        {
            info.add(dollars.getText().toString());
        }
        else
        {
            info.add("0");
        }

        EditText cents = findViewById(R.id.cents);
        if(cents.getText().toString().length() == 2)
        {
            info.add(cents.getText().toString());
        }
        else if(cents.getText().toString().length() == 1)
        {
            info.add("0" + cents.getText().toString());
        }
        else
        {
            info.add("00");
        }

        if(userAll)
        {
            info.add("ALL");
        }
        else
        {
            EditText user = findViewById(R.id.userEdit);
            info.add(user.getText().toString());
        }

        EditText days = findViewById(R.id.daysEdit);
        info.add(days.getText().toString());
        EditText msg = findViewById(R.id.msgEdit);
        if(msg.getText().toString().length() > 0)
        {
            info.add(msg.getText().toString());
        }

        intent.putStringArrayListExtra("com.example.loanabuddy.REQUEST", info);
        startActivity(intent);
        this.finish();
    }
}
