package com.coupOn.platform.coupOn;

import android.net.Uri;

public class Cards { //Cards need to have values like picture and so on
    private String userId;
    //private String name;
    private String couponName;
    private String interests;
    private String description;
    private String expireDate;
    private String location;
    private String discountType;
    private String code;

    private Uri uri;

    public Cards(String couponName, String interests, String description, String expireDate, String location, String discountType, String code, Uri uri) {//String name,
        //this.name = name;
        this.couponName = couponName;
        this.interests = interests;
        this.description = description;
        this.expireDate = expireDate;
        this.location = location;
        this.discountType = discountType;
        this.code = code;
        this.uri = uri;
    }

    public Cards(String couponName, String interests, String description) {//String name,
//        this.name = name;
        this.couponName = couponName;
        this.interests = interests;
        this.description = description;
        this.uri = null;
    }

    public Cards(String userId, String couponName){
        this.userId = userId;
        this.couponName = couponName;
        this.uri = null;
    }

    public Cards(String userId, String couponName, Uri uri){
        this.userId = userId;
        this.couponName = couponName;
        this.uri = uri;
    }



    //getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
