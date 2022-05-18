package com.coupOn.platform.coupOn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coupOn.platform.coupOn.ChatMessage.ChatScreen;
import com.coupOn.platform.coupOn.Model.Coupon;
import com.coupOn.platform.coupOn.Model.MainDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder>
public class UserCouponAdapter extends RecyclerView.Adapter<UserCouponAdapter.CouponsListViewHolder> {

    private List<Coupon> usersCoupList = new ArrayList<>();

    private Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //Connects to Authentication.
    StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    public UserCouponAdapter(ArrayList<Coupon> usersCoupList, Context context){
        this.usersCoupList = usersCoupList;
        this.context = context;
    }

    public class CouponsListViewHolder extends RecyclerView.ViewHolder{
        private TextView couponNameItem;
        private TextView expireDateItem;
        private TextView locationItem;
        private TextView descriptionItem;
        private Button deleteCoupon;
        private ImageView imageView;
        private RelativeLayout couponCard;
        private LinearLayout couponRootLayout;

        public CouponsListViewHolder(final View view){
            super(view);

            couponNameItem = view.findViewById(R.id.couponNameItem);
            expireDateItem = view.findViewById(R.id.expireDateItem);
            locationItem = view.findViewById(R.id.locationItem);
            descriptionItem = view.findViewById(R.id.descriptionItem);
            deleteCoupon = view.findViewById(R.id.deleteCoupon);
            imageView = view.findViewById(R.id.imageCoupon);
            couponCard = view.findViewById(R.id.couponCard);
            this.couponRootLayout = itemView.findViewById(R.id.couponRootLayout);

            if(UserCoupons.getCameFrom() == 2)
            {
                deleteCoupon.setVisibility(View.INVISIBLE);
            }
            else
                deleteCoupon.setVisibility(View.VISIBLE);
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
        final Uri[] uriMain = new Uri[1];
        final String[] ownerID = {""};
        int price = usersCoupList.get(position).getPrice();

        holder.couponNameItem.setText(couponName);
        holder.expireDateItem.setText(Html.fromHtml("Expire On: " + "<b>" + expireDate + "<b>"));
        holder.locationItem.setText(Html.fromHtml("Shop: " + "<b>" + location + "<b>"));
        holder.descriptionItem.setText(description);

        db.collection("coupons")
                .document(cid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imageName = documentSnapshot.getString("CouponImage");
                        ownerID[0] = documentSnapshot.getString("UserUid");
                        mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

                        try {
                            mStorageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri uri = task.getResult();
                                    uriMain[0] = uri;
                                    Glide.with(context)
                                            .load(uri) // the uri you got from Firebase
                                            .into(holder.imageView); //Your imageView variable
                                }
                            });
                        } catch (Exception e) {
                            System.out.println("this is an error with the uri (MainDB)");
                        }
                    }
                });



        holder.couponRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(UserCoupons.getCameFrom() == 1)
                {
                    Intent intent = new Intent(context , InfoCouponActivity.class);
                    intent.putExtra("couponName", couponName);
                    intent.putExtra("imageUri", uriMain[0]);
                    intent.putExtra("ownerId", ownerID[0]);
                    intent.putExtra("couponID", cid);

                    context.startActivity(intent);
                }
                else    // came from chats
                {
                    int position = holder.getLayoutPosition();

                    if(UserCoupons.getCameFrom() == 2){    //came from chat
                        //choose this coupon for trade
                        final EditText edittext = new EditText(context);        //pop-up for offer
                        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);//"The desired price is: " + tempPrice + "\n" +
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Offer Coupoints!  " + "Price: " + price);
                        builder.setMessage( "How much Coupoints do you offer for " + "\"" + couponName +"\"" +  "?");
                        builder.setView(edittext);

                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which)  //FAKE positive button
                            {
                                Toast.makeText(context, "Offer Sent!", Toast.LENGTH_SHORT).show();
                                //Do nothing here because we override this button later to change the close behaviour.
                                //However, we still need this because on older versions of Android unless we
                                //pass a handler the button doesn't get instantiated
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //Negative button
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(context, "Offer canceled", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        // start positive button
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)     //the REAL positive button!
                            {
                                Boolean wantToCloseDialog = false;

                                String offerTxt = edittext.getText().toString();
                                final int offer;
                                if(!offerTxt.isEmpty())
                                {
                                    wantToCloseDialog = true;
                                    offer = Integer.parseInt(offerTxt);
                                    System.out.println(cid + " t");
                                    if(cid != null){
                                        Toast.makeText(context, "Offer submitted", Toast.LENGTH_LONG).show();

                                        //timestamp for time and notification id
                                        String timeStamp = ""+System.currentTimeMillis();

                                        List<String> notifications = new ArrayList<>();


                                        // Firebase-Firestore
                                        Map<String, Object> dataEdit = new HashMap<>();
                                        //data.put("CoupUid", "coupon");
                                        dataEdit.put("Notifications", notifications);
                                        DocumentReference updateUser = db.collection("users")
                                                .document(usersCoupList.get(holder.getLayoutPosition()).getOwnerId());
                                        updateUser.update("Notifications", FieldValue.arrayUnion("3" + "*" + mAuth.getCurrentUser().getUid() + "*" + timeStamp + "*" + usersCoupList.get(holder.getLayoutPosition()).getCouponName() + "*"
                                                + MainDB.getInstance().getCurUser().get(mAuth.getCurrentUser().getUid()).getFullName() + "*" +usersCoupList.get(holder.getLayoutPosition()).getCouponId()
                                                + "*" + offer));

                                    }
                                }
                                if(wantToCloseDialog)
                                    dialog.dismiss();
                                else    //else dialog stays open
                                    Toast.makeText(context, "Field is empty", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //end pop-up offer
                    }
                }
            }
        });

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
                        if(!usersCoupList.get(holder.getAdapterPosition()).getCouponImage().equals("default_coupon.jpeg")) {
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

//        holder.couponCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(UserCoupons.getCameFrom() == 1)
//                {
//                    Intent intent = new Intent(context , InfoCouponActivity.class);
//                    intent.putExtra("couponName", couponName);
//                    intent.putExtra("imageUri", uriMain[0]);
//                    intent.putExtra("ownerId", ownerID[0]);
//                    intent.putExtra("couponID", cid);
//
//                    context.startActivity(intent);
//                }
//            }
//        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
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
