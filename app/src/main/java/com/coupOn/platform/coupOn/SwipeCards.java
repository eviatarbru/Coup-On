package com.coupOn.platform.coupOn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Chat.UserChatList;
import com.coupOn.platform.coupOn.Model.Coupon;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.coupOn.platform.coupOn.Notification.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//MainActivity
public class SwipeCards extends AppCompatActivity {

    private Cards cards_data[];
    private static ArrayList<String> likedCoupons = new ArrayList<>();
    private static ArrayList<String> dislikedCoupons = new ArrayList<>();
    public static String tempCouponId;

    //For the chatPart
    private RecyclerView messagesRecycleView;
    ConstraintLayout loading; //Loading screen
    ConstraintLayout swipes; //Main screen

    //private ArrayList<String> al;
    private ArrayAdapterCoupon arrayAdapter;// <String> || ArrayAdapter --> arrayAdapter
    private int i;

    private static volatile boolean isFinished = false;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;
    private ArrayList<Cards> rowItems = new ArrayList<Cards>();

    private Cards firstCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_cards);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.

        loading = findViewById(R.id.loading);
        swipes = findViewById(R.id.swipeScreen);
        loading.setVisibility(View.VISIBLE);
        swipes.setVisibility(View.INVISIBLE);

        if(MainDB.getInstance().getCouponCards() == null || MainDB.getInstance().getCouponCards().isEmpty()) {
            new Thread(new InitDB()).start(); //Making a Thread for the User's Info

            new Thread((new GetChatUsers())).start(); //Chat Thread

            new Thread(new GetCouponsCards()).start();
//
            new Thread(new setUrisToCoupons()).start();
        }
        new Thread(new GetUserFirebaseS(flingContainer)).start(); //Just an example to test the random user info.

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
//            String tempCouponId;
            @Override
            public void removeFirstObjectInAdapter() {
                firstCoupon = MainDB.getInstance().getCouponCards().get(0);
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                tempCouponId = rowItems.get(0).getCouponId();
                MainDB.getInstance().getCouponCards().remove(0);
//                rowItems.remove(0); //changed
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                //Toast.makeText(SwipeCards.this, "Left!", Toast.LENGTH_SHORT).show();
                if(tempCouponId != null){
                    dislikedCoupons.add(SwipeCards.tempCouponId.toString());
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
               // Toast.makeText(SwipeCards.this, "Right!", Toast.LENGTH_SHORT).show();
                if(tempCouponId != null){
                    likedCoupons.add(tempCouponId);
                    addToHisNotifications();
                }
            }

            @Override //required DataSnapShot
            public void onAdapterAboutToEmpty(int itemsInAdapter) { //need to ask for more coupons
                                                                    //OR notify user that there are no coupons to show
                // Ask for more data here
                //Cards item = new Cards( dataSnapshot);
                Cards item = new Cards("id", "No Coupons");// need to be changed based on DB
                rowItems.add(item);// "XML ".concat(String.valueOf(i))
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }


            @Override
            public void onScroll(float scrollProgressPercent) {

            }


        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() { // clicked on the card!!
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Toast.makeText(SwipeCards.this, "Clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), InfoCouponActivity.class);
                String couponName = rowItems.get(0).getCouponName(); //couponName is a value that needs to be fetched from firebase
                Uri imageUri = rowItems.get(0).getUri();
                String ownerId = rowItems.get(0).getOwnerId();
                intent.putExtra("couponName", couponName);
                intent.putExtra("imageUri", imageUri);
                intent.putExtra("ownerId", ownerId);
                startActivity(intent);

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // Using a thread to get the user's info and put it as a Hashmap<String, User> in MainDB
    class InitDB implements Runnable
    {
        @Override
        public void run() {
            MainDB.getInstance();
        }
    }

    //Gets the chat users
    public class GetChatUsers implements Runnable
    {
        @Override
        public void run() {
            User user = null;
            while(user == null) {
                user = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid());
            }
            if(user.getChattingUserUIDs() != null || !user.getChattingUserUIDs().isEmpty())
            {
                for (int i = 0; i < user.getChattingUserUIDs().size(); i++)
                {
                    MainDB.getInstance().getChatUsersInfo(user.getChattingUserUIDs().get(i));
                }
            }
            isFinished = true;

        }
    }


    private void addToHisNotifications(){
        //timestamp for time and notification id
        String timeStamp = ""+System.currentTimeMillis();

        List<String> notifications = new ArrayList<>();


        // Firebase-Firestore
        Map<String, Object> dataEdit = new HashMap<>();
        //data.put("CoupUid", "coupon");
        dataEdit.put("Notifications", notifications);
        DocumentReference updateUser = db.collection("users")
                .document(firstCoupon.getOwnerId());
        updateUser.update("Notifications", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid() + "*" + timeStamp + "*" + firstCoupon.getCouponName() + "*"
                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName()));
    }

    //Gets the offered coupons
    public class GetCouponsCards implements Runnable
    {
        @Override
        public void run() {
            User user = null;
            while(user == null) {
                user = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid());
            }
            MainDB.getInstance().getOfferedCoupons();
        }
    }

    //Sets the uri for each coupon
    public class setUrisToCoupons implements Runnable
    {
        @Override
        public void run() {
            while(!MainDB.getInstance().getFinishedOfferedCoupons()) {  }
            MainDB.getInstance().getUriToOfferedCoupons();
        }
    }

    /*  Steps for Success
        1. new Thread(new GetUserFirebase(uid)).start();
        2. Get user.
     */
    public class GetUserFirebaseS implements Runnable
    {
        SwipeFlingAdapterView flingContainer;


        //This part receives a uid and the flingContainer and gives u the user's Fullname in a card.
        //After we did this, we'll want to use the coupon info, you can do it here.
        public GetUserFirebaseS(SwipeFlingAdapterView flingContainer) {
            this.flingContainer = flingContainer;
        }

            @Override
            public void run() {
                while(!MainDB.getInstance().getFinishedOfferedCouponsImage()) { }
                if(MainDB.getInstance().getCouponCards().isEmpty() || MainDB.getInstance().getCouponCards() == null) {

                    for(int i = 0; i < MainDB.getInstance().getCouponsOffered().size(); i++){

                        if( (((i+1) % 5) == 0 && !MainDB.getInstance().getMatchCoupons().isEmpty()) || MainDB.getInstance().getUnmatchCoupons().isEmpty()){
//                           (We are at the fifth coupon) OR (No low rank coupons to show) --> show high rank coupon

                            Coupon c = MainDB.getInstance().getMatchCoupons().get(0);
                            MainDB.getInstance().getMatchCoupons().remove(0);
                            rowItems.add(new Cards(c.getCouponName(), c.getInterest(), c.getDescription(), c.getExpireDate(), c.getLocation()
                                    , c.getDiscountType(), c.getCouponId(), c.getUri(), c.getOwnerId()));
                        }
                        else{
                            if( (((i+1) % 5) != 0 || MainDB.getInstance().getMatchCoupons().isEmpty()) || MainDB.getInstance().getMatchCoupons().isEmpty()){
//                                (We are NOT at the fifth coupon) OR (No high rank coupons to show) --> show Low rank coupon

                                Coupon c = MainDB.getInstance().getUnmatchCoupons().get(0);
                                MainDB.getInstance().getUnmatchCoupons().remove(0);
                                rowItems.add(new Cards(c.getCouponName(), c.getInterest(), c.getDescription(), c.getExpireDate(), c.getLocation()
                                        , c.getDiscountType(), c.getCouponId(), c.getUri(), c.getOwnerId()));
                            }
                        }
                    }
                    MainDB.getInstance().setCouponCards(rowItems);
                }
                else {
                    isFinished = true;
                    rowItems = MainDB.getInstance().getCouponCards();
                }
                SwipeCards.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        arrayAdapter = new ArrayAdapterCoupon(SwipeCards.this, R.layout.item, rowItems);

                        flingContainer.setAdapter(arrayAdapter);

                        arrayAdapter.notifyDataSetChanged();
                        while(!isFinished){ }
                        loading.setVisibility(View.INVISIBLE);
                        swipes.setVisibility(View.VISIBLE);
                        isFinished = false;
                    }
                });
            }
    }


    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void gotoCouponInfo(View view){  // Identical to onItemClicked, currently not used
        Intent intent = new Intent(this, InfoCouponActivity.class);
        String couponName = rowItems.get(0).getCouponName(); //couponName is a value that needs to be fetched from firebase
        Uri imageUri = rowItems.get(0).getUri();
        intent.putExtra("couponName", couponName);
        intent.putExtra("imageUri", imageUri);
        startActivity(intent);
    }

    public void gotoProfile(View view){
        Intent intent = new Intent(this, Profile_screen.class);
        startActivity(intent);
    }

    public void gotoAddCoupon(View view){
        Intent intent = new Intent(this, addCoupon.class);
        startActivity(intent);
    }

    public void notificationScreen(View view){
        Intent intent = new Intent(this, NotificationsFragment.class);
        startActivity(intent);
    }

    public void chatScreen(View view){
        Intent intent = new Intent(this, UserChatList.class);
        startActivity(intent);
    }
    public void gotoUserCoupons(View view){
        Intent intent = new Intent(this, UserCoupons.class);
        startActivity(intent);
    }

}
