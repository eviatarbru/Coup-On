package com.coupOn.platform.coupOn;

import android.net.Uri;

public class Cards { //Cards need to have values like picture and so on
    private String couponName;
    private String interests;
    private String description;
    private String expireDate;
    private String location;
    private String discountType;
    private String couponId;
    private String ownerId;

    private Uri uri;

    public Cards(String couponName, String interests, String description, String expireDate, String location
            , String discountType, String couponId, Uri uri, String ownerId) {//String name,
        this.couponName = couponName;
        this.interests = interests;
        this.description = description;
        this.expireDate = expireDate;
        this.location = location;
        this.discountType = discountType;
        this.couponId = couponId;
        this.uri = uri;
        this.ownerId = ownerId;
    }

    public Cards(String couponName, String ownerId) {
        this.couponName = couponName;
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    //getters and setters

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

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String code) {
        this.couponId = couponId;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "couponName='" + couponName + '\'' +
                ", interests='" + interests + '\'' +
                ", description='" + description + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", location='" + location + '\'' +
                ", discountType='" + discountType + '\'' +
                ", couponId='" + couponId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", uri=" + uri +
                '}';
    }
}
