package com.shashank.platform.loginui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ForgotPasswordScreen extends AppCompatActivity {

        private ImageView imageView;
        int count = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_ACTION_BAR);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_forgot_password_screen);
            this.imageView = findViewById(R.id.imageView);
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




    public void verifyEmailBtn(View view)
    {

    }

    public void backScreen(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}