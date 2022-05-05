package com.coupOn.platform.coupOn.Notification;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.platform.coup_on.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {

    private Context context;
    private ArrayList<String> notificationsList;

    public AdapterNotification(Context context, ArrayList<String> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean chatCreater = false;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

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
        System.out.println(this.notificationsList + " this is the");
        //get data
        String info[] = this.notificationsList.get(position).split("\\*");
        System.out.println(info + "this is the");
        int state = Integer.parseInt(info[0]);
        String SenderName = info[1];
        String timestamp = info[2];
        String coupName = info[3];
        String myName = info[4];
        String couponId = info[5];
        String offer = info[6];
        int numOffer = Integer.parseInt(info[6]);

        int myCoupoints = MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getCoupoints();


        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", Long.parseLong(timestamp)).toString();
        //set to views
        holder.nameTv.setText(myName);
        holder.timeTv.setText(pTime);
        String sourceString = "";
        switch(state) {
            case 1:
                sourceString = "Offered " + "</b>" + offer + " Coupoints " + "</b> " + "for your coupon: " + "</b>" + "\"" + coupName + "\"" + "</b>";
                break;
            case 2:
                sourceString = "Didn't have enough money to pay for your coupon: " + coupName + "</b> Would you like to open a chat with him? The offer was: " + "</b>" + offer + " Coupoints ";
                break;
            case 3:
                sourceString = "[Updated] Offered: " + "</b>" + offer + " Coupoints " + "</b> " + "for your coupon: " + "</b>" + "\"" + coupName + "\"" + "</b>";
                break;
            case 4:
                sourceString = "[Deleted] Your coupon: " + coupName + " </b> has been deleted because it got expired!";
                break;
            case 5:
                sourceString = "[Succeeded] The coupon: " + coupName + "</b> was exchanged successfully! Price: " + offer + " coupoints! </b> You can view your coupon in your Coupon List!";
                break;
        }
        holder.notificationTv.setText(Html.fromHtml(sourceString));

//        holder.notificationTv.setText("Offered " +"Coupoints for your coupon: " + "\"" + coupName + "\"");

        db.collection("coupons")
                .document(couponId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imageName = documentSnapshot.getString("CouponImage");
                        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageName);
                        try {
                            mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                        Uri uri = task.getResult();
                                        Glide.with(context)
                                                .load(uri) // the uri you got from Firebase
                                                .into(holder.imageView); //Your imageView variable
                                }
                            });
                        } catch (Exception e) {
                            System.out.println("this is an error with the uri, Exception: " + e);
                        }
                    }
                });

        holder.deleteBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                switch (state)
                {
                    case 1:
                        break;
                    case 2:
                        removeRealtime(holder.getAdapterPosition(), SenderName, couponId);
                        break;
                }


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
            String timestampNow = ""+System.currentTimeMillis();
            @Override
            public void onClick(View view) {
                switch(state)
                {
                    case 1: //TODO Check coupoints + if not enough -> send a new notification with a chat option. else -> reduce coupoints and send coupon.
                        databaseReference.child("Users").child(SenderName).child("coupoints").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    System.out.println("there is a problem");
                                }
                                else {
                                    String coupoints = String.valueOf(task.getResult().getValue());
                                    int coupoint = Integer.parseInt(coupoints);
                                    if(numOffer <= coupoint) //Checks The other user's Coupoints
                                    {
                                        databaseReference.child("Users").child(SenderName).child("coupoints").setValue(coupoint - numOffer);
                                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("coupoints").setValue(myCoupoints + numOffer);
                                        addUserNotification("5" + "*" + mAuth.getCurrentUser().getUid() + "*" + timestampNow + "*" + coupName + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*" +couponId
                                                + "*" + offer, SenderName);
                                        ArrayList <String> notificationClearCouponId = new ArrayList<>();
                                        notificationClearCouponId.add(couponId);
                                        MainDB.getInstance().NotificationClear(notificationClearCouponId);
                                        Map<String, Object> dataEdit = new HashMap<>();
                                        dataEdit.put("UserUid", SenderName);
                                        dataEdit.put("Tradable", false);
                                        DocumentReference updateUser = db.collection("coupons")
                                                .document(couponId);
                                        updateUser.update(dataEdit);
                                    }
                                    else // If the other user doesnt have enough money to pay for the coupon.
                                    {
                                        //Message for the one giving the offer that he doesnt have enough and if he would like to open a chat with him.
                                        addUserNotification("2" + "*" + mAuth.getCurrentUser().getUid() + "*" + timestampNow + "*" + coupName + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*" +couponId
                                                + "*" + offer, SenderName);
                                        //Message for the owner of the coupon, if he would like to open a chat with him!
                                        addUserNotification("2" + "*" + SenderName + "*" + timestampNow + "*" + coupName + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*" +couponId
                                                + "*" + offer, mAuth.getCurrentUser().getUid());
                                    }
                                    System.out.println(coupoints + " this is the coupoints");
                                }
                            }
                        });
                        break;
                    case 2: //TODO Notification That asks the user to open a chat, if the other user didn't have enough coupoints.
                        addToRealtimeAndFirestore(holder.getAdapterPosition(), SenderName, couponId); //Checks if it can open a chat (Both pressed yes), otherwise, it will add a one to their chat and if one of them pressed no we will delete the chat.
                        break;
                    case 3: //TODO Same as the first one, but when he sends the offer, we will reduce the coupoints from him to make sure the trade will happen 100%
                        databaseReference.child("Users").child(SenderName).child("coupoints").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    System.out.println("there is a problem");
                                }
                                else {
                                    String coupoints = String.valueOf(task.getResult().getValue());
                                    int coupoint = Integer.parseInt(coupoints);
                                    if(numOffer <= coupoint) //Checks The other user's Coupoints
                                    {
                                        databaseReference.child("Users").child(SenderName).child("coupoints").setValue(coupoint - numOffer);
                                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("coupoints").setValue(myCoupoints + numOffer);
                                        addUserNotification("5" + "*" + mAuth.getCurrentUser().getUid() + "*" + timestampNow + "*" + coupName + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*" +couponId
                                                + "*" + offer, SenderName);

                                        Map<String, Object> dataEdit = new HashMap<>();
                                        dataEdit.put("UserUid", SenderName);
                                        dataEdit.put("Tradable", false);
                                        DocumentReference updateUser = db.collection("coupons")
                                                .document(couponId);
                                        updateUser.update(dataEdit);
                                    }
                                    else // If the other user doesnt have enough money to pay for the coupon.
                                    {
                                        //Message for the one giving the offer that he doesnt have enough and if he would like to open a chat with him.
                                        noCoupointMessage(SenderName);
                                    }
                                    System.out.println(coupoints + " this is the coupoints");
                                }
                            }
                        });
                        break;
                    case 4:
                    case 5:
                        //We don't need to do anything with those, they are only confirmation notifications.
                        break;
                }
                HashMap<String, String> delNotify = new HashMap<>();
                delNotify.put("Notifications", notificationsList.get(holder.getAdapterPosition()));
                notificationsList.remove(holder.getAdapterPosition());
                updateData(notificationsList);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).
                        update("Notifications", FieldValue.arrayRemove(delNotify.get("Notifications")));
            }
        });
    }

    //Adds the notification to the user
    public void addUserNotification(String notification, String ownerCoupon)
    {
        List<String> notifications = new ArrayList<>();

        Map<String, Object> dataEdit = new HashMap<>();
        //data.put("CoupUid", "coupon");
        dataEdit.put("Notifications", notifications);
        DocumentReference updateUser = db.collection("users")
                .document(ownerCoupon);
        updateUser.update("Notifications", FieldValue.arrayUnion(notification));
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
        ImageView imageView;

        public HolderNotification(@NonNull View itemView) {
            super(itemView);

            // init views
            currentRow = itemView.findViewById(R.id.currentRow);
            nameTv = itemView.findViewById(R.id.nameTv);
            notificationTv = itemView.findViewById(R.id.notificationTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            deleteBut = itemView.findViewById(R.id.button_delete);
            acceptBut = itemView.findViewById(R.id.button_accept);
            imageView = itemView.findViewById(R.id.imageCoupon);
        }
    }

    //Opens a chat with the user (Case 2)
    public void addToRealtimeAndFirestore(int position, String user2, String couponId)
    {
        final int[] chatKey = {0};
        final int[] chatkeyFinal = {0};
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
                        if ((getUserOne.equals(mAuth.getCurrentUser().getUid()) && getUserTwo.equals(user2)) || (getUserOne.equals(user2) && getUserTwo.equals(mAuth.getCurrentUser().getUid()))) {
                            checker[0] = true;
                            if (dataSnapshot2.child("agreed").exists()) {
                                chatkeyFinal[0] = chatKey[0];
                                System.out.println(chatKey[0] + " this is the chatKey");
                                databaseReference.child("chat").child((chatKey[0]) + "").child("agreed").child(mAuth.getCurrentUser().getUid()).setValue("1");
                                DocumentReference updateUser = db.collection("users")
                                        .document(mAuth.getCurrentUser().getUid());
                                updateUser.update("ChatUsers", FieldValue.arrayUnion(user2));

                                DocumentReference updateUser1 = db.collection("users")
                                        .document(user2);
                                updateUser1.update("ChatUsers", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
                                db.collection("coupons").document(couponId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        String uid = value.getString("UserUid");
                                        System.out.println(chatkeyFinal[0] + " this is the uid");
                                        if(uid.equals(mAuth.getCurrentUser().getUid()))
                                        {
                                            databaseReference.child("chat").child((chatkeyFinal[0]) + "").child("coupons").child(user2).child(couponId).setValue(couponId);
                                        }
                                        else if(uid.equals(user2))
                                        {
                                            databaseReference.child("chat").child((chatkeyFinal[0]) + "").child("coupons").child(mAuth.getCurrentUser().getUid()).child(couponId).setValue(couponId);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
                if(!checker[0]) {
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("user_1").setValue(mAuth.getCurrentUser().getUid());
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("user_2").setValue(user2);
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("messages").child(currentTimestamp).child("msg").setValue("Hello I Opened a chat with you!");
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("messages").child(currentTimestamp).child("mobile").setValue(mAuth.getCurrentUser().getUid());
                    databaseReference.child("chat").child((maxKey[0] + 1) + "").child("agreed").child(mAuth.getCurrentUser().getUid()).setValue("1");
                    db.collection("coupons").document(couponId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String uid = value.getString("UserUid");
                            if(uid.equals(mAuth.getCurrentUser().getUid()))
                            {
                                databaseReference.child("chat").child((maxKey[0] + 1) + "").child("coupons").child(user2).child(couponId).setValue(couponId);
                            }
                            else if(uid.equals(user2))
                            {
                                databaseReference.child("chat").child((maxKey[0] + 1) + "").child("coupons").child(mAuth.getCurrentUser().getUid()).child(couponId).setValue(couponId);
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Opens a chat with the user (Case 2)
    public void removeRealtime(int position, String user2, String couponId)
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
                        if ((getUserOne.equals(mAuth.getCurrentUser().getUid()) && getUserTwo.equals(user2)) || (getUserOne.equals(user2) && getUserTwo.equals(mAuth.getCurrentUser().getUid()))) {
                            databaseReference.child("chat").child(chatKey[0] + "").child("agreed").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    System.out.println(snapshot.getValue().toString().split(",").length + "this is the snapshot");
                                    if(snapshot.getValue().toString().split(",").length != 2)
                                        databaseReference.child("chat").child(chatKey[0] + "").removeValue();
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

    //Opens a chat with the user (Case 2)
    public void noCoupointMessage(String user2)
    {
        final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
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
                        if ((getUserOne.equals(mAuth.getCurrentUser().getUid()) && getUserTwo.equals(user2)) || (getUserOne.equals(user2) && getUserTwo.equals(mAuth.getCurrentUser().getUid()))) {
                            databaseReference.child("chat").child(chatKey[0] + "").child("messages").child(currentTimestamp).child("msg").setValue("[Updated Offer] Hello The User " + user2 + " didn't have enough coupoints to pay for the coupon!");
                            System.out.println(chatKey[0] + " this is the  chatkey");
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