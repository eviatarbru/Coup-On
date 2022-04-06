package com.coupOn.platform.coupOn.Chat;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesList
{
    private String name, email, lastMessage, chatKey, mobile;
    private int unseenMessages;

    public MessagesList(String name, String email, String lastMessages, int unseenMessages, String chatKey, String mobile)
    {
        this.name = name;
        this.email = email;
        this.lastMessage = lastMessages;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MessagesList{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", chatKey='" + chatKey + '\'' +
                ", mobile='" + mobile + '\'' +
                ", unseenMessages=" + unseenMessages +
                '}';
    }
}
