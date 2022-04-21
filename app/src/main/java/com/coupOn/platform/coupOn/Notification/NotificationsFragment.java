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

    private String userID;
    // recycler view
    RecyclerView notificationRv;
    AdapterNotification adapterNotify;

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();

    // Firebase-Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("users")
            .document(userID);


    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);

        mAuth = FirebaseAuth.getInstance();

        notificationRv = findViewById(R.id.notificationRv);
        notificationRv.setHasFixedSize(true);
        notificationRv.setLayoutManager(new LinearLayoutManager(this));
        adapterNotify = new AdapterNotification(this, notificationLists);

        notificationRv.setAdapter(adapterNotify);

        try{
            this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(int i = 0; i < notificationLists.size(); i++){
                        /*dataset = true;
                        User user = MainDB.getInstance().getChattingUsers().get(uidUser);
                        MessagesList messagesList = new MessagesList(user.getFullName(), user.getEmail(), lastMessage, unseenMessages, rightKey, uidUser);
                        messagesLists.add(messagesList);
                        messagesAdapter.updateData(messagesLists);*/
                    }
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
}