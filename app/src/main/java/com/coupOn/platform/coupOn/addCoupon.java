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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.platform.coup_on.R;

import java.util.HashMap;
import java.util.Map;

public class addCoupon extends AppCompatActivity {

    private EditText name;
    private EditText expireDate;
    private EditText location;
    private EditText description;
    private ImageView imageView;
    int count = 0;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        this.name = findViewById(R.id.couponName);
        this.expireDate = findViewById(R.id.expireDate);
        this.expireDate.addTextChangedListener(new DateWatcher());
        this.location = findViewById(R.id.location);
        this.description = findViewById(R.id.description);
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
        final String expireDate = this.expireDate.getText().toString();
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
        finish();
        return;
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
            Toast.makeText(addCoupon.this, "Full Name is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}