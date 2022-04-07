package com.coupOn.platform.coupOn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.coupOn.platform.coupOn.Model.Coupon;
import com.coupOn.platform.coupOn.Model.User;
import com.shashank.platform.coup_on.R;

import java.util.ArrayList;

public class UserCoupons extends AppCompatActivity {

    private RecyclerView couponsList;
    private ArrayList<Coupon> userCouponsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coupons);

        userCouponsList = new ArrayList<>();

        setCouponInfo();

//        this.couponsList = (RecyclerView) findViewById(R.id.couponsList);
//
//        LinearLayoutManager lim = new LinearLayoutManager(this);
//        lim.setOrientation(LinearLayoutManager.VERTICAL);
//        ContentAdapter ca = new ContentAdapter(createList(10));

    }

    private void setCouponInfo() {
//        User user = new User("ido", "ido test");
//
//        userCouponsList.add(new Coupon(image,"test","11-12-2022","Israel",true,"testint",100,"123456",user));
    }
}