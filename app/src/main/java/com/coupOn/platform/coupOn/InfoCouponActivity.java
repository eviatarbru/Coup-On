package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

public class InfoCouponActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_coupon);
        this.mAuth = FirebaseAuth.getInstance();

        String userUid = mAuth.getCurrentUser().getUid();

        ImageView couponImage = findViewById(R.id.couponImage);
        TextView couponName = findViewById(R.id.couponName);
        TextView ownerName = findViewById(R.id.ownerName);
        TextView ownerEmail = findViewById(R.id.ownerEmail);
        TextView ownerCode = findViewById(R.id.ownerCode);
//        TextView ownerCodeTxt = findViewById(R.id.ownerCodeTxt);

        ownerCode.setVisibility(View.INVISIBLE);
//        ownerCodeTxt.setVisibility(View.INVISIBLE);

        Intent infoIntent = getIntent();     //get data from last screen
        Bundle info = infoIntent.getExtras();
        String name = (String) info.get("couponName"); // need to get from swipeCards the name of the coupon
        Uri imageUri = (Uri) info.get("imageUri");
        String ownerId = (String) info.get("ownerId");
        String couponId = (String) info.get("couponID");

        db.collection("users")
                .document(ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ownerName.setText(documentSnapshot.getString("FullName"));
                        ownerEmail.setText(documentSnapshot.getString("Email"));
//                        ownerCode.setText(documentSnapshot.getString("CouponCode"));
//                        if(!documentSnapshot.getBoolean("Tradeable")){
//                            ownerCode.setVisibility(View.VISIBLE);
//                        }
//                        else{
//                            System.out.println("we got a null");
//                        }
                    }
                });

        db.collection("coupons")
                .document(couponId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ownerCode.setText("Code: " + documentSnapshot.getString("CouponCode"));
                        if(!documentSnapshot.getBoolean("Tradeable")){
                            ownerCode.setVisibility(View.VISIBLE);
//                            ownerCodeTxt.setVisibility(View.VISIBLE);
                        }
                        else{
                            System.out.println("we got a null");
                        }
                    }
                });

        Glide.with(this)
                .load(imageUri) // the uri you got from Firebase
                .into(couponImage);

        couponName.setText(name);


    }

    public void returnMainScreen(View view)
    {
        Intent intent = new Intent(this, SwipeCards.class);
        startActivity(intent);
        finish();
    }
}
