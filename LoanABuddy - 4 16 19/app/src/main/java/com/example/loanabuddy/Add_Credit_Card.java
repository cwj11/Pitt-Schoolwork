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
import com.stripe.android.TokenCallback;
import com.stripe.android.view.CardInputWidget;

import com.stripe.android.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.ExternalAccount;
import com.stripe.net.RequestOptions;
//import com.stripe.model.Token;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;
import java.util.Map;

public class Add_Credit_Card extends AppCompatActivity {

    private FirebaseUser user;
    private static final String TAG = "ADD_CREDIT_CARD";
    private DocumentSnapshot document;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Add_Credit_Card mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        user = FirebaseAuth.getInstance().getCurrentUser();
        com.stripe.Stripe.apiKey = "sk_test_0EWfrNALXvb6Ie6shSQnqR0X00WBLUm7ye";
    }

    public void addCard(View view){
        String uid = user.getUid();

        if(user != null) {
            final DocumentReference values = db.collection("USERS").document(uid);
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
                                    CardInputWidget mCardInputWidget = findViewById(R.id.card_input_widget);
                                    Card card = mCardInputWidget.getCard();
                                    if (card == null) {
                                        return;
                                    }
                                    card.setCurrency("usd");

                                    Stripe stripe = new Stripe(mContext, "pk_test_GNj4HvpcPe3u4z4ZS6hWNFBV00Du5iprBI");
                                    stripe.createToken(
                                            card,
                                            new TokenCallback() {
                                                public void onSuccess(Token token) {

                                                    values.update("tokenId", token.getId());

                                                }
                                                public void onError(Exception error) {
                                                    error.printStackTrace();

                                                }
                                            }
                                    );

                                    String acct = document.get("account").toString();
                                    Account account = null;
                                    try {
                                        account = Account.retrieve(acct);
                                    } catch (StripeException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("external_account", document.get("tokenId").toString());
                                    try {
                                        account.getExternalAccounts().create(params);
                                    } catch (StripeException e1){
                                        e1.printStackTrace();
                                    }
                                }
                            }).start();




                        }
                    }
                }
            });
        }
    }


}
