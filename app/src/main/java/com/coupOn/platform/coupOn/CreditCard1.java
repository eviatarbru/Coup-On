package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.shashank.platform.coup_on.R;

public class CreditCard1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard1);

        CardForm cardForm1 = (CardForm) findViewById(R.id.card_form1);
        TextView txtDes = (TextView) findViewById(R.id.payment_amount);
        Button btnPay = (Button) findViewById(R.id.btn_pay);

        txtDes.setText("20 NIS");
        btnPay.setText(String.format("Payer %s", txtDes.getText()));

        cardForm1.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(CreditCard1.this, "Name: "+card.getName()+ " | Last 4 digits: "+card.getLast4()+ ", Payment approved. ",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreditCard1.this, SwipeCards.class);
                startActivity(intent);
            }
        });
    }
}
