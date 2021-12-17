package com.shashank.platform.coup_on;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText fullName;
    private EditText dateOfBirth;
    private ImageView imageView;
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
        this.password = findViewById(R.id.password);
        this.fullName = findViewById(R.id.Fullname);
        this.dateOfBirth = findViewById(R.id.dateOfBirth);
        this.dateOfBirth .addTextChangedListener(new DateWatcher());
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
        final String fullName = this.fullName.getText().toString();
        final String dateOfbirth = this.dateOfBirth.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "sign_in_error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String, Object> user = new HashMap<>();
                    user.put("Uid", mAuth.getCurrentUser().getUid());
                    user.put("Email", email);
                    user.put("FullName", fullName);
                    user.put("DateOfBirth", dateOfbirth);

                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RegisterActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
