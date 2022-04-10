package com.coupOn.platform.coupOn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coupOn.platform.coupOn.Chat.MessagesList;
import com.coupOn.platform.coupOn.Model.Coupon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.platform.coup_on.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder>
public class UserCouponAdapter extends RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder> {

    private List<Coupon> usersCoupList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageReference;

    public UserCouponAdapter(ArrayList<Coupon> usersCoupList){
        this.usersCoupList = usersCoupList;
    }

    public class CouponsListViewHolder extends RecyclerView.ViewHolder{
        private TextView couponNameItem;
        private TextView expireDateItem;
        private TextView locationItem;
        private TextView descriptionItem;
        private ImageView couponImageItem;

        public CouponsListViewHolder(final View view){
            super(view);
            couponNameItem = view.findViewById(R.id.couponNameItem);
            expireDateItem = view.findViewById(R.id.expireDateItem);
            locationItem = view.findViewById(R.id.locationItem);
            descriptionItem = view.findViewById(R.id.descriptionItem);
            couponImageItem = view.findViewById(R.id.couponImageItem);
        }
    }

    @NonNull
    @Override
    public UserCouponAdapter.CouponsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_coupons_list_items, parent, false);
        return new CouponsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCouponAdapter.CouponsListViewHolder holder, int position) { //give position
        this.mAuth = FirebaseAuth.getInstance();
        System.out.println("@@@@ here ny brada1");

        String couponName = usersCoupList.get(position).getCouponName();
        String expireDate = usersCoupList.get(position).getExpireDate();
        String location = usersCoupList.get(position).getLocation();
        String description = usersCoupList.get(position).getDescription();

        holder.couponNameItem.setText(couponName);
        holder.expireDateItem.setText(expireDate);
        holder.locationItem.setText(location);
        holder.descriptionItem.setText(description);

        //String userUid =  mAuth.getCurrentUser().getUid();
        System.out.println("@@@@ here ny brada1");

        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + usersCoupList.get(position).getCouponImage() + ".jpeg");

        try {
            final File localFile = File.createTempFile(usersCoupList.get(position).getCouponImage(), "jpeg");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImageView myImg=(ImageView)holder.couponImageItem; /*findViewById(R.id.couponImageItem);*/
                            myImg.setImageBitmap(bitmap);
                        }
                    })
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                            ImageView myImg=(ImageView)holder.couponImageItem; /*findViewById(R.id.couponImageItem);*/
//                            myImg.setImageBitmap(bitmap);
////                            holder.couponImageItem.setImageURI(Uri.fromFile(localFile));
////                            ((ImageView) findViewById(R.id.couponImageItem)).setImageBitmap(bitmap);
//                        }
//                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Failed @@@@@" + usersCoupList.get(position).getCouponImage());
                            System.out.println(localFile + "@@@@");
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }


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
