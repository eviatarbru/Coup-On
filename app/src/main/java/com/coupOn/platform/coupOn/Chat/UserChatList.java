package com.coupOn.platform.coupOn.Chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

public class UserChatList extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();

    private String email;
    private String fullname;
    private RecyclerView messagesRecycleView;

    private FirebaseFirestore users = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_chat);

        messagesRecycleView = findViewById(R.id.messagesRecyclerView);
        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(this));

        //Get User List
        this.email = MainDB.getInstance().getCurUser().get(0).getEmail();
        this.fullname = MainDB.getInstance().getCurUser().get(0).getFullName();
        for(User user: MainDB.getInstance().getChattingUsers().values())
        {
            MessagesList messagesList = new MessagesList(user.getFullName(), user.getEmail(), "", 0);
            this.messagesLists.add(messagesList);
        }
        messagesRecycleView.setAdapter(new MessagesAdapter(messagesLists, UserChatList.this));


    }

}