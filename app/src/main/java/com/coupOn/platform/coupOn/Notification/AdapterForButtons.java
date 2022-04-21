package com.coupOn.platform.coupOn.Notification;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shashank.platform.coup_on.R;

import androidx.annotation.NonNull;

import java.util.List;


public class AdapterForButtons extends  RecyclerView.Adapter<ButtonPress>{

    List<String> items;

    public AdapterForButtons(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ButtonPress onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ButtonPress(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonPress holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


class ButtonPress extends RecyclerView.ViewHolder{
    TextView textView;
    private AdapterForButtons adapter;
    public ButtonPress(@NonNull View itemView){
        super(itemView);

        textView = itemView.findViewById(R.id.currentRow);
        itemView.findViewById(R.id.button_delete).setOnClickListener(view ->{
            adapter.items.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public ButtonPress linkAdapter(AdapterForButtons adapter){
        this.adapter = adapter;
        return this;
    }
}