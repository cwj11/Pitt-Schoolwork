package com.example.loanabuddy;

import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Calendar;

public class Verify_Request extends AppCompatActivity {

    private ArrayList<String> info;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__request);
        String typeOf = null;
        String amount = null;
        String receivUser = null;
        String days = null;
        String msg = null;
        user = FirebaseAuth.getInstance().getCurrentUser();



        info = new ArrayList<>(getIntent().getStringArrayListExtra("com.example.loanabuddy.REQUEST"));



        if(info.get(0).equals("loan"))
        {
            typeOf = "You are submitting a request to loan ";
        }
        else if(info.get(0).equals("borrow"))
        {
            typeOf = "You are submitting a request to borrow ";
        }

        amount = "$" + info.get(1) + "." + info.get(2) + "\n";

        if(info.get(3).equals("ALL"))
        {
            receivUser = "This request will be broadcast to all users.\n";
        }
        else
        {
            receivUser = "This request will be sent to " + info.get(3) + ".\n";
        }


        days = "If this request is accepted, the borrower of this transaction will automatically pay the loaner back in  " + info.get(4) + " day(s).\n";

        if(info.size() < 6)
        {
            msg = "No message has been attached.\n";
        }
        else
        {
            msg = "Message: " + info.get(5) + "\n";
        }

        TextView textView = findViewById(R.id.textView3);
        textView.setText(typeOf + amount + receivUser + days + msg);

    }

    public void submitRetHome(View view)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, Integer.parseInt(info.get(4)));
        //Here is where submit button logic goes, hoes
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("typeOf", info.get(0));
        data.put("dollars", info.get(1));
        data.put("cents", info.get(2));

        data.put("date", calendar.getTime().toString());

        if(info.size() < 6)
        {
            data.put("msg", "No message has been attached.");
        }
        else
        {
            data.put("msg", info.get(5));
        }


        data.put("sender", user.getEmail());
        data.put("receiver", info.get(3));
        data.put("reject", "0");

        db.collection("LOANREQUESTS").document().set(data);

        startActivity(new Intent(Verify_Request.this,Home_Screen.class));
        finish();

    }


}
