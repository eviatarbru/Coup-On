package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.Arrays;
import java.util.List;

public class addCoupon extends AppCompatActivity {

    private EditText name;
    private EditText expireDate;
    private EditText location;
    private EditText description;
    private EditText couponCode;
    private EditText discountType;
    private EditText price;
    private Spinner spinner;


    private ImageView imageView; // light/ night
    int count = 0;

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
        this.spinner = (Spinner)(findViewById(R.id.TypeSpinner));
        this.price = findViewById(R.id.price);

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


        //code for google auto complete location: ######

//        Places.initialize(getApplicationContext(),"AIzaSyA6JkEmEgnUbveiTij15FncPHTasIkEWbY"); //Our API
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

    }   // not this

//   @Override
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

    // end code for google autocomplete location #######

    public void gotoAddCouponImage(View view) {
        Intent intent = new Intent(addCoupon.this, InterestsScreen.class);
        int rank = 0;

        final String name = this.name.getText().toString();
        final String expireDate = this.expireDate.getText().toString();
        final String location = this.location.getText().toString();
        final String description = this.description.getText().toString();
        final String couponCode = this.couponCode.getText().toString();
        final String discountType = this.discountType.getText().toString();
        final String choice = this.spinner.getSelectedItem().toString();
        final String price = this.price.getText().toString();
        final double finalPrice = Integer.parseInt(price);
        rank = rankCoupon(discountType, choice);
        if(rank == -1)
            return;

        System.out.println(rank + " this is the rank, MY G @@@@");


        intent.putExtra("name", name);
        intent.putExtra("expireDate", expireDate);
        intent.putExtra("location", location);
        intent.putExtra("description", description);
        intent.putExtra("couponCode", couponCode);
        intent.putExtra("discountType", discountType);
        intent.putExtra("fromScreen", 2);
        intent.putExtra("rank", rank);
        intent.putExtra("price", finalPrice);
        // we can add here the part that we give a ranking based on the discountType
        // evi need to look
        boolean validateCoupon = validateCoupon(name, expireDate, location, description, couponCode, discountType, finalPrice);
        if(!validateCoupon)
        {
            return;
        }

        startActivity(intent);
        finish();
        return;
    }

    public int rankCoupon(String discountType, String choice)
    {
        int rank = 0;
        if(choice.contains("xy%"))
        {
            if(!discountType.contains("%") || !onlyDigits(discountType, discountType.length() - 1) || discountType.contains(" ")) {
                Toast.makeText(addCoupon.this, "discount wrong, for example: 10%", Toast.LENGTH_SHORT).show();
                return -1;
            }
            if(discountType.contains("100%")) //100%
                rank = 5;
            else if(discountType.length() == 3) {
                if (discountType.charAt(0) == '1' || discountType.charAt(0) == '0')
                    rank = 1;
                else if (discountType.charAt(0) == '2' || discountType.charAt(0) == '3')
                    rank = 2;
                else if (discountType.charAt(0) == '4' || discountType.charAt(0) == '5')
                    rank = 3;
                else if (discountType.charAt(0) == '6' || discountType.charAt(0) == '7' || discountType.charAt(0) == '8' || discountType.charAt(0) == '9')
                    rank = 4;
            }
            else {
                Toast.makeText(addCoupon.this, "discount wrong, should be 1%-100%", Toast.LENGTH_SHORT).show();
                rank = -1;
            }
        }//<item>buy this, discount that</item>
        else if(choice.contains("x discount")) //money money 10$
        {
            if((!discountType.contains("₪") && !discountType.contains("$")  && !discountType.contains("€") && !discountType.contains("£") || discountType.contains(" ")) || !onlyDigits(discountType, discountType.length() - 1)) {
                Toast.makeText(addCoupon.this, "discount wrong, for example: 20$", Toast.LENGTH_SHORT).show();
                return -1;
            }
            int value = Integer.parseInt(discountType.substring(0, discountType.length() - 1));
            if(value < 20)
                rank = 2;
            else if(value < 40)
                rank = 3;
            else if(value < 80)
                rank = 4;
            else
                rank = 5;
        }
        else if(choice.contains("free")) // x+y free
        {
            if(!discountType.contains("+") || discountType.contains(" + ") || discountType.contains(" ") || !onlyDigits(discountType, discountType.length())) {
                Toast.makeText(addCoupon.this, "discount wrong, for example: 1+1", Toast.LENGTH_SHORT).show();
                return -1;
            }
            String [] vals = discountType.split("[+]" ,2);
            double valueleft = Double.parseDouble(vals[0]);
            double valueright = Double.parseDouble(vals[1]);
             double finalVal = 10.0 / (double)( valueleft / valueright);
            if(finalVal < 7)
                rank = 2;
            else if(finalVal <= 10.0)
                rank = 3;
            else if(finalVal <= 20.0)
                rank = 4;
            else
                rank = 5;
        }
        else
            rank = 2;
        return rank;
    }

    public boolean validateCoupon(String name, String expireDate, String location, String description, String couponCode
            , String discountType, double Price)
    {
        if(name.trim().isEmpty()) //name check
        {
            Toast.makeText(addCoupon.this, "Name is empty or wrong format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!DateWatcher.isDate(expireDate)) //date check
        {
            Toast.makeText(addCoupon.this, "Date format is wrong error!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(location.trim().isEmpty()) //varPassword and password check
        {
            Toast.makeText(addCoupon.this, "Location is wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(description.trim().isEmpty()) //name check
        {
            Toast.makeText(addCoupon.this, "Descriptions is empty or wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(couponCode.trim().isEmpty())
        {
            Toast.makeText(addCoupon.this, "The coupon code is empty or wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(discountType.trim().isEmpty())
        {
            Toast.makeText(addCoupon.this, "Discount type is empty or wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(discountType.trim().isEmpty())
        {
            Toast.makeText(addCoupon.this, "Discount type is empty or wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Price < 0)
        {
            Toast.makeText(addCoupon.this, "Enter a positive price", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean onlyDigits(String str, int n)
    {
        // Traverse the string from
        // start to end
        boolean val = false;
        for (int i = 0; i < n; i++) {

            // Check if character is
            // digit from 0-9
            // then return true
            // else false
            if (str.charAt(i) >= '0'
                    && str.charAt(i) <= '9') {
                val = true;
            } else {
                val = false;
            }

        }
        return val;
    }


}