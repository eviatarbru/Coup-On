package com.coupOn.platform.coupOn.ChatMessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>
{

    private List <MessageChatList> chatLists;
    private final Context context;
    private String userMobile;

    //firebase
    private FirebaseAuth mAuth;

    public ChatAdapter(List<MessageChatList> chatLists, Context context) {
        mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
        String uid = mAuth.getCurrentUser().getUid(); //Gets the UID of the current User.
        this.chatLists = chatLists;
        this.context = context;
        this.userMobile = uid;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        MessageChatList list2 = chatLists.get(position);

        if(list2.getMobile().equals(userMobile))
        {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate() + " " + list2.getTime());
        }
        else
        {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate() + " " + list2.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatLists(List <MessageChatList> chatLists)
    {
        this.chatLists = new ArrayList<>();
           this.chatLists = chatLists;
           notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);

            myLayout = itemView.findViewById(R.id.myLayout);
            myMessage = itemView.findViewById(R.id.myMessage);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
}
