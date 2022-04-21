package com.coupOn.platform.coupOn.Notification;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Chat.MessagesList;
import com.shashank.platform.coup_on.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.HolderNotification> {

    private Context context;
    private ArrayList<String> notificationsList;

    public AdapterNotification(Context context, ArrayList<String> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public HolderNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view row_notification

        View view = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);

        return new HolderNotification(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNotification holder, int position) {
        //get and set data to views

        //get data
        String model = notificationsList.get(position);
       // String name = model.getsName();
       // String notification = model.getNotification();
        long timestamp = Calendar.getInstance().getTimeInMillis();

        //convert timestamp to dd/mm/yyyy hh:mm am/pm
        //Calendar calendar = Calendar.getInstance(Locale.getDefault());
        //calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", timestamp).toString();

        //set to views
        holder.nameTv.setText(model);
        holder.notificationTv.setText("Liked your coupon. Do you want to open a chat with him?");
        holder.timeTv.setText(pTime);

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
        //ImageView avatarIv;
        TextView nameTv, notificationTv, timeTv;
        private LinearLayout currentRow;
        //int position;

        public HolderNotification(@NonNull View itemView) {
            super(itemView);

            // init views
            //avatarIv = itemView.findViewById(R.id.avatarIv);
            currentRow = itemView.findViewById(R.id.currentRow);
            nameTv = itemView.findViewById(R.id.nameTv);
            notificationTv = itemView.findViewById(R.id.notificationTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }

}
/*itemView.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   // go to the chat of the user
               }
           });

            itemView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/