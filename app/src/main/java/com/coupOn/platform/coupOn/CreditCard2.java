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
import com.shashank.platform.coup_on.R;

public class CreditCard2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard2);

        CardForm cardForm1 = (CardForm) findViewById(R.id.card_form1);
        TextView txtDes = (TextView) findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);

        txtDes.setText("40 Coupoints");
        btnPay.setText(String.format("Payer %s", txtDes.getText()));

        cardForm1.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(CreditCard2.this, "Name: "+card.getName()+ " | Last 4 digits: "+card.getLast4()+ ", Payment approved. ",
                        Toast.LENGTH_SHORT).show();
                MainDB.getInstance().boughtCoupoints(40);
                Intent intent = new Intent(CreditCard2.this, SwipeCards.class);
                startActivity(intent);
            }
        });
    }
}
