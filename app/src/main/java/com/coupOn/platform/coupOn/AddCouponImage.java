package com.coupOn.platform.coupOn;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.coupOn.platform.coupOn.Model.Coupon;
import com.coupOn.platform.coupOn.Model.MainDB;
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
import com.shashank.platform.coup_on.databinding.ActivityAddCouponImageBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddCouponImage extends AppCompatActivity {

    private ImageView imageView;
    int count = 0;      //for the light and dark theme


    private ImageView image_icon;
    private ImageView upload_Icon;
    private TextView upload_Text;
    private Button confirmBtn;

    private FirebaseStorage storage;
    public StorageReference storageReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ActivityAddCouponImageBinding binding; //for the image
    //public ActivityResultLauncher<String> mTakePhoto;
    public Uri imageUri;
    //public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCouponImageBinding.inflate(getLayoutInflater()); //for the image
        setContentView(binding.getRoot());

//        mTakePhoto = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri result) {
//
//                        binding.imageIcon.setImageURI(result);
//                    }
//                }
//        );

        this.mAuth = FirebaseAuth.getInstance();

        this.imageView = findViewById(R.id.imageView);
        this.image_icon = findViewById(R.id.image_icon);
        this.upload_Icon = findViewById(R.id.upload_icon);
        this.upload_Text = findViewById(R.id.upload_text);
        this.confirmBtn = findViewById(R.id.confirmBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storageReference;
        //storageReference = storage.getReference();

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

        binding.imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        binding.uploadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        binding.uploadIcon.setOnClickListener(new View.OnClickListener() {
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
                String interest = (String) infoConfirm.get("interests");
                String couponCode = (String) infoConfirm.get("couponCode");
                String discountType = (String) infoConfirm.get("discountType");

                String userUid =  mAuth.getCurrentUser().getUid();
                String couponId = db.collection("coupons")
                        .document()
                        .getId();

                String fileName = uploadPicture();

                Map<String, Object> data = new HashMap<>();
                data.put("CoupName", name);
                data.put("ExpireDate", expireDate);
                data.put("Location", location);
                data.put("Description", description);
                data.put("CouponImage", fileName);
                data.put("UserUid",userUid);
                data.put("Interest", interest);
                data.put("CouponId", couponId);
                data.put("couponCode", couponCode);
                data.put("discountType", discountType);

                db.collection("coupons").
                        add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(AddCouponImage.this, "Coupon added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCouponImage.this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
                            }
                        });



                Coupon coupon = new Coupon(name, expireDate, location, description, fileName, userUid, interest, couponId);
                MainDB.getInstance().getCurUser().get(userUid).addCouponToUser(coupon);

                startActivity(intent);
            }
        });

    }

    public void choosePicture() {

        //mTakePhoto.launch("image/*");

        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            System.out.println("@@@@ dataaa" + imageUri);
            binding.imageIcon.setImageURI(imageUri);
            //uploadPicture();
        }
    }

    private String uploadPicture() {

        //progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Uploading Image...");
        //progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()); //yyyy_MM_dd_HH_mm_ss
        Date now = new Date();
        String fileName = formatter.format(now);
        String userUid =  mAuth.getCurrentUser().getUid();
        fileName = userUid + "_" + fileName;

        System.out.println("@@@fileName: "+fileName);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        binding.imageIcon.setImageURI(null);
                        Toast.makeText(AddCouponImage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        //if(progressDialog.isShowing())
                            //progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //if(progressDialog.isShowing())
                    //progressDialog.dismiss();
                Toast.makeText(AddCouponImage.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
        return fileName;

//        final String randomKey = UUID.randomUUID().toString();
//        //System.out.println("@@@the random key:" + randomKey);
//        String imageUri_String;

        }
}


