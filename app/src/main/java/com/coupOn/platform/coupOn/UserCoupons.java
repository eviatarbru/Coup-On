package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private static String chatKey;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();

    private static int cameFrom;

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

        Intent infoIntent = getIntent();     //get data from last screen
        Bundle info = infoIntent.getExtras();


        cameFrom = (int) info.get("cameFrom");  //1 is for swipeCards
                                                //2 is for chatScreen
        if(cameFrom == 2)
            chatKey = (String) info.get("chatKey");

        new Thread(new setCouponInfo()).start(); //Making a Thread for the User's Info
    }

    private void setAdapter() {
        userCouponAdapter = new UserCouponAdapter(userCouponsList, this);
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
            if(cameFrom == 1) {
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
                                            , snapshot.getLong("Rank").intValue()
                                            , snapshot.getLong("Price").intValue());
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
            if(cameFrom == 2)
            {
                ArrayList<String> buckets = new ArrayList<>();
                System.out.println("chatkey: " + chatKey);
                databaseReference.child("chat").child(chatKey + "").child("coupons").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("count snapshot: " + snapshot.getChildrenCount());
                        for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                            System.out.println("data2: " + dataSnapshot2.getKey());
                            buckets.add(dataSnapshot2.getKey());
                        }
                        for(String bucket: buckets) {
                            FirebaseFirestore.getInstance()
                                    .collection("coupons")
                                    .document(bucket)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Coupon coupon;
                                            DocumentSnapshot snapshot = task.getResult();

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
                                                    , snapshot.getLong("Rank").intValue()
                                                    , snapshot.getLong("Price").intValue());
                                            // go over the real-time db to fetch the cid of the desired coupons
                                            userCouponsList.add(coupon);
                                            System.out.println(coupon + " this is a the");
                                            userCouponAdapter.updateData(userCouponsList);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    public static int getCameFrom() {
        return cameFrom;
    }
}