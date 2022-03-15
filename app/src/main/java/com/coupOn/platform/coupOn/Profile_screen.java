package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.platform.coup_on.R;

import java.io.File;
import java.io.IOException;

public class Profile_screen extends AppCompatActivity {

    File localFile = null;

    //Variables
    private TextView email;
    private ImageView imageView;
    private TextView textView;
    private TextView fullName;
    private String userID;
    private String emailStr;
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
                startActivity(new Intent(Profile_screen.this, LoginScreen.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();
        emailStr = user.getEmail();
        email.setText(emailStr);

        // Firebase-Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users")
            .document(emailStr);

        docRef.get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String fullNameStr = document.getString("FullName");
                        if (fullNameStr != null) {
                            fullName.setText(fullNameStr);
                        }
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
    }

    public void interestScreen(View view) {
        Intent intent = new Intent(Profile_screen.this, InterestsScreen.class);
        startActivity(intent);
    }
}


/*StorageReference storageRef =
          FirebaseStorage.getInstance().getReferenceFromUrl(
                  "gs://coup-on-project1.appspot.com"
          );
        StorageReference muichRef = storageRef.child("bayern-munich.jpg");


        try {
            localFile = File.createTempFile("bayern-munich", "jpg");
        } catch (IOException e) {
            Log.d("Profile_screen", "Failed to create local temp file", e);
        }

        muichRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to above local temp file
                        if (localFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(myBitmap);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });*/