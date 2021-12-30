package com.coupOn.platform.coupOn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.shashank.platform.coup_on.R;

public class addCoupon extends AppCompatActivity {

    private EditText name;
    private EditText exipreDate;
    private EditText location;
    private EditText description;
    private ImageView imageView;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        this.name = findViewById(R.id.couponName);
        this.exipreDate = findViewById(R.id.expireDate);
        this.location = findViewById(R.id.location);
        this.exipreDate = findViewById(R.id.expireDate);
        this.exipreDate.addTextChangedListener(new DateWatcher());
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

    public void couponValidationAndAdd(View view) {
    }
}