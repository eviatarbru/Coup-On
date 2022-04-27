package com.coupOn.platform.coupOn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shashank.platform.coup_on.R;

import java.util.List;


public class ArrayAdapterCoupon extends android.widget.ArrayAdapter<Cards> {

    public ArrayAdapterCoupon(Context context, int resourceId, List<Cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        TextView couponName = (TextView) convertView.findViewById(R.id.name);
        TextView expireDate = (TextView) convertView.findViewById(R.id.expireDate);
        TextView couponLocation = (TextView) convertView.findViewById(R.id.couponLocation);
        TextView couponInterest = (TextView) convertView.findViewById(R.id.couponInterest);
        TextView discountType = (TextView) convertView.findViewById(R.id.discountType);
        TextView couponDescription = (TextView) convertView.findViewById(R.id.couponDescription);
        TextView couponPrice = (TextView) convertView.findViewById(R.id.price);

        ImageView image = (ImageView) convertView.findViewById(R.id.imageCoupon);

        couponName.setText(card_item.getCouponName());  // gets it from a tread in swipeCards
        expireDate.setText(card_item.getExpireDate());
        couponLocation.setText(card_item.getLocation());
        couponInterest.setText(card_item.getInterests());
        discountType.setText(card_item.getDiscountType());  // null for some reason
        couponDescription.setText(card_item.getDescription());
        if(card_item.getPrice() % 1 != 0)
            couponPrice.setText("₪" + card_item.getPrice() + "");
        else
        {
            int intPrice = (int)card_item.getPrice();
            couponPrice.setText("₪" + intPrice + "");
        }


        Glide.with(getContext())
                .load(card_item.getUri()) // the uri you got from Firebase
                .into(image); //Your imageView variable
        return convertView;
    }
}
