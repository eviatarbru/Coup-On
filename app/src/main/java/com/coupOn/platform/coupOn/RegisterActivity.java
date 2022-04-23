package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText varPassword;
    private EditText fullName;
    private EditText dateOfBirth;
    private ImageView imageView;
    private Drawable lock;
    int count = 0;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        this.email = findViewById(R.id.email);
        this.lock = DrawableCompat.wrap(AppCompatResources.getDrawable(this, R.drawable.ic_lock_white_24dp));
        this.password = findViewById(R.id.password);
        this.password.addTextChangedListener(new PasswordWatcher(this.password, this));
        this.varPassword = findViewById(R.id.verifyPassword);
        this.fullName = findViewById(R.id.Fullname);
        this.dateOfBirth = findViewById(R.id.dateOfBirth);
        this.dateOfBirth.addTextChangedListener(new DateWatcher());
        this.imageView = findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    imageView.setImageResource(R.drawable.lightcoupon);
                    count = 1;
                } else {
                    imageView.setImageResource(R.drawable.nightcoupon);
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

    public void registerListener(View view)
    {
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        final String varPassword = this.varPassword.getText().toString();
        final String fullName = this.fullName.getText().toString();
        final String dateOfbirth = this.dateOfBirth.getText().toString();
        boolean validate = validate(email, password, varPassword, fullName, dateOfbirth);
        if(!validate)
        {
            return;
        }
        //check email already exist or not.
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {

                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                    if (isNewUser)
                    {
                        Intent intent = new Intent(RegisterActivity.this, InterestsScreen.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        intent.putExtra("fullName", fullName);
                        intent.putExtra("dateOfBirth", dateOfbirth);
                        intent.putExtra("fromScreen", 1);

                        startActivity(intent);
                        finish();
                        return;
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Email Already Exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                });
    }

    public boolean validate(String email, String password, String valPassword, String fullName, String dateOfBirth)
    {
        if(email.trim().isEmpty() || email.indexOf("@") == -1) //mail check
        {
            Toast.makeText(RegisterActivity.this, "Email is empty or wrong format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.trim().isEmpty()) //password check
        {
            Toast.makeText(RegisterActivity.this, "Password error!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.equals(valPassword)) //varPassword and password check
        {
            Toast.makeText(RegisterActivity.this, "Passwords are not equal to each other!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(fullName.trim().isEmpty()) //name check
        {
            Toast.makeText(RegisterActivity.this, "Fullname is empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!DateWatcher.isDate(dateOfBirth)) //date of birth check
        {
            Toast.makeText(RegisterActivity.this, "Check date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
