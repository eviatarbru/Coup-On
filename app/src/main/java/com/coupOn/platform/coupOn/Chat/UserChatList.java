package com.coupOn.platform.coupOn.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

public class UserChatList extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();

    private boolean dataset = false;

    private String email;
    private String fullname;
    private RecyclerView messagesRecycleView;
    private MessagesAdapter messagesAdapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    private int unseenMessages = 0;
    private String lastMessage = "";

    private String chatKey = "";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore users = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_chat);

        //get data from messages adapter class
        email = getIntent().getStringExtra("email");
        chatKey = getIntent().getStringExtra("chatKey");
        final String getMobile = getIntent().getStringExtra("mobile");

        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.

        messagesRecycleView = findViewById(R.id.messagesRecyclerView);
        //System.out.println("Hello");
        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        //System.out.println("Hello");
        //set adapter to recyclerview
        messagesAdapter = new MessagesAdapter(messagesLists, UserChatList.this);

        messagesRecycleView.setAdapter(messagesAdapter);
        //System.out.println("Hello");
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";
                //System.out.println("Hello");
                final ArrayList<User> usersChat = new ArrayList<>();
                usersChat.addAll(MainDB.getInstance().getChattingUsers().values());
                for( DataSnapshot dataSnapshot: snapshot.child("chatUser").getChildren())
                {
                    final String getUid = dataSnapshot.getKey();

                    dataset = false;

                    if(getUid.equals(mAuth.getUid()))
                    {
                        for( DataSnapshot dataSnapshot1: snapshot.child("chatUser").child(mAuth.getUid()).getChildren())
                        {
                            final String uidUser = dataSnapshot1.getKey();

                            databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int getChatCounts = (int)snapshot.getChildrenCount();

                                    if(getChatCounts > 0)
                                    {
                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                                        {
                                            final String getKey = dataSnapshot2.getKey();
                                            chatKey = getKey;

                                            if(dataSnapshot2.hasChild("user_1") && dataSnapshot2.hasChild("user_2") && dataSnapshot2.hasChild("messages"))
                                            {
                                                final String getUserOne = dataSnapshot2.child("user_1").getValue(String.class);
                                                final String getUserTwo = dataSnapshot2.child("user_2").getValue(String.class);

                                                if((getUserOne.equals(getUid) && getUserTwo.equals(uidUser)) || (getUserOne.equals(uidUser) && getUserTwo.equals(getUid)))
                                                {
                                                    for(DataSnapshot chatDataSnapshot : dataSnapshot2.child("messages").getChildren())
                                                    {
                                                        final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());

                                                        final long getLatSeenMessage = Long.parseLong(MemoryData.getLastMsg(UserChatList.this, getKey));

                                                        lastMessage = chatDataSnapshot.child("msg").getValue((String.class));
                                                        if(getMessageKey > getLatSeenMessage)
                                                        {
                                                            unseenMessages++;
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(!dataset)
                                    {
                                        dataset = true;
                                        for(int i = 0; i < usersChat.size(); i++)
                                        {
                                            MessagesList messagesList = new MessagesList(usersChat.get(i).getFullName(), usersChat.get(i).getEmail(), lastMessage, unseenMessages, chatKey, getUid);
                                            messagesLists.add(messagesList);
                                            messagesAdapter.updateData(messagesLists);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}