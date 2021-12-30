package com.coupOn.platform.coupOn.Model;

import android.media.Image;

public class Coupon
{
    private Image couponImg;
    private String name;
    private String expiteDate;
    private String location;
    private String internetOrIrl;
    private String description;
    private String ownerUserId;

    public Coupon(String name, String expiteDate, String internetOrIrl, String description) {
        this.name = name;
        this.expiteDate = expiteDate;
        this.internetOrIrl = internetOrIrl;
        this.description = description;
    }


}
