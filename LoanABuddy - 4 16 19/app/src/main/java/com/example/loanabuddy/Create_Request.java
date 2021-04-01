package com.example.loanabuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Create_Request extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__request);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.loanborrow_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        String selection = parent.getItemAtPosition(pos).toString();
        if(selection.equals("LOAN"))
        {
            Intent intent = new Intent(this, Create_Loan.class);
            startActivity(intent);
            this.finish();
        }
        else if(selection.equals("BORROW"))
        {
            Intent intent = new Intent(this, Create_Borrow.class);
            startActivity(intent);
            this.finish();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
