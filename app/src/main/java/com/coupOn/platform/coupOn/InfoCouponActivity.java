package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

//        Intent coupInfo = getIntent();
//        Bundle infoCoup = coupInfo.getExtras();
//        String couponImage1 = (String) infoCoup.get("couponImage");
//        String name1 = (String) infoCoup.get("couponName");
//        String username1 = (String) infoCoup.get("userCouponOwner");
//        String couponInfo1 = (String) infoCoup.get("infoCoupon");

        ImageView couponImage = findViewById(R.id.couponImage);
        TextView couponName = findViewById(R.id.couponName);
        TextView ownerName = findViewById(R.id.ownerName);
        TextView ownerEmail = findViewById(R.id.ownerEmail);

        Intent confirmIntent = getIntent();     //get data from last screen
        Bundle infoConfirm = confirmIntent.getExtras();
        //String couponName = (String) infoConfirm.get("couponName"); // need to get from swipeCards the name of the coupon

        db.collection("users")
                .whereEqualTo("Uid", userUid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        System.out.println("@@@@ success!!!");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList){
                            String name = snapshot.getString("FullName");
                            String email = snapshot.getString("Email");
                            ownerName.setText(name);
                            ownerEmail.setText(email);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
//        couponName.setText(name1);

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
