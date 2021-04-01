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

public class View_Loan extends AppCompatActivity {

    private String document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__loan);
        document = getIntent().getStringExtra("com.example.loanabuddy.Loan");
        details();
    }

    private void details()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference values = db.collection("LOANS").document(document);
        values.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc;
                    doc = task.getResult();
                    if (doc.exists())
                    {
                        StringBuilder message = new StringBuilder();

                        if(doc.get("typeOf").equals("borrow"))
                        {
                            message.append("Loaner" + doc.get("receiver") + "\n");
                            message.append("Borrower: " + doc.get("sender") + "\n");
                        }
                        else
                        {
                            message.append("Loaner" + doc.get("sender") + "\n");
                            message.append("Borrower: " + doc.get("receiver") + "\n");
                        }

                        message.append("Amount: " + "$" + doc.get("dollars") + "." + doc.get("cents") + "\n");
                        message.append("Payback: " + doc.get("date") + "\n");

                        TextView textView = findViewById(R.id.details2);
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

    public void retTrans(View view)
    {
        startActivity(new Intent(View_Loan.this, Transactions.class));
        this.finish();
    }
}
