package com.coupOn.platform.coupOn.Notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.coupOn.platform.coupOn.Chat.MessagesList;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;


public class NotificationsFragment extends AppCompatActivity {

    private final ArrayList<String> notificationLists = new ArrayList<>();

    //private String userID;
    // recycler view
    RecyclerView notificationRv;
    AdapterNotification adapterNotify;

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private final DatabaseReference databaseReference = database.getReference();

    // Firebase-Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //DocumentReference docRef = db.collection("users")
    //       .document(userID);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("check number 0");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);
        System.out.println("check number 1");
        mAuth = FirebaseAuth.getInstance();

        notificationRv = findViewById(R.id.notificationRv);
        System.out.println("check number 2");
        notificationRv.setHasFixedSize(true);
        System.out.println("check number 3");
        notificationRv.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("check number 4");
        adapterNotify = new AdapterNotification(this, notificationLists);
        System.out.println("check number 5");

        notificationRv.setAdapter(adapterNotify);
        System.out.println("check number 6");

        if(MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getNotifications() != null || !MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getNotifications().isEmpty()){
            ArrayList<String> notifications = MainDB.getInstance().getCurUser().
                    get(mAuth.getCurrentUser().getUid()).getNotifications();
            adapterNotify.updateData(notifications);
        }
        else{
            adapterNotify.updateData(null);
        }
    }
}