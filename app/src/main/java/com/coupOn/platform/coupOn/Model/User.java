package com.coupOn.platform.coupOn.Model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class User
{
    private Image profileImg;
    private String email;
    private String fullName;
    private String date;
    private String gender;
    private ArrayList<String> interests;
    private ArrayList<Coupon> coupons;
    private ArrayList<Coupon> likedCoupons;
    private ArrayList<String> chattingUserUIDs;
    private ArrayList<String> notifications;

    public User(String email, String fullName, String date, ArrayList<String> chattingString, ArrayList<String> interests, ArrayList<String> notifications) {
        this.email = email;
        this.fullName = fullName;
        this.date = date;
        this.chattingUserUIDs = new ArrayList<String>();
        this.interests = new ArrayList<>();
        this.interests = interests;
        this.coupons = new ArrayList<Coupon>();
        this.chattingUserUIDs = chattingString;
        this.notifications = new ArrayList<>();
        this.notifications = notifications;
    }

    public User(String email, String fullName, String date, ArrayList<String> chattingString, ArrayList<String> interests) {
        this.email = email;
        this.fullName = fullName;
        this.date = date;
        this.chattingUserUIDs = new ArrayList<String>();
        this.interests = new ArrayList<>();
        this.interests = interests;
        this.coupons = new ArrayList<Coupon>();
        this.chattingUserUIDs = chattingString;
        this.notifications = new ArrayList<>();
    }

    public User(String email, String fullName, String date, ArrayList<String> chattingString) {
        this.email = email;
        this.fullName = fullName;
        this.date = date;
        this.coupons = new ArrayList<Coupon>();
        this.chattingUserUIDs = new ArrayList<String>();
        this.chattingUserUIDs = chattingString;
        this.interests = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public User(String email, String fullName, String date) {
        this.email = email;
        this.fullName = fullName;
        this.date = date;
        this.coupons = new ArrayList<Coupon>();
        this.chattingUserUIDs = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.notifications = new ArrayList<>();
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

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", date=" + date +
                ", interests=" + interests +
                ", chattingUserUIDs=" + chattingUserUIDs +
                ", notifications=" + notifications +
                '}';
    }
}
