package com.coupOn.platform.coupOn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;

public class UserCouponAdapter extends RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder> {

    private ArrayList<Coupon> usersCoupList;

    public UserCouponAdapter(ArrayList<Coupon> usersCoupList){
        this.usersCoupList = usersCoupList;
    }

    public class CouponsListViewHolder extends RecyclerView.ViewHolder{
        private TextView coupListName;

        public CouponsListViewHolder(final View view){
            super(view);
            coupListName = view.findViewById(R.id.coupListName);
        }
    }

    @NonNull
    @Override
    public UserCouponAdapter.CouponsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_coupons_list_items, parent, false);
        return new CouponsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCouponAdapter.CouponsListViewHolder holder, int position) {
        //String couponName = usersCoupList.get(position).getCouponName();
        //holder.coupListName.setText(couponName);
    }

    @Override
    public int getItemCount() {
        return usersCoupList.size();
    }
}
