package com.coupOn.platform.coupOn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

public class UserCoupons extends AppCompatActivity {

    private final ArrayList<Coupon> userCouponsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserCouponAdapter userCouponAdapter;
    private static UserCoupons instance;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static UserCoupons getInstance() {
        if(instance == null)
            instance = new UserCoupons();
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coupons);

        this.mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.couponsList);
        setAdapter();

        new Thread(new setCouponInfo()).start(); //Making a Thread for the User's Info
    }

    private void setAdapter() {
        userCouponAdapter = new UserCouponAdapter(userCouponsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userCouponAdapter);
    }


    //    private void setCouponInfo() {
    public class setCouponInfo implements Runnable {
        boolean isFinish = true;
        @Override
        public void run() {
            String userUid = mAuth.getCurrentUser().getUid();
            FirebaseFirestore.getInstance()
                    .collection("coupons")
                    .whereEqualTo("UserUid", userUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Coupon coupon;
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {

                                coupon = new Coupon(snapshot.getString("CouponImage")
                                        , snapshot.getString("CoupName")
                                        , snapshot.getString("ExpireDate")
                                        , snapshot.getString("Location")
                                        , snapshot.getString("Description")
                                        , snapshot.getString("UserUid")
                                        , snapshot.getString("CouponId")
                                        , snapshot.getString("Interest")
                                        , snapshot.getString("DiscountType")
                                        , snapshot.getString("CouponCode")
                                        ,snapshot.getLong("Rank").intValue()
                                        ,snapshot.getLong("Price").intValue());
                                userCouponsList.add(coupon);
                            }
                            userCouponAdapter.updateData(userCouponsList);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

}