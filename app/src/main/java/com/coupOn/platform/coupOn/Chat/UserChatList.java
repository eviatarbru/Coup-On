package com.coupOn.platform.coupOn.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.platform.coup_on.R;

public class UserChatList extends AppCompatActivity {

    private String email;
    private String fullname;
    private RecyclerView messagesRecycleView;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_chat);

        messagesRecycleView = findViewById(R.id.messagesRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        this.email = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getEmail();
        this.fullname = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName();

        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}