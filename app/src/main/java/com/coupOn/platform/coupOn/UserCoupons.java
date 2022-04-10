package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.coupOn.platform.coupOn.Model.User;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.platform.coup_on.R;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserCoupons extends AppCompatActivity {

    private final ArrayList<Coupon> userCouponsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserCouponAdapter userCouponAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coupons);

        this.mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.couponsList);

        new Thread(new setCouponInfo()).start(); //Making a Thread for the User's Info

        setAdapter();

//        readData();
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

                            System.out.println("@@@@ on success");

                            Coupon coupon;

                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotList) {

                                //                            System.out.println("@@@@ "+ snapshot.getString("CoupName"));

                                coupon = new Coupon(snapshot.getString("CouponImage")
                                        , snapshot.getString("CoupName")
                                        , snapshot.getString("ExpireDate")
                                        , snapshot.getString("Location")
                                        , snapshot.getString("Description")
                                        , snapshot.getString("UserUid")
                                        , snapshot.getString("CouponId")
                                        , snapshot.getString("Interest"));
                                userCouponsList.add(coupon);
                                System.out.println("@@@@" + coupon.toString());
                            }
                            System.out.println("@@@@ after for");
                            userCouponAdapter.updateData(userCouponsList);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("@@@@ fail!");
                        }
                    });
        }
    }
}