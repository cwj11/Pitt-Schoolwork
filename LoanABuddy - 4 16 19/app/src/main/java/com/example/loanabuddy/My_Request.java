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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class My_Request extends AppCompatActivity {

    private String document;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__request);
        document = getIntent().getStringExtra("com.example.loanabuddy.MY");
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
                        if(doc.get("reject").equals("1"))
                        {
                            message.append("This request was rejected.\n");
                        }
                        message.append("This is a request to " + doc.get("typeOf") + " $"+ doc.get("dollars") + "." + doc.get("cents") + ".\n");
                        message.append("This was delivered to " + doc.get("receiver") + ".\n");
                        if(doc.get("reject").equals("0")) {
                            message.append("If this request is accepted, the borrower of this transaction will automatically pay the loaner back on  " + doc.get("date") + ".\n");
                        }
                        message.append("Message: " + doc.get("msg") + "\n");

                        TextView textView = findViewById(R.id.Details);
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



    public void deleteDoc(View view){
        db = FirebaseFirestore.getInstance();
        db.collection("LOANREQUESTS").document(document).delete();
        startActivity(new Intent(My_Request.this, Transactions.class));
        this.finish();
    }

    public void returnTrans(View view)
    {
        startActivity(new Intent(My_Request.this, Transactions.class));
        this.finish();
    }

}
