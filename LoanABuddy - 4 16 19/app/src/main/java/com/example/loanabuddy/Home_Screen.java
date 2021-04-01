package com.example.loanabuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Home_Screen extends AppCompatActivity {

    private FirebaseUser user;
    private static final String TAG = "HOME_SCREEN";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static StringBuilder message = null;
    private DocumentSnapshot document;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__screen);

        user = FirebaseAuth.getInstance().getCurrentUser();


        newUser();

        generate_Home();


    }

    public void newUser()
    {


        if(user != null) {
            String uid = user.getUid();
            Map<String, Object> data = new HashMap<>();


            db.collection("USERS").document(uid).set(data, SetOptions.merge());

            DocumentReference values = db.collection("USERS").document(uid);
            values.update("balance_dollars", FieldValue.increment(0));
            values.update("balance_cents", FieldValue.increment(0));
            values.update("loan_count", FieldValue.increment(0));
            values.update("loan_dollars", FieldValue.increment(0));
            values.update("loan_cents", FieldValue.increment(0));
            values.update("borrow_count", FieldValue.increment(0));
            values.update("borrow_amount", FieldValue.increment(0));
            values.update("borrow_dollars", FieldValue.increment(0));
            values.update("borrow_cents", FieldValue.increment(0));
            values.update("my_requests", FieldValue.increment(0));
            values.update("requests", FieldValue.increment(0));
        }
    }

    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Home_Screen.this,LoginActivity.class));
    }



    public void generate_Home()
    {
        String uid = user.getUid();

        if(user != null)
        {
            DocumentReference values = db.collection("USERS").document(uid);
            values.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        if (document.exists())
                        {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            message = new StringBuilder ();
                            message.append("Welcome " + user.getEmail() + "\n");

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

                            message.append(document.get("loan_count").toString() + " Loans made: $");
                            message.append(document.get("loan_dollars").toString());
                            cents = Integer.parseInt(document.get("loan_cents").toString());

                            if(cents < 10)
                            {
                                message.append(".0" + cents + "\n");
                            }
                            else
                            {
                                message.append("." + cents + "\n");
                            }

                            message.append(document.get("borrow_count").toString() + " Loans borrowed: $");
                            message.append(document.get("borrow_dollars").toString());
                            cents = Integer.parseInt(document.get("borrow_cents").toString());

                            if(cents < 10)
                            {
                                message.append(".0" + cents + "\n");
                            }
                            else
                            {
                                message.append("." + cents + "\n");
                            }

                            message.append("Sent " + document.get("my_requests") + " requests\n");
                            message.append("Received " + document.get("requests") + " requests\n");

                            TextView textView = findViewById(R.id.textView);
                            textView.setText(message.toString());



                        } else {
                           // Log.d(TAG, "No such document");
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    public void goToCreate(View view)
    {
        Intent intent = new Intent(this, Create_Request.class);
        startActivity(intent);

    }

    public void goToPaySettings(View view)
    {
        startActivity(new Intent(Home_Screen.this, Pay_Settings.class));

    }

    public void goToTransactions(View view)
    {
        startActivity(new Intent(Home_Screen.this,Transactions.class));

    }

}
