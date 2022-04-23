package com.coupOn.platform.coupOn.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.coupOn.platform.coupOn.SwipeCards;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;


public class NotificationsFragment extends AppCompatActivity {

    private final ArrayList<String> notificationLists = new ArrayList<>();

    RecyclerView notificationRv;
    AdapterNotification adapterNotify;

    private FirebaseAuth mAuth;

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

    public void mainMenu(View view)
    {
        Intent intent = new Intent(NotificationsFragment.this, SwipeCards.class);
        startActivity(intent);
    }
}