package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.platform.coup_on.R;

public class CreditCard3 extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard3);

        CardForm cardForm1 = (CardForm) findViewById(R.id.card_form1);
        TextView txtDes = (TextView) findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);

        txtDes.setText("50 Coupoints");
        btnPay.setText(String.format("Payer %s", txtDes.getText()));

        cardForm1.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(CreditCard3.this, "Name: "+card.getName()+ " | Last 4 digits: "+card.getLast4()+ ", Payment approved. ",
                        Toast.LENGTH_SHORT).show();
                MainDB.getInstance().boughtCoupoints(50);
                Intent intent = new Intent(CreditCard3.this, SwipeCards.class);
                startActivity(intent);
            }
        });
    }
}
