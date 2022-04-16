package com.coupOn.platform.coupOn.Model;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

public class Coupon
{
    private String couponImage;
    private String couponName;
    private String expireDate;
    private String location;
    private String description;
    private String ownerId;
    private String couponId;
    private String interest;
    private String discountType;
    private String code;

    private Uri uri;

    private FirebaseAuth mAuth;

    public Coupon(String couponImage, String couponName, String expireDate, String location, String description
            , String ownerId, String couponId, String interest, String discountType, String code) {
        this.couponImage = couponImage;
        this.couponName = couponName;
        this.expireDate = expireDate;
        this.location = location;
        this.description = description;
        this.ownerId = ownerId;
        this.couponId = couponId;
        this.interest = interest;
        this.discountType = discountType;
        this.code = code;
        this.uri = null;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponImage='" + couponImage + '\'' +
                ", couponName='" + couponName + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", couponId='" + couponId + '\'' +
                ", interest='" + interest + '\'' +
                ", discountType='" + discountType + '\'' +
                ", code='" + code + '\'' +
                ", uri=" + uri +
                '}';
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerUserId) {
        this.ownerId = ownerUserId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
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
}
