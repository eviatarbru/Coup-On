package com.coupOn.platform.coupOn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    public void gotoAddCouponImage(View view) {
        Intent intent = new Intent(addCoupon.this, AddCouponImage.class);

        final String name = this.name.getText().toString();
        final String expireDate = this.exipreDate.getText().toString();
        final String location = this.location.getText().toString();
        final String description = this.description.getText().toString();


        intent.putExtra("name", name);
        intent.putExtra("expireDate", expireDate);
        intent.putExtra("location", location);
        intent.putExtra("description", description);

        boolean validateCoupon = validateCoupon(name, expireDate, location, description);
        if(!validateCoupon)
        {
            return;
        }

        startActivity(intent);
    }

    public boolean validateCoupon(String name, String expireDate, String location, String description)
    {
        if(name.trim().isEmpty()) //name check
        {
            Toast.makeText(addCoupon.this, "Email is empty or wrong format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!DateWatcher.isDate(expireDate)) //date check
        {
            Toast.makeText(addCoupon.this, "Password error!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(location.trim().isEmpty()) //varPassword and password check
        {
            Toast.makeText(addCoupon.this, "Passwords are not equal to each other!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(description.trim().isEmpty()) //name check
        {
            Toast.makeText(addCoupon.this, "Fullname is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}