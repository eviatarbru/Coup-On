package com.coupOn.platform.coupOn.ChatMessage;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Chat.MemoryData;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.platform.coup_on.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatScreen extends AppCompatActivity {

//    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://coup-on-project1-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final List<MessageChatList> chatLists = new ArrayList<>();
    private String chatKey;
    String getUserMobile = "";
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    private String getName;

    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.

        final ImageView backBtn = findViewById(R.id.back_btn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditText);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        //get data from messages adapter class
        final String getName = getIntent().getStringExtra("name");
        chatKey = getIntent().getStringExtra("chatKey");
        final String getMobile = getIntent().getStringExtra("mobile");
        this.getName = getName;

        getUserMobile = MainDB.getInstance().getCurUser().keySet().toString();

        //get User Mobile From Memory
        nameTV.setText(getName);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(ChatScreen.this));

        chatAdapter = new ChatAdapter(chatLists, ChatScreen.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        final int[] counter = {0};
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(chatKey.isEmpty()) {
                    //generate chat key. by defult chatKey is 1.
                    chatKey = "1";

                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }
                if(snapshot.hasChild("chat"))
                {
                    chatLists.clear();

                    if(snapshot.child("chat").child(chatKey).hasChild("messages"))
                    {
                        for(DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren())
                        {
                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("mobile"))
                            {
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getMobile = messagesSnapshot.child("mobile").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

//                                try {
//                                    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(messageTimestamps);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }

                                System.out.println(messageTimestamps + "ido witman");

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm", Locale.CANADA);

                                MessageChatList chatList = new MessageChatList(getMobile, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));

                                chatLists.add(chatList);

                                if(loadingFirstTime ||  Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsg(ChatScreen.this, chatKey)))
                                {
                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, ChatScreen.this);
                                    chatAdapter.updateChatLists(chatLists);

                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getTxtMessage = messageEditText.getText().toString();

                if(getTxtMessage != "") {
                    //get current timestamp
                    final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                    MemoryData.saveLastMsgTS(currentTimestamp, chatKey, ChatScreen.this);
                    databaseReference.child("chat").child(chatKey).child("user_1").setValue(uid);
                    databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                    databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                    databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(uid);

                    // clear edit text
                    messageEditText.setText("");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}