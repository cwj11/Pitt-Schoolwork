package com.example.loanabuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.ExternalAccount;
import com.stripe.net.RequestOptions;
import com.stripe.model.Token;
import com.stripe.model.Card;

import java.util.HashMap;
import java.util.Map;

public class Payout extends AppCompatActivity {

    private FirebaseUser user;
    private static final String TAG = "PAYOUT";
    private DocumentSnapshot document;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        user = FirebaseAuth.getInstance().getCurrentUser();


        generate_Payout();
        Stripe.apiKey = "sk_test_0EWfrNALXvb6Ie6shSQnqR0X00WBLUm7ye";
    }

    public void generate_Payout(){
        String uid = user.getUid();

        if(user != null) {
            DocumentReference values = db.collection("USERS").document(uid);
            values.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            StringBuilder message = new StringBuilder ();
                            message.append("Balance: $" + document.get("balance_dollars").toString());
                            int cents = Integer.parseInt(document.get("balance_cents").toString());
                            if(cents < 10)
                            {
                                message.append(".0" + cents + "\n");
                            }
                            else
                            {
                                message.append("." + cents + "\n");
                            }
                            TextView textView = findViewById(R.id.textView8);
                            textView.setText(message.toString());
                        }
                    }
                }
            });
        }
    }

    public void payoutMoney(View view){
        String uid = user.getUid();

        if(user != null) {
            DocumentReference values = db.collection("USERS").document(uid);
            values.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    String acct = document.get("account").toString();
                                    Account account = null;
                                    try {
                                        account = Account.retrieve(acct);
                                    } catch (StripeException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Map<String, Object> par = new HashMap<>();
                                    Map<String, Object> profile = new HashMap<>();

                                    par.put("individual", profile);
                                    profile.put("first_name", "Connor");
                                    profile.put("last_name", "Johnson");

                                    try {
                                        account.update(par);
                                    } catch (StripeException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
            });
        }
            startActivity(new Intent(Payout.this, Home_Screen.class));
            Payout.this.finish();

    }

}
