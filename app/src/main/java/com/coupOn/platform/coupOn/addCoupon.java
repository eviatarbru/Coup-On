package com.coupOn.platform.coupOn;

import androidx.annotation.Nullable;
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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.platform.coup_on.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addCoupon extends AppCompatActivity {

    private EditText name;
    private EditText expireDate;
    private EditText location;
    private EditText description;
    private EditText couponCode;
    private EditText discountType;


    private ImageView imageView; // light/ night
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
        this.couponCode = findViewById(R.id.couponCode);
        this.discountType = findViewById(R.id.discountType);

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

        //code for google auto complete location

//        Places.initialize(getApplicationContext(),"AIzaSyA6JkEmEgnUbveiTij15FncPHTasIkEWbY");
//
//        location.setFocusable(false);
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
//                ,Place.Field.LAT_LNG, Place.Field.NAME);
//
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
//                        fieldList).build(addCoupon.this);
//                startActivityForResult(intent, 100);
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 100 && resultCode == RESULT_OK){
//            Place place = Autocomplete.getPlaceFromIntent(data);
//            location.setText(place.getAddress());
//        }
//        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
//            Status status = Autocomplete.getStatusFromIntent(data);
//
//            Toast.makeText(getApplicationContext(),status.getStatusMessage()
//            ,Toast.LENGTH_SHORT).show();
//        }
//    }

    // end code for google autocomplete location

    public void gotoAddCouponImage(View view) {
        Intent intent = new Intent(addCoupon.this, InterestsScreen.class);

        final String name = this.name.getText().toString();
        final String expireDate = this.expireDate.getText().toString();
        final String location = this.location.getText().toString();
        final String description = this.description.getText().toString();
        final String couponCode = this.couponCode.getText().toString();
        final String discountType = this.discountType.getText().toString();

        intent.putExtra("name", name);
        intent.putExtra("expireDate", expireDate);
        intent.putExtra("location", location);
        intent.putExtra("description", description);
        intent.putExtra("couponCode", couponCode);
        intent.putExtra("discountType", discountType);
        intent.putExtra("fromScreen", 2);

        // we can add here the part that we give a ranking based on the discountType
        // evi need to look
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