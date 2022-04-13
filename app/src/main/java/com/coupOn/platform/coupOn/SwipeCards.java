package com.coupOn.platform.coupOn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Chat.UserChatList;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

//MainActivity
public class SwipeCards extends AppCompatActivity {

    private Cards cards_data[];

    //For the chatPart
    private RecyclerView messagesRecycleView;
    private ImageView chat_Icon;
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

    ListView listView;
    private ArrayList<Cards> rowItems = new ArrayList<Cards>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("This is the Create of it (Swipe Card)");
        setContentView(R.layout.activity_swipe_cards);
        System.out.println("onCreate");
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        this.chat_Icon = findViewById(R.id.chat_icon);

        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.

        loading = findViewById(R.id.loading);
        swipes = findViewById(R.id.swipeScreen);
        loading.setVisibility(View.VISIBLE);
        swipes.setVisibility(View.INVISIBLE);
        Cards item = new Cards("id", "Coupon 1");
        Cards item2 = new Cards("id", "Coupon 2");
        Cards item3 = new Cards("id", "Coupon 3");
        Cards item4 = new Cards("id", "Coupon 4");
        Cards item5 = new Cards("id", "Coupon 5");
        rowItems.add(item);
        rowItems.add(item2);
        rowItems.add(item3);
        rowItems.add(item4);
        rowItems.add(item5);

        new Thread(new InitDB()).start(); //Making a Thread for the User's Info

        new Thread((new GetChatUsers())).start(); //Chat Thread
//        String uidU = MainDB.getInstance().getCurUser().keySet().toString(); //For the testing
//        uidU = uidU.substring(1, uidU.length()-1); //For the testing
        new Thread(new GetUserFirebaseS(uid, flingContainer)).start(); //Just an example to test the random user info.


        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0); //changed
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(SwipeCards.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(SwipeCards.this, "Right!", Toast.LENGTH_SHORT).show();

                user = FirebaseAuth.getInstance().getCurrentUser();
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

                                    }
                                    if (emailStr != null) {

                                    } else {
                                        Toast.makeText(SwipeCards.this, "Failed to get user from FB", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }

            @Override //required DataSnapShot
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
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
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Toast.makeText(SwipeCards.this, "Clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), InfoCouponActivity.class);
                startActivity(intent);
            }
        });

        chat_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatScreen(view);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("This is the start of it (Swipe Card)");
    }

    // Using a thread to get the user's info and put it as a Hashmap<String, User> in MainDB
    class InitDB implements Runnable
    {

        @Override
        public void run() {
            MainDB.getInstance();
        }
    }

    /*  Steps for Success
        1. new Thread(new GetUserFirebase(uid)).start();
        2. Get user.
     */
    public class GetUserFirebaseS implements Runnable
    {
        User userFB;
        String uidFB;
        //        ArrayList<Cards> rowItems;
        SwipeFlingAdapterView flingContainer;


        //This part receives a uid and the flingContainer and gives u the user's Fullname in a card.
        //After we did this, we'll want to use the coupon info, you can do it here.
        public GetUserFirebaseS(String uidFB, SwipeFlingAdapterView flingContainer) {
            this.uidFB = uidFB;
            this.flingContainer = flingContainer;
        }

        @Override
        public void run() {
            userFB = MainDB.getInstance().getUserFirebase(uidFB);
            User user1 = userFB;

            SwipeCards.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // your stuff to update the UI
                    rowItems.add(new Cards(user1.getFullName(), user1.getFullName()));
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

    public class GetChatUsers implements Runnable
    {
        @Override
        public void run() {
            User user = null;
            while(user == null) {
                user = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid());
            }
            if(user.getChattingUserUIDs() != null)
            {
                for (int i = 0; i < user.getChattingUserUIDs().size(); i++)
                {
                    MainDB.getInstance().getChatUsersInfo(user.getChattingUserUIDs().get(i));
                }
            }
            isFinished = true;
        }
    }


    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void gotoCouponInfo(View view){
        Intent intent = new Intent(this, InfoCouponActivity.class);
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

    public void chatScreen(View view){
        Intent intent = new Intent(this, UserChatList.class);
        startActivity(intent);
//        finish();
    }
    public void gotoUserCoupons(View view){
        Intent intent = new Intent(this, UserCoupons.class);
        startActivity(intent);
    }

}