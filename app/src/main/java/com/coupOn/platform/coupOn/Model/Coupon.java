package com.coupOn.platform.coupOn.Model;

import android.media.Image;

import com.google.firebase.auth.FirebaseAuth;

public class Coupon
{
    private Image couponImg;
    private String couponName;
    private String expireDate;
    private String location;
    private boolean digitalOrIrl;
    private String description;
    private int price;
    private String ownerUserId;
    private User owner;
    // private boolean [] interests; // !!!!need to think of a data structure!!!!

    private FirebaseAuth mAuth;

    public Coupon(Image couponImg, String couponName, String expireDate, String location, boolean digitalOrIrl,
                  String description, int price, String ownerUserId, User owner) {

        this.couponImg = couponImg;
        this.couponName = couponName;
        this.expireDate = expireDate;
        this.location = location;
        this.digitalOrIrl = digitalOrIrl;
        this.description = description;
        this.price = price;
        this.ownerUserId = mAuth.getCurrentUser().getUid();
        this.owner = owner;
    }


}
