package com.coupOn.platform.coupOn;

import android.net.Uri;

public class Cards { //Cards need to have values like picture and so on
    private String userId;
    private String name;
    private String couponName;
    private String interests;
    private String description;
    private Uri uri;

    public Cards(String name, String couponName, String interests, String description) {
        this.name = name;
        this.couponName = couponName;
        this.interests = interests;
        this.description = description;
        this.uri = null;
    }

    public Cards(String userId, String name){
        this.userId = userId;
        this.name = name;
        this.uri = null;
    }

    public Cards(String userId, String name, Uri uri){
        this.userId = userId;
        this.name = name;
        this.uri = uri;
    }



    //getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
