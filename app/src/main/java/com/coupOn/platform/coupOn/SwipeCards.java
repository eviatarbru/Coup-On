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

    //private ArrayList<String> al;
    private ArrayAdapterCoupon arrayAdapter;// <String> || ArrayAdapter --> arrayAdapter
    private int i;

    //firebase
    private FirebaseUser user;
    private String userID;

    ListView listView;
    private ArrayList<Cards> rowItems = new ArrayList<Cards>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_cards);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        this.chat_Icon = findViewById(R.id.chat_icon);
//        MainDB.getInstance();
        new Thread(new InitDB()).start();
//        String uidU = MainDB.getInstance().getCurUser().keySet().toString();
//        uidU = uidU.substring(1, uidU.length()-1);
        new Thread(new GetUserFirebaseS("9K7MPR33qzN4gpO4Sp0onzRUmJG2", flingContainer)).start();
//        System.out.println("this is the best user: " + user1);

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
                Toast.makeText(SwipeCards.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        chat_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatScreen(view);
            }
        });

    }

    class InitDB implements Runnable
    {

        @Override
        public void run() {
            MainDB.getInstance();
            System.out.println(MainDB.getInstance().getCurUser());
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


        //This is the part good part, we take a user and print it
        public GetUserFirebaseS(String uidFB, SwipeFlingAdapterView flingContainer) {
            this.uidFB = uidFB;
//            this.rowItems = rowItems;
            this.flingContainer = flingContainer;
        }

        @Override
        public void run() {
            userFB = MainDB.getInstance().getUserFirebase(uidFB);
            System.out.println(userFB + " bYLE");
            User user1 = userFB;

            SwipeCards.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // your stuff to update the UI
                    rowItems.add(new Cards(user1.getFullName(), user1.getFullName()));
                    arrayAdapter = new ArrayAdapterCoupon(SwipeCards.this, R.layout.item, rowItems);

                    flingContainer.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                    System.out.println("BYLE2.0");
                }
            });

        }

        public User getUserFB() {
            return userFB;
        }
    }


    /*public void checkUser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString());
                    rowItems.add(item);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
    }*/

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
    public void gotoprofile(View view){
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
    }

}