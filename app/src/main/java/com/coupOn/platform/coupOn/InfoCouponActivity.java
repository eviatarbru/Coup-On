package com.coupOn.platform.coupOn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.platform.coup_on.R;

public class InfoCouponActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_coupon);

        Intent coupInfo = getIntent();
        Bundle infoCoup = coupInfo.getExtras();
//        String couponImage1 = (String) infoCoup.get("couponImage");
//        String name1 = (String) infoCoup.get("couponName");
//        String username1 = (String) infoCoup.get("userCouponOwner");
//        String couponInfo1 = (String) infoCoup.get("infoCoupon");

        final ImageView couponImage = findViewById(R.id.couponImage);
        final TextView name = findViewById(R.id.couponName);
        final TextView username = findViewById(R.id.userCouponOwner);
        final TextView couponInfo = findViewById(R.id.infoCoupon);

//        name.setText(name1);
//        username.setText(username1);
//        couponInfo.setText(couponInfo1);

    }

    public void returnMainScreen(View view)
    {
        Intent intent = new Intent(this, SwipeCards.class);
        startActivity(intent);
        finish();
    }
}