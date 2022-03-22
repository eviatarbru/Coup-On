package com.coupOn.platform.coupOn.Model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class User
{
    private Image profileImg;
    private String email;
    private String fullName;
    private Date date;
    private String gender;
    private ArrayList<Coupon> coupons;
    private ArrayList<Coupon> likedCoupons;

    public User(String email, String fullName, ArrayList<Coupon> coupons, ArrayList<Coupon> likedCoupons)
    {
        //get Info from the firebase;, when login in.
    }

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'';
    }
}
