package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.shashank.platform.coup_on.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
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
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null)
                {
                    Intent intent = new Intent(RegisterActivity.this, InterestsScreen.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
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
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "sign_in_error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseUser fuser = mAuth.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
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
                    user.put("DateOfBirth", dateOfbirth);

                    db.collection("users")
                            .document(mAuth.getCurrentUser().getUid())
                            .set(user);
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(RegisterActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                    });
                }
            }
        });
    }

    public boolean validate(String email, String password, String valPassword, String fullName, String dateOfBirth)
    {
        if(email.trim().isEmpty() || email.indexOf("@") == -1) //mail check
            return false;
        if(password.trim().isEmpty()) //password check
            return false;
        if(!password.equals(valPassword)) //varPassword and password check
            return false;
        if(fullName.trim().isEmpty()) //name check
            return false;
        if(!DateWatcher.isDate(dateOfBirth)) //date of birth check
            return false;
        return true;
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
