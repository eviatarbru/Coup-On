package com.coupOn.platform.coupOn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shashank.platform.coup_on.R;

import java.util.List;


public class ArrayAdapterCoupon extends android.widget.ArrayAdapter<Cards> {

    Context context;

    public ArrayAdapterCoupon(Context context, int resourceId, List<Cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageCoupon);

        name.setText(card_item.getName());
        image.setImageURI(card_item.getUri());
//        image.setImageResource(R.drawable.example_coupon);// mipmap.ic_launcher

        return convertView;
    }
}
