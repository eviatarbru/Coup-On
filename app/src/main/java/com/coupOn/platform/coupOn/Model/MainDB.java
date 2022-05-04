package com.coupOn.platform.coupOn.Model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coupOn.platform.coupOn.Cards;
import com.coupOn.platform.coupOn.Chat.MemoryData;
import com.coupOn.platform.coupOn.Chat.MessagesList;
import com.coupOn.platform.coupOn.Chat.UserChatList;
import com.coupOn.platform.coupOn.SwipeCards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();

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
        setCoupointsRealtime();
        onChangeCoupoints();
    }

    //Gets the coupoint value
    public void setCoupointsRealtime()
    {
        try {

            this.databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("coupoints").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        System.out.println("there is a problem");
                    }
                    else {
                        String coupoints = String.valueOf(task.getResult().getValue());
                        curUser.get(mAuth.getCurrentUser().getUid()).setCoupoints(Integer.parseInt(coupoints));
                        System.out.println(coupoints + " this is the coupoints");
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void onChangeCoupoints()
    {
        try {

            this.databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    System.out.println(snapshot.getValue() + " this is the value");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    System.out.println(snapshot.getValue() + " this is the value");
                    curUser.get(mAuth.getCurrentUser().getUid()).setCoupoints(Integer.parseInt(snapshot.getValue() + ""));
                    SwipeCards.changeCoupoints(snapshot.getValue() + "");
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
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
                        {
                            Coupon c = new Coupon(document.getString("CouponImage"), document.getString("CoupName")
                                    , document.getString("ExpireDate"), document.getString("Location")
                                    , document.getString("Description"), document.getString("UserUid")
                                    , document.getString("CouponId"), document.getString("Interest")
                                    , document.getString("DiscountType"), document.getString("CouponCode")
                                    , document.getLong("Rank").intValue()
                                    , document.getLong("Price").intValue());
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

    public void NotificationClear(ArrayList<String> expiredCouponsId) {
        // get users's notifications arrays then go over each array and delete the item that contain an id from expiredCoupons

        users.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {     //gets users


                            ArrayList<String> userNotifications = new ArrayList<String>();
                            userNotifications = (ArrayList) document.get("Notifications");  //holds all the notifications for that user

                            if (userNotifications != null) {
                                for (String notification : userNotifications) {             //go over every notification
                                    for (String cid : expiredCouponsId){                      //check if an expired coupon is in there
                                        if(notification.contains("*" + cid + "*")){
                                            users.collection("users").document(document.getString("Uid")).
                                                    update("Notifications", FieldValue.arrayRemove(notification));
                                        }
                                    }
                                }
                            }

                        }   //end for

                    }
                });         //end on success
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

