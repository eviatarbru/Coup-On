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
    private ArrayList<String> chattingUserUIDs;

    public User(String email, String fullName, ArrayList<Coupon> coupons, ArrayList<Coupon> likedCoupons)
    {
        //get Info from the firebase;, when login in.
    }

    public User(String email, String fullName, ArrayList<String> chattingString) {
        this.email = email;
        this.fullName = fullName;
        this.chattingUserUIDs = new ArrayList<String>();
        this.coupons = new ArrayList<Coupon>();
        this.chattingUserUIDs = chattingString;
//        if(chattingString.contains(","))
//        {
//            String[] chattingUser = chattingString.split(", ");
//            System.out.println(chattingUser + " I want to die");
//            for(int i = 0; i < chattingUser.length; i++)
//            {
//                this.chattingUserUIDs.add(chattingUser[i]);
//            }
//        }
//        else
//        {
//            this.chattingUserUIDs.add(chattingString);
//        }

    }

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
        this.chattingUserUIDs = new ArrayList<>();
        this.coupons = new ArrayList<Coupon>();
    }

    public void addCouponToUser(Coupon coupon){

        this.coupons.add(coupon);
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

    public ArrayList<String> getChattingUserUIDs() {
        return chattingUserUIDs;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", chattingUserUIDs=" + chattingUserUIDs +
                '}';
    }
}
