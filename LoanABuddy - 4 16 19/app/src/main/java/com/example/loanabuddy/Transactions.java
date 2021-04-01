package com.example.loanabuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Transactions extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private ArrayList<String> loanList = new ArrayList<String>();
    private ListView myView;
    private ListView reqView;
    private ListView loanView;
    private RequestListAdapter reqAdapter;
    private MyListAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myList();
        reqList();
        loaner();
        loanee();
    }

    public void myList()
    {

        db.collection("LOANREQUESTS")
                .whereEqualTo("sender", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<String> myList = new ArrayList<String>();
                        ArrayList<String> docList = new ArrayList<String>();
                        for (QueryDocumentSnapshot doc : value) {

                            if (doc.get("typeOf") != null) {
                                if(doc.get("dollars") != null && doc.get("cents") != null)
                                {
                                    myList.add(doc.getString("typeOf") + " $" + doc.get("dollars") + "."+ doc.get("cents"));
                                    docList.add(doc.getId());
                                }
                            }
                        }

                        myAdapter = new MyListAdapter(myList, docList, Transactions.this);

                        myView = (ListView)findViewById(R.id.myList);
                        myView.setAdapter(myAdapter);
                    }
                });



    }

    public void reqList()
    {
        db.collection("LOANREQUESTS")
                .whereEqualTo("receiver", user.getEmail()).whereEqualTo("reject", "0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<String> list = new ArrayList<String>();
                        ArrayList<String> docList = new ArrayList<String>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("sender") != null) {
                                if(doc.get("typeOf") != null && doc.get("cents") != null)
                                {
                                    list.add(doc.getString("sender") + ": " + doc.get("typeOf"));
                                    docList.add(doc.getId());
                                }

                            }
                        }

                        reqAdapter = new RequestListAdapter(list, docList, Transactions.this);


                        reqView = (ListView)findViewById(R.id.myList2);
                        reqView.setAdapter(reqAdapter);
                    }
                });

    }

    private void loaner()
    {
            db.collection("LOANS")
                .whereEqualTo("sender", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<String> senderList = new ArrayList<String>();
                        ArrayList<String> docList = new ArrayList<String>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("typeOf") != null) {
                                if(doc.get("dollars") != null && doc.get("cents") != null)
                                {
                                    senderList.add(doc.getString("typeOf") + " $" + doc.get("dollars") + "."+ doc.get("cents"));
                                    docList.add(doc.getId());
                                }
                            }
                        }


                        LoanListAdapter loanAdapter = new LoanListAdapter(senderList, docList, Transactions.this);

                        ListView lView = (ListView)findViewById(R.id.myList3);
                        lView.setAdapter(loanAdapter);
                    }
                });



    }

    private void loanee()
    {
        db.collection("LOANS")
                .whereEqualTo("receiver", user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<String> receiverList = new ArrayList<String>();
                        ArrayList<String> docList = new ArrayList<String>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("typeOf") != null) {
                                if(doc.get("dollars") != null && doc.get("cents") != null)
                                {
                                    receiverList.add(doc.getString("typeOf") + " for $" + doc.get("dollars") + "."+ doc.get("cents"));
                                    docList.add(doc.getId());
                                }
                            }
                        }

                        LoanListAdapter borrowAdapter = new LoanListAdapter(receiverList, docList, Transactions.this);

                        ListView lView = (ListView)findViewById(R.id.myList4);
                        lView.setAdapter(borrowAdapter);
                    }
                });

    }



    public void returnHome(View view)
    {
        startActivity(new Intent(Transactions.this, Home_Screen.class));
        Transactions.this.finish();
    }



}
