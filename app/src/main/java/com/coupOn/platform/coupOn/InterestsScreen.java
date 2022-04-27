package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsScreen extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TAG";
    private boolean [] isPressed;
    private String [] interests = {"Food", "Fashion", "Animals", "Health", "Cultivation", "Education", "Travel"
                                        , "Electronics", "Sports", "House", "Attractions", "Special"};
    private int pressedBeforeInterests = -1;
    private View beforePressedBtn = null;

    private int fromScreen;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_screen);
        TextView titleText = findViewById(R.id.titleText);
        Intent regIntent = getIntent();
        Bundle infoReg = regIntent.getExtras();
        this.fromScreen = (int) infoReg.get("fromScreen");
        switch(this.fromScreen)
        {
            case 2:
                titleText.setText("Coupon Interests");
                break;
            case 3:
                titleText.setText("Edit Interests");
                break;
            default:
                titleText.setText("Interests");
                break;
        }
        String st;
        RelativeLayout tempLayout;
        GridLayout container = findViewById(R.id.container);
        container.setColumnCount(3);
        container.setRowCount(this.interests.length / 3);
        mAuth = FirebaseAuth.getInstance();
        switch (this.fromScreen)
        {
            case 1:
                mAuth = FirebaseAuth.getInstance();
                firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null)
                        {
                            Intent intent = new Intent(InterestsScreen.this, LoginScreen.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                };
                break;
        }
        GridLayout.LayoutParams params;
        this.isPressed = new boolean[this.interests.length];
        for(int i = 0; i < this.interests.length / 3 * 3; i++)
        {
            params = new GridLayout.LayoutParams();
            params.height = LayoutParams.WRAP_CONTENT;
            params.width = LayoutParams.WRAP_CONTENT;
            st = interests[i];
            params.columnSpec = GridLayout.spec(i % 3, 1, 1);
            params.rowSpec = GridLayout.spec(i / 3, 1, 1);
            params.topMargin = 30;
            params.rightMargin = 5;
            params.leftMargin = 5;
            container.addView(createButton(st, i), params);
        }
    }

    public Button createButton(String st, int val)
    {
        Button btn;
        btn = new Button(this);
        btn.setId(val);
//        btn.setTag(val);
        btn.setText(st);
        btn.setAllCaps(false);
        btn.setOnClickListener(this);
        btn.setTextColor(getResources().getColor(R.color.textInputLayout));
        btn.setBackground(getDrawable(R.drawable.gridbuttonunselected));
        return btn;
    }

    @Override
    public void onClick(View view) {
        if(this.fromScreen == 2 && this.pressedBeforeInterests != -1)
        {
            this.isPressed[this.pressedBeforeInterests] = false;
        }
        int index = view.getId();
        this.isPressed[index] = !this.isPressed[index];
        if(this.isPressed[index])
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayoutLight));
            view.setBackground(getDrawable(R.drawable.gridbuttonselected));
            if(this.beforePressedBtn != null && this.fromScreen == 2)
            {
                ((Button)this.beforePressedBtn).setTextColor(getResources().getColor(R.color.textInputLayout));
                this.beforePressedBtn.setBackground(getDrawable(R.drawable.gridbuttonunselected));
            }
        }
        else
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayout));
            view.setBackground(getDrawable(R.drawable.gridbuttonunselected));
            if(this.beforePressedBtn != null && this.fromScreen == 2)
            {
                ((Button)this.beforePressedBtn).setTextColor(getResources().getColor(R.color.textInputLayout));
                this.beforePressedBtn.setBackground(getDrawable(R.drawable.gridbuttonunselected));
            }
        }
        this.pressedBeforeInterests = index;
        this.beforePressedBtn = view;
    }

    public void registerCompletion(View view)
    {
        List<String> interestsFb = new ArrayList<>();
        for(int i = 0; i < this.isPressed.length; i++)
        {
            if(this.isPressed[i])
            {
                interestsFb.add(this.interests[i]);
            }
        }
//        interestsFb = interestsFb.substring(0, interestsFb.length()-2);

        Intent regIntent = getIntent();
        Bundle infoReg = regIntent.getExtras();
        switch (this.fromScreen)
        {
            case 1: //The user registration
                String email = (String) infoReg.get("email");
                String password = (String) infoReg.get("password");
                String fullName = (String) infoReg.get("fullName");
                String dateOfBirth = (String) infoReg.get("dateOfBirth");

                List<String> finalInterestsFb = interestsFb;
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(InterestsScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(InterestsScreen.this, "sign_in_error", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            FirebaseUser fuser = mAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(InterestsScreen.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Map<String, Object> user = new HashMap<>();
                            user.put("Uid", mAuth.getCurrentUser().getUid());
                            user.put("Email", email);
                            user.put("FullName", fullName);
                            user.put("DateOfBirth", dateOfBirth);
                            user.put("Interests", finalInterestsFb);

                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(user);

                        }
                    }
                });
                break;
            case 2: //The coupon registration
                String coupName = (String) infoReg.get("name");
                String expireDate = (String) infoReg.get("expireDate");
                String location = (String) infoReg.get("location");
                String description = (String) infoReg.get("description");
                String couponCode2 = (String) infoReg.get("couponCode");
                String discountType2 = (String) infoReg.get("discountType");
                int price = (int) infoReg.get("price");
                int rank = (int) infoReg.get("rank");
                String couponInterests = interestsFb.get(0);

                Intent intent2 = new Intent(InterestsScreen.this, AddCouponImage.class);
                intent2.putExtra("name", coupName);
                intent2.putExtra("expireDate", expireDate);
                intent2.putExtra("location", location);
                intent2.putExtra("description", description);
                intent2.putExtra("interests", couponInterests);
                intent2.putExtra("couponCode", couponCode2);
                intent2.putExtra("discountType", discountType2);
                intent2.putExtra("rank", rank);
                intent2.putExtra("price", price);

                startActivity(intent2);
                break;
            case 3: //The user edit Interests
                Map<String, Object> dataEdit = new HashMap<>();
                //data.put("CoupUid", "coupon");
                dataEdit.put("Interests", interestsFb);
                DocumentReference updateUser = db.collection("users")
                        .document(mAuth.getCurrentUser().getUid());
                updateUser.update(dataEdit);
                Intent intent3 = new Intent(InterestsScreen.this, Profile_screen.class);
                startActivity(intent3);
                break;
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        switch (this.fromScreen)
        {
            case 1:
                mAuth.addAuthStateListener(firebaseAuthStateListener);
                break;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        switch (this.fromScreen)
        {
            case 1:
                mAuth.removeAuthStateListener(firebaseAuthStateListener);
                break;
        }
    }
}