package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.platform.coup_on.R;

public class ForgotPasswordScreen extends AppCompatActivity {

        private ImageView imageView;
        private EditText emailEditText;
        private Button resetPasswordBtn;
        //private ProgressBar progressBar;
        int count = 0;

        FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_ACTION_BAR);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_forgot_password_screen);
            this.imageView = findViewById(R.id.imageView);

            emailEditText = findViewById(R.id.emailEditText);
            resetPasswordBtn = findViewById(R.id.resetPasswordBtn); //resetPassword
            auth = FirebaseAuth.getInstance();

            imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                public void onSwipeTop() {
                }

                public void onSwipeRight() {
                    if (count == 0) {
                        imageView.setImageResource(R.drawable.nightcoupon);
                        count = 1;
                    } else {
                        imageView.setImageResource(R.drawable.lightcoupon);
                        count = 0;
                    }
                }

                public void onSwipeLeft() {
                    if (count == 0) {
                        imageView.setImageResource(R.drawable.nightcoupon);
                        count = 1;
                    } else {
                        imageView.setImageResource(R.drawable.lightcoupon);
                        count = 0;
                    }
                }

                public void onSwipeBottom() {
                }

            });
        }

    public void resetPassword(View view){
            String email = emailEditText.getText().toString().trim();

            if(email.isEmpty()){
                emailEditText.setError("Email is required!");
                emailEditText.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailEditText.setError("Please provide a valid email!");
                emailEditText.requestFocus();
                return;
            }
            //progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordScreen.this, "Check your email to reset password!", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(ForgotPasswordScreen.this, "Try again! Something went wrong:(", Toast.LENGTH_LONG).show();

                }
            });
    }


   /* public void verifyEmailBtn(View view)
    {

    }*/

    public void backScreen(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}