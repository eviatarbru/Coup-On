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
    // private boolean [] interests; // need to think of a adaptive  data structure

    private FirebaseAuth mAuth;

    public Image getCouponImg() {
        return couponImg;
    }

    public void setCouponImg(Image couponImg) {
        this.couponImg = couponImg;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDigitalOrIrl() {
        return digitalOrIrl;
    }

    public void setDigitalOrIrl(boolean digitalOrIrl) {
        this.digitalOrIrl = digitalOrIrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

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
