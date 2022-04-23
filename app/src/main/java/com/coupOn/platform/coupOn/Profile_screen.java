package com.coupOn.platform.coupOn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.io.File;

public class Profile_screen extends AppCompatActivity {

    File localFile = null;

    //Variables
    private TextView email;
    private TextView fullName;
    private String userID;
    private Button logOut;

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
        logOut = (Button) findViewById(R.id.signOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile_screen.this, LoginScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // if the activity running has it's own context
            }
        });

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        // Firebase-Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users")
            .document(userID);

        docRef.get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String fullNameStr = document.getString("FullName");
                        String emailStr = document.getString("Email");
                        if (fullNameStr != null) {
                            fullName.setText(fullNameStr);
                        }
                       if(emailStr != null)
                            email.setText(emailStr);
                    }
                    else {
                        Toast.makeText(Profile_screen.this, "Failed to get user from FB", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void changePassword(View view) {
        Intent intent = new Intent(Profile_screen.this, ChangePasswordScreen.class);
        startActivity(intent);
    }

    public void backScreen(View view) {
        Intent intent = new Intent(Profile_screen.this, SwipeCards.class);
        startActivity(intent);
        finish();
    }

    public void interestScreen(View view) {
        Intent intent = new Intent(Profile_screen.this, InterestsScreen.class);
        intent.putExtra("fromScreen", 3);
        startActivity(intent);
    }
}