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

    Context context;

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

        ImageView image = (ImageView) convertView.findViewById(R.id.imageCoupon);



//        System.out.println("@@@@ card: "+ card_item.getDiscountType() + " " + card_item.getDescription() + " " + card_item.getCouponId());

        couponName.setText(card_item.getCouponName());  // gets it from a tread in swipeCards
        expireDate.setText(card_item.getExpireDate());
        couponLocation.setText(card_item.getLocation());
        couponInterest.setText(card_item.getInterests());
        discountType.setText(card_item.getDiscountType());  // null for some reason
        couponDescription.setText(card_item.getDescription());

//        image.setImageURI(null);
        Glide.with(getContext())
                .load(card_item.getUri()) // the uri you got from Firebase
                .into(image); //Your imageView variable
//        image.setImageResource(R.drawable.example_coupon);// mipmap.ic_launcher

        return convertView;
    }
}
