package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.platform.coup_on.R;

import java.util.List;

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

        Intent infoIntent = getIntent();     //get data from last screen
        Bundle info = infoIntent.getExtras();
        String name = (String) info.get("couponName"); // need to get from swipeCards the name of the coupon
        Uri imageUri = (Uri) info.get("imageUri");
        String ownerId = (String) info.get("ownerId");

        db.collection("users")
                .document(ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ownerName.setText(documentSnapshot.getString("FullName"));
                        ownerEmail.setText(documentSnapshot.getString("Email"));
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

// #### put in swipeCard in gotoCouponInfo ####
//final String couponName = this.couponName.getText().toString(); //couponName is a value that needs to be fetched from firebase
//intent.putExtra("couponName", couponName);
