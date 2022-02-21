package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.platform.coup_on.R;

public class Profile_screen extends AppCompatActivity {

    //Variables
    private TextView email;
    private ImageView imageView;
    private TextView textView;
    private TextView fullName;
    int count = 0;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        this.email = findViewById(R.id.email);
        this.fullName = findViewById(R.id.Fullname);

        mAuth = FirebaseAuth.getInstance();

        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
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

        });
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