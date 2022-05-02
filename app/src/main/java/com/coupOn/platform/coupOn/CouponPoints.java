package com.coupOn.platform.coupOn;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.platform.coup_on.R;

public class CouponPoints extends AppCompatActivity {

    //Variables
    private int packageChoice = 0;
    private ImageView p1; //package 1
    private ImageView p2; //package 2
    private ImageView p3; //package 3

    private ImageView imageView; // light/ night
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_points);
        this.p1 = findViewById(R.id.package_one);
        this.p2 = findViewById(R.id.package_two);
        this.p3 = findViewById(R.id.package_three);


        this.imageView = findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.lightcoupon);
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.nightcoupon);
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.nightcoupon);
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.lightcoupon);
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });
    }

    public void choseOne(View view) {
        packageChoice = 1;
        Intent intent = new Intent(CouponPoints.this, CreditCard1.class);
        startActivity(intent);
    }

    public void choseTwo(View view) {
        packageChoice = 2;
        Intent intent = new Intent(CouponPoints.this, CreditCard2.class);
        startActivity(intent);
    }

    public void choseThree(View view) {
        packageChoice = 3;
        Intent intent = new Intent(CouponPoints.this, CreditCard3.class);
        startActivity(intent);
    }
}
