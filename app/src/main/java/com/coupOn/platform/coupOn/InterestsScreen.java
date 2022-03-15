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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InterestsScreen extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TAG";
    private boolean [] isPressed;
    private String [] interests = {"Gaming", "Utility", "Entertainment", "Merch", "Spa", "Movies", "Sports", "Animals", "Flights", "Transportation", "Museums and shit", "Gayming"};

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_screen);
        int val;
        String st;
        RelativeLayout tempLayout;
        GridLayout container = findViewById(R.id.container);
        container.setColumnCount(3);
        container.setRowCount(this.interests.length / 3);
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
        int index = view.getId();
        this.isPressed[index] = !this.isPressed[index];
        if(this.isPressed[index])
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayoutLight));
            view.setBackground(getDrawable(R.drawable.gridbuttonselected));
        }
        else
        {
            ((Button)view).setTextColor(getResources().getColor(R.color.textInputLayout));
            view.setBackground(getDrawable(R.drawable.gridbuttonunselected));
        }
        System.out.println(Arrays.toString(this.isPressed));
    }

    public void registerCompletion(View view)
    {
        String interestsFb = "";
        for(int i = 0; i < this.isPressed.length; i++)
        {
            if(this.isPressed[i])
            {
                interestsFb = interestsFb + ", " + this.interests[i];
            }
        }

        Intent regIntent = getIntent();
        Bundle infoReg = regIntent.getExtras();
        String email = (String) infoReg.get("email");
        String password = (String) infoReg.get("password");
        String fullName = (String) infoReg.get("fullName");
        String dateOfBirth = (String) infoReg.get("dateOfBirth");
        String finalInterestsFb = interestsFb;

        System.out.println(email + " " + password + " " + fullName + " " + dateOfBirth + " " + finalInterestsFb);
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
                    //user.put("Uid", mAuth.getCurrentUser().getUid());
                    //user.put("Email", email);
                    user.put("FullName", fullName);
                    user.put("DateOfBirth", dateOfBirth);
                    user.put("Interests", finalInterestsFb);

                        db.collection("users")
                            .document(mAuth.getCurrentUser().getEmail())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(InterestsScreen.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InterestsScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}