package com.coupOn.platform.coupOn.Notification;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {

    private Context context;
    private ArrayList<String> notificationsList;

    public AdapterNotification(Context context, ArrayList<String> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();

    @NonNull
    @Override
    public AdapterNotification.HolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view row_notification

        View view = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);

        return new HolderNotification(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNotification holder, int position) {
        //get and set data to views

        //get data
        String info[] = this.notificationsList.get(position).split("\\*");
        String uID = info[0];
        String timestamp = info[1];
        String coupName = info[2];
        String myID = info[3];
        String couponId = info[4];

        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", Long.parseLong(timestamp)).toString();

        //set to views
        holder.nameTv.setText(myID);
        holder.timeTv.setText(pTime);
        holder.notificationTv.setText("Liked your coupon " + coupName);

        holder.deleteBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                HashMap<String, String> delNotify = new HashMap<>();
                delNotify.put("Notifications", notificationsList.get(holder.getAdapterPosition()));
                Toast.makeText(view.getContext(), "Notification Deleted!!", Toast.LENGTH_SHORT).show();
                notificationsList.remove(holder.getAdapterPosition());
                updateData(notificationsList);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).
                        update("Notifications", FieldValue.arrayRemove(delNotify.get("Notifications")));
            }
        });

        holder.acceptBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                addToRealtime(holder.getAdapterPosition(), uID, couponId);
                DocumentReference updateUser = db.collection("users")
                        .document(mAuth.getCurrentUser().getUid());
                updateUser.update("ChatUsers", FieldValue.arrayUnion(uID));

                DocumentReference updateUser1 = db.collection("users")
                        .document(uID);
                updateUser1.update("ChatUsers", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
                HashMap<String, String> delNotify = new HashMap<>();
                delNotify.put("Notifications", notificationsList.get(holder.getAdapterPosition()));
                notificationsList.remove(holder.getAdapterPosition());
                updateData(notificationsList);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).
                        update("Notifications", FieldValue.arrayRemove(delNotify.get("Notifications")));
            }
        });
    }

    public void updateData(ArrayList<String> notificationsList1)
    {
        notificationsList = notificationsList1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    // holder class for views for row_notification.xml
    class HolderNotification extends RecyclerView.ViewHolder{

        // declare views
        private AdapterNotification adapter;
        TextView nameTv, notificationTv, timeTv;
        Button deleteBut, acceptBut;
        private LinearLayout currentRow;

        public HolderNotification(@NonNull View itemView) {
            super(itemView);

            // init views
            currentRow = itemView.findViewById(R.id.currentRow);
            nameTv = itemView.findViewById(R.id.nameTv);
            notificationTv = itemView.findViewById(R.id.notificationTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            deleteBut = itemView.findViewById(R.id.button_delete);
            acceptBut = itemView.findViewById(R.id.button_accept);
        }
    }
    public void addToRealtime(int position, String user2, String couponId)
    {
        final int[] chatKey = {0};
        final int[] maxKey = {0};
        final boolean checker[] = {false};
        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int getChatCounts = (int) snapshot.getChildrenCount();
                if (getChatCounts > 0) {
                    for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                        final String getKey = dataSnapshot2.getKey();
                        chatKey[0] = Integer.parseInt(getKey);
                        maxKey[0] = Math.max(maxKey[0], chatKey[0]);
                        final String getUserOne = dataSnapshot2.child("user_1").getValue(String.class); //User 1
                        final String getUserTwo = dataSnapshot2.child("user_2").getValue(String.class); //User 2
                        if ((getUserOne.equals(mAuth.getCurrentUser().getUid()) && getUserTwo.equals(user2)) || (getUserOne.equals(user2) && getUserTwo.equals(mAuth.getCurrentUser().getUid())))
                            checker[0] = true;
                        System.out.println(maxKey[0] + " this is the chatKey (UserChatList)");
                    }
                }
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                if(!checker[0]) {
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("user_1").setValue(mAuth.getCurrentUser().getUid());
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("user_2").setValue(user2);
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("messages").child(currentTimestamp).child("msg").setValue("Hello i accepted your request!");
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("messages").child(currentTimestamp).child("mobile").setValue(mAuth.getCurrentUser().getUid());
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("coupons").setValue(couponId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}