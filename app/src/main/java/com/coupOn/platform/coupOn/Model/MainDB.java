package com.coupOn.platform.coupOn.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coupOn.platform.coupOn.Cards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.grpc.internal.JsonUtil;

public class MainDB
{
    private static MainDB instance;

    public static MainDB getInstance() {
        if(instance == null)
            instance = new MainDB();
        return instance;
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore users = FirebaseFirestore.getInstance();
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private final HashMap<String, User> curUser = new HashMap<>();
    private final HashMap<String, User> chattingUsers = new HashMap<>();
    private final ArrayList<Coupon> couponsOffered = new ArrayList<>();

    private ArrayList<Cards> couponCards = new ArrayList<Cards>(); //to save the cards

    private boolean finishedOfferedCoupons = false;

    private boolean finishedOfferedCouponsImage = false;


    private MainDB()
    {
        currentUser();
    }

    private void currentUser()
    {
        //Retrieve Current User
        users = FirebaseFirestore.getInstance(); //Connects to fireStore.
        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
        DocumentReference dr = users.collection("users").document(uid); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                List<String> chatUsers = (List<String>) value.get("ChatUsers");
                ArrayList<String> chatUsers1 = new ArrayList<>();
                if(chatUsers != null)
                    chatUsers1.addAll(chatUsers);
                List<String> interests = (List<String>) value.get("Interests");
                ArrayList<String> interests1 = new ArrayList<>(interests);
                if(!interests.isEmpty() || interests != null)
//                    curUser.get(mAuth.getCurrentUser().getUid()).setInterests(interests1);
                if(!chatUsers1.isEmpty() || chatUsers1 != null) {
                    if (!interests.isEmpty() || interests != null)
                        curUser.put(uid, new User(email, fullName, chatUsers1, interests1)); //Get from currentUser the Email and the FullName from the fireStore.
                    else
                        curUser.put(uid, new User(email, fullName, chatUsers1)); //Get from currentUser the Email and the FullName from the fireStore.
                }
                else {
                    if (!interests.isEmpty() || interests != null)
                        curUser.put(uid, new User(email, fullName, null, interests1)); //Get from currentUser the Email and the FullName from the fireStore.
                    else
                        curUser.put(uid, new User(email, fullName)); //Get from currentUser the Email and the FullName from the fireStore.
                }
            }
        });
    }

    public void getOfferedCoupons()
    {
        final int[] counter = {0};
        int sizeInterests = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getInterests().size();
        CollectionReference couponsRef = users.collection("coupons");
        for(String interest: MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getInterests()) {
            Query query = couponsRef.whereEqualTo("Interest", interest);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot document: task.getResult())
                        { //(String couponImage, String couponName, String expireDate, String location, String description, String ownerId, String couponId, String interest
                            Coupon c = new Coupon(document.getString("CouponImage"), document.getString("CoupName")
                                    , document.getString("ExpireDate"), document.getString("Location")
                                    , document.getString("Description"), document.getString("UserUid")
                                    , document.getString("CouponId"), document.getString("Interest")
                                    , document.getString("DiscountType"), document.getString("CouponCode"));
                            couponsOffered.add(c);
                        }
                        counter[0]++;
                        if(counter[0] >= sizeInterests - 1)
                            finishedOfferedCoupons = true;
                    }
                    else
                    {
                        System.out.println("Check log! (this is a fail)");
                        counter[0]++;
                        if(counter[0] >= sizeInterests - 1)
                            finishedOfferedCoupons = true;
                    }
                }
            });
        }
    }

    public boolean getFinishedOfferedCoupons()
    {
        return this.finishedOfferedCoupons;
    }

    public void getUriToOfferedCoupons() {
        final int[] counter = {0};
        if(this.couponsOffered.size() == 0)
        {
            finishedOfferedCouponsImage = true;
            return;
        }
        for(int i = 0; i < this.couponsOffered.size(); i++)
        {
            mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + couponsOffered.get(i).getCouponImage());
            try {
                int finalI = i;
                mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        couponsOffered.get(finalI).setUri(task.getResult());
                        counter[0]++;
                        if((counter[0] >= couponsOffered.size())) {
                            finishedOfferedCouponsImage = true;
                        }
                    }
                });
            } catch (Exception e) {
                System.out.println("this is an error with the uri (MainDB)");
            }
        }
    }

    public boolean getFinishedOfferedCouponsImage()
    {
        return this.finishedOfferedCouponsImage;
    }

    public void getChatUsersInfo(String uid1)
    {
        final User[] userInfo = new User[1]; //Firebase wants to change the User to Final when retrieving the data.
        User user;
        System.out.println(uid1);

        DocumentReference dr = users.collection("users").document(uid1); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                chattingUsers.put(uid1, new User(email, fullName));//Get from currentUser the Email and the FullName from the fireStore.
            }
        });
    }

    public ArrayList<Cards> getCouponCards() {
        return couponCards;
    }

    public void setCouponCards(ArrayList<Cards> couponCards) {
        this.couponCards = couponCards;
    }

    public HashMap<String, User> getCurUser() {
        return curUser;
    }

    public HashMap<String, User> getChattingUsers() {
        return chattingUsers;
    }

    public Set<String> getChattingUIDS()
    {
        return chattingUsers.keySet();
    }

    public ArrayList<Coupon> getCouponsOffered() {
        return couponsOffered;
    }

}

