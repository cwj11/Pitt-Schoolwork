package com.example.loanabuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class View_Request extends AppCompatActivity {

    private String document;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__request);
        document = getIntent().getStringExtra("com.example.loanabuddy.Req");
        details();
    }

    private void details()
    {
        db = FirebaseFirestore.getInstance();
        DocumentReference values = db.collection("LOANREQUESTS").document(document);
        values.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc;
                    doc = task.getResult();
                    if (doc.exists())
                    {
                        StringBuilder message = new StringBuilder();

                        message.append("This is a request to " + doc.get("typeOf") + " $"+ doc.get("dollars") + "." + doc.get("cents") + ".\n");
                        message.append("This was delivered from " + doc.get("sender") + ".\n");
                        message.append("If this request is accepted, the borrower of this transaction will automatically pay the loaner back on  " + doc.get("date") + ".\n");
                        message.append("Message: " + doc.get("msg") + "\n");

                        TextView textView = findViewById(R.id.details);
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

    public void deny(View view){
        db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("LOANREQUESTS").document(document);

        doc.update("reject", "1");
        startActivity(new Intent(View_Request.this, Transactions.class));
        finish();
    }

    public void accept(View view){
        db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("LOANREQUESTS").document(document);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snap = task.getResult();
                    if (snap.exists()){
                        Map<String, Object> data = snap.getData();
                        db.collection("LOANS").document().set(data);
                        doc.delete();

                    }
                }
            }
        });
        startActivity(new Intent(View_Request.this, Transactions.class));
        finish();
    }
}
