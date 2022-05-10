package com.coupOn.platform.coupOn;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.platform.coup_on.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profile_screen extends AppCompatActivity {

    File localFile = null;

    //Variables
    private TextView email;
    private TextView fullName;
    private String userID;
    private Button logOut;
    private Button daemon;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser user;
    private DatabaseReference reference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //Date Structure for daemon
    private ArrayList<String> expiredCoupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_profile_screen);
        this.email = (TextView) findViewById(R.id.email);
        this.fullName = (TextView) findViewById(R.id.fullName);
        logOut = (Button) findViewById(R.id.signOut);
        this.daemon = findViewById(R.id.deamon);
        String uid = mAuth.getCurrentUser().getUid().toString();

        if (uid.equals("zlsGYk5EvLUfvyJtKe8k50nnrIG2")|| uid.equals("9K7MPR33qzN4gpO4Sp0onzRUmJG2") || uid.equals("rDknxGEhmLMd7KxVCbNgv0hMzwi1"))
            daemon.setVisibility(View.VISIBLE);
         else
            daemon.setVisibility(View.INVISIBLE);  //only admins



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile_screen.this, LoginScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // if the activity running has it's own context
            }
        });

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        // Firebase-Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users")
            .document(userID);

        docRef.get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String fullNameStr = document.getString("FullName");
                        String emailStr = document.getString("Email");
                        if (fullNameStr != null) {
                            fullName.setText(fullNameStr);
                        }
                       if(emailStr != null)
                            email.setText(emailStr);
                    }
                    else {
                        Toast.makeText(Profile_screen.this, "Failed to get user from FB", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void changePassword(View view) {
        Intent intent = new Intent(Profile_screen.this, ChangePasswordScreen.class);
        startActivity(intent);
    }

    public void backScreen(View view) {
        Intent intent = new Intent(Profile_screen.this, SwipeCards.class);
        startActivity(intent);
        finish();
    }

    public void interestScreen(View view) {
        Intent intent = new Intent(Profile_screen.this, InterestsScreen.class);
        intent.putExtra("fromScreen", 3);
        startActivity(intent);
    }

    public void activateDaemon(View view){

        expiredCoupons.clear();

        db.collection("coupons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String couponId = document.getString("CouponId");
                                String couponDateStr = document.getString("ExpireDate");

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                                try {
                                    Date couponDate = df.parse(couponDateStr);  //couponDate = the expire date of the coupon
                                    Date c = Calendar.getInstance().getTime();
                                    String formattedDate = df.format(c);
                                    Date currDate = df.parse(formattedDate);    //currDate = current date

                                    if(couponDate.compareTo(currDate) < 0){     // < 0 means currDate is greater
                                        expiredCoupons.add(couponId);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("@@@@ expired: " + expiredCoupons);

                                //now we clear all the notifications of those expired coupons

                            if(!expiredCoupons.isEmpty()){
                                MainDB.getInstance().NotificationClear(expiredCoupons);
                                Toast.makeText(view.getContext(), "Notifications cleared", Toast.LENGTH_SHORT).show();
                            }

                            for(String cid : expiredCoupons){
                                db.collection("coupons").document(cid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String imageName = documentSnapshot.getString("CouponImage");
                                        String ownerId = documentSnapshot.getString("UserUid");
                                        String couponName = documentSnapshot.getString("CoupName");
                                        String CouponId = documentSnapshot.getString("CouponId");

                                        //timestamp for time and notification id
                                        String timeStamp = ""+System.currentTimeMillis();

                                        List<String> notifications = new ArrayList<>();

                                        // Firebase-Firestore
                                        Map<String, Object> dataEdit = new HashMap<>();
                                        //data.put("CoupUid", "coupon");
                                        dataEdit.put("Notifications", notifications);
                                        DocumentReference updateUser = db.collection("users")
                                                .document(ownerId);
                                        updateUser.update("Notifications", FieldValue.arrayUnion("4" + "*"
                                                + mAuth.getCurrentUser().getUid() + "*" + timeStamp + "*" + couponName + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*"
                                                + CouponId + "*" + 0));
                                        //send a notification to the user that his coupon got deleted

                                        db.collection("coupons").document(cid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(view.getContext(), "Expired coupons deleted!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        if(!imageName.equals("default_coupon.jpeg")) {
                                            storage.getReference().child("/images").child(imageName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(view.getContext(), "Expired coupons Images Deleted!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        //end of db delete

                                    }
                                });
                            }       //end for
                        } else {
                            return;
                        }
                    }
                });
//        End removing expired coupons

//        now we decrease the rank of the coupons that are about to expire

        db.collection("coupons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String couponDateStr = document.getString("ExpireDate");
                            String couponId = document.getString("CouponId");
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                            try {
                                Date couponDate = df.parse(couponDateStr);  //couponDate = the expire date of the coupon
                                Date c = Calendar.getInstance().getTime();
                                String formattedDate = df.format(c);
                                Date currDate = df.parse(formattedDate);    //currDate = current date

                                int diff = getDateDiff(currDate, couponDate);

                                if(diff <= 7)
                                {
                                    db.collection("coupons").document(couponId).update("Rank", 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "Ranks updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    //end for
                    }
                });

    }

    int getDateDiff(Date currDate, Date couponDate){

        long different = couponDate.getTime() - currDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return (int) elapsedDays;
    }

    public void gotoCouponPoints(View view) {
        Intent intent = new Intent(Profile_screen.this, CouponPoints.class);
        startActivity(intent);
    }

}