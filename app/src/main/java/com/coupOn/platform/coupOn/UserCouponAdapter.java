package com.coupOn.platform.coupOn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.List;

//RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder>
public class UserCouponAdapter extends RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder> {

    private List<Coupon> usersCoupList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public UserCouponAdapter(ArrayList<Coupon> usersCoupList){
        this.usersCoupList = usersCoupList;
    }

    public class CouponsListViewHolder extends RecyclerView.ViewHolder{
        private TextView couponNameItem;
        private TextView expireDateItem;
        private TextView locationItem;
        private TextView descriptionItem;
        private Button deleteCoupon;

        public CouponsListViewHolder(final View view){
            super(view);

            couponNameItem = view.findViewById(R.id.couponNameItem);
            expireDateItem = view.findViewById(R.id.expireDateItem);
            locationItem = view.findViewById(R.id.locationItem);
            descriptionItem = view.findViewById(R.id.descriptionItem);
            deleteCoupon = view.findViewById(R.id.deleteCoupon);

        }
    }

    @NonNull
    @Override
    public UserCouponAdapter.CouponsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_coupons_list_items, parent, false);
        return new CouponsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( @NonNull UserCouponAdapter.CouponsListViewHolder holder, int position) { //give position
        FirebaseStorage storage = FirebaseStorage.getInstance();

        String cid = usersCoupList.get(position).getCouponId();
        String couponName = usersCoupList.get(position).getCouponName();
        String expireDate = usersCoupList.get(position).getExpireDate();
        String location = usersCoupList.get(position).getLocation();
        String description = usersCoupList.get(position).getDescription();

        holder.couponNameItem.setText(couponName);
        holder.expireDateItem.setText(expireDate);
        holder.locationItem.setText(location);
        holder.descriptionItem.setText(description);

        holder.deleteCoupon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        System.out.println("this is the info of the coupon i pressed yes" + couponName + " id: " + cid + " image: " + usersCoupList.get(holder.getAdapterPosition()).getCouponImage());
                        db.collection("coupons").document(cid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(view.getContext(), "Coupon Deleted!!", Toast.LENGTH_SHORT).show();
                                usersCoupList.remove(holder.getAdapterPosition());
                                updateData(usersCoupList);
                            }
                        });
                        if(usersCoupList.get(holder.getAdapterPosition()).getCouponImage().equals("default_coupon")) {
                            storage.getReference().child("/images").child(usersCoupList.get(holder.getAdapterPosition()).getCouponImage()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Coupon Image Deleted!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return usersCoupList.size();
    }

    //PETER NEEDS TO LOOK (WAIT FOR EVI)
    public void updateData(List<Coupon> usersCoupList1)
    {
        usersCoupList = usersCoupList1;
        notifyDataSetChanged();
    }
}
