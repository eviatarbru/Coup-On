package com.coupOn.platform.coupOn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.platform.coup_on.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddCouponImage extends AppCompatActivity {

    private ImageView imageView;
    int count = 0;      //for the light and dark theme


    private ImageView couponPic;
    private ImageView upload_Icon;
    private TextView upload_Text;
    private Button confirmBtn;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon_image);

        this.mAuth = FirebaseAuth.getInstance();

        this.imageView = findViewById(R.id.imageView);

        this.couponPic = findViewById(R.id.image_icon);
        this.upload_Icon = findViewById(R.id.upload_icon);
        this.upload_Text = findViewById(R.id.upload_text);
        this.confirmBtn = findViewById(R.id.confirmBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

        couponPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        upload_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        upload_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() { //adding coupon to db
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCouponImage.this, SwipeCards.class);

                Intent confirmIntent = getIntent();     //get data from last screen
                Bundle infoConfirm = confirmIntent.getExtras();
                String name = (String) infoConfirm.get("name");
                String expireDate = (String) infoConfirm.get("expireDate");
                String location = (String) infoConfirm.get("location");
                String description = (String) infoConfirm.get("description");

                String userUid =  mAuth.getCurrentUser().getUid();

                //System.out.println(mAuth.getCurrentUser().getUid() + "byle");

                Map<String, Object> data = new HashMap<>();
                //data.put("CoupUid", "coupon");
                data.put("CoupName", name);
                data.put("ExpireDate", expireDate);
                data.put("Location", location);
                data.put("Description", description);
                data.put("CouponImage", imageUri);
                data.put("UserUid",userUid);


                db.collection("coupons").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddCouponImage.this, "Coupon added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCouponImage.this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
                            }
                        });



                startActivity(intent);
            }
        });

    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            couponPic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        // final ProgressDialog pd = new ProgressDialog(this);
        // pd.setTitle("Uploading Image...");
        // pd.show();

        final String randomKey = UUID.randomUUID().toString();
        //System.out.println("@@@the random key:" + randomKey);
        String imageUri_String;

        imageUri_String = imageUri.toString();


        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageReference.child(imageUri_String);

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageReference.child("images/" + randomKey);

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

        //addOnProgressListener(new OnProgressListener<>()) ### not working

        }

//    public void confirmCoupon(View view) {
//        Intent intent = new Intent(AddCouponImage.this, SwipeCards.class);
//
//        Intent confirmIntent = getIntent();
//        Bundle infoConfirm = confirmIntent.getExtras();
//        String name = (String) infoConfirm.get("name");
//        String expireDate = (String) infoConfirm.get("expireDate");
//        String location = (String) infoConfirm.get("location");
//        String description = (String) infoConfirm.get("description");
//
//        Map<String, Object> user = new HashMap<>();
//        user.put("Uid", mAuth.getCurrentUser().getUid());
//        user.put("CoupName", name);
//        user.put("ExpireDate", expireDate);
//        user.put("Location", location);
//        user.put("Description", description);
//        user.put("CouponImage", imageUri);
//
//        db.collection("users")
//                .document(mAuth.getCurrentUser().getUid())
//                .set(user);
//
//        startActivity(intent);
//    }
}


