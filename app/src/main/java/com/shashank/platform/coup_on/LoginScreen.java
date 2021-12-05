package com.shashank.platform.coup_on;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private EditText email;
    private EditText password;
    private Button signUpButton;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);
        this.imageView = findViewById(R.id.imageView);
        this.textView = findViewById(R.id.textView);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.signUpButton = findViewById(R.id.nextPage);
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

    public void signUpListener(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void signInListener(View view)
    {
        System.out.println(this.email.getText());
        System.out.println(this.password.getText());
        Intent intent = new Intent(this, SwipeCards.class);
        startActivity(intent);
    }

    public void ForgotPasswordBtn(View view)
    {
        Intent intent = new Intent(this, ForgotPasswordScreen.class);
        startActivity(intent);
    }
}
