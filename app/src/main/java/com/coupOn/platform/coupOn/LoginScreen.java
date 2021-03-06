package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.platform.coup_on.R;

public class LoginScreen extends AppCompatActivity {

    //Variables
    private ImageView imageView;
    private TextView textView;
    private EditText email;
    private EditText password;
    private Button signInButton;
    private TextView forgotPassword;
    int count = 0;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        this.imageView = findViewById(R.id.imageView);
        this.textView = findViewById(R.id.textView);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.signInButton = findViewById(R.id.register);
        this.forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(this::ForgotPasswordBtn);

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
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null)
                {
                    Intent intent = new Intent(LoginScreen.this, SwipeCards.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
    }

    public void signInListener(View view)
    {
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(LoginScreen.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(LoginScreen.this, "Login error!", Toast.LENGTH_SHORT).show();
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

    public void signUpListener(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void ForgotPasswordBtn(View view)
    {
        Intent intent = new Intent(this, ForgotPasswordScreen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
