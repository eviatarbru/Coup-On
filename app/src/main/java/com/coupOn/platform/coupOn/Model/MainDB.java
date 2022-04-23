package com.coupOn.platform.coupOn.Model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coupOn.platform.coupOn.Cards;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    private ArrayList<Cards> couponCards = new ArrayList<Cards>();      //to save the cards
    private ArrayList<Coupon> matchCoupons = new ArrayList<Coupon>();   //1 in 5 coupons that are good (20)
    private ArrayList<Coupon> unmatchCoupons = new ArrayList<Coupon>(); //4 in 5 coupons that are not very good (80)

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
                String date = value.getString("DateOfBirth");
                List<String> chatUsers = (List<String>) value.get("ChatUsers");
                ArrayList<String> chatUsers1 = new ArrayList<>();
                if(chatUsers != null)
                    chatUsers1.addAll(chatUsers);
                List<String> interests = (List<String>) value.get("Interests");
                ArrayList<String> interests1 = new ArrayList<>();
                if(interests != null)
                    interests1.addAll(interests);
                List<String> notifications = (List<String>) value.get("Notifications");
                ArrayList<String> notifications1 = new ArrayList<>();
                if(notifications != null)
                    notifications1.addAll(notifications);
                boolean chatB = (!chatUsers1.isEmpty() || chatUsers1 != null);
                boolean interestsB = (!interests.isEmpty() || interests != null);
                boolean notificationsB = (!notifications1.isEmpty() || notifications1 != null);
                if(chatB) //Check Chats
                {
                    if (interestsB) //Check Interests
                    {
                        if(notificationsB) // true - chat, interests and notifications.
                            curUser.put(uid, new User(email, fullName, date, chatUsers1, interests1, notifications1)); //Get from currentUser the Email and the FullName from the fireStore.
                        else // true - chat and interests / False - notifications.
                            curUser.put(uid, new User(email, fullName, date,  chatUsers1, interests1)); //Get from currentUser the Email and the FullName from the fireStore.
                    }
                    else
                    if(notificationsB) // true - chat and notifications / False - interests.
                        curUser.put(uid, new User(email, fullName, date,  chatUsers1, null, notifications1)); //Get from currentUser the Email and the FullName from the fireStore.
                    else // true - chat / False - interests and notifications.
                        curUser.put(uid, new User(email, fullName, date,  chatUsers1)); //Get from currentUser the Email and the FullName from the fireStore.
                }
                else {  //No Chats
                    if (interestsB) //True - Interests / False - Chats
                    {
                        if(notificationsB) //True - Interests and Notifications / False - Chats
                            curUser.put(uid, new User(email, fullName, date,  null, interests1 , notifications1)); //Get from currentUser the Email and the FullName from the fireStore.
                        else //True - Interests / False - Chats and Notifications
                            curUser.put(uid, new User(email, fullName, date,  null, interests1)); //Get from currentUser the Email and the FullName from the fireStore.
                    }
                    else //False - Chats and Interests
                    {
                        if(notificationsB) //True - Notifications / False - Chats and Interests
                            curUser.put(uid, new User(email, fullName, date,  null, null, notifications1)); //Get from currentUser the Email and the FullName from the fireStore.
                        else //False - Chats, Interests and Notifications
                            curUser.put(uid, new User(email, fullName, date)); //Get from currentUser the Email and the FullName from the fireStore.
                    }
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
                                    , document.getString("DiscountType"), document.getString("CouponCode")
                                    , document.getLong("Rank").intValue());
                            if(!document.getString("UserUid").equals(mAuth.getCurrentUser().getUid()))
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
                        if(couponsOffered.get(finalI).getRank() > 3)
                            matchCoupons.add(couponsOffered.get(finalI));
                        else
                            unmatchCoupons.add(couponsOffered.get(finalI));

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
        DocumentReference dr = users.collection("users").document(uid1); //This is how u retrieve data from fireStore.
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email = value.getString("Email");
                String fullName = value.getString("FullName");
                String date = value.getString("DateOfBirth");
                chattingUsers.put(uid1, new User(email, fullName, date));//Get from currentUser the Email and the FullName from the fireStore.
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

    public ArrayList<Coupon> getMatchCoupons() {
        return matchCoupons;
    }

    public void setMatchCoupons(ArrayList<Coupon> matchCoupons) {
        this.matchCoupons = matchCoupons;
    }

    public ArrayList<Coupon> getUnmatchCoupons() {
        return unmatchCoupons;
    }

    public void setUnmatchCoupons(ArrayList<Coupon> unmatchCoupons) {
        this.unmatchCoupons = unmatchCoupons;
    }
}

