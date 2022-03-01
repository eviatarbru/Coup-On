package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.platform.coup_on.R;

public class Profile_screen extends AppCompatActivity {

    //Variables
    private TextView email;
    private ImageView imageView;
    private TextView textView;
    private TextView fullName;
    private String userID;
    int count = 0;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        this.email = (TextView) findViewById(R.id.email);
        this.fullName = (TextView) findViewById(R.id.fullName);

        /*imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.lightcoupon);
                    textView.setText("On");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.nightcoupon);
                    textView.setText("Off");
                    count = 0;
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.lightcoupon);
                    textView.setText("On");
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.nightcoupon);
                    textView.setText("Off");
                    count = 0;
                }
            }

            public void onSwipeBottom() {
            }

        });*/

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email.setText(user.getEmail());
                fullName.setText((user.getDisplayName()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_screen.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        if(uid != null){
            String email2 = firebaseUser.getEmail();
            String fullName2 = firebaseUser.getDisplayName();
        }*/

        /*firebaseAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
                if (firebaseUser != null) {
                    String email2 = firebaseUser.getEmail();
                    String fullName2 = firebaseUser.getDisplayName();
                }
            }
        };*/

    }

    public void loginScreen(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordScreen.class);
        startActivity(intent);
    }

    public void backScreen(View view) {
        Intent intent = new Intent(this, SwipeCards.class);
        startActivity(intent);
    }
}