package com.coupOn.platform.coupOn.Model;

import android.media.Image;

public class Coupon
{
    private Image couponImg;
    private String name;
    private String expiteDate;
    private String internetOrIrl;
    private String description;

    public Coupon(String name, String expiteDate, String internetOrIrl, String description) {
        this.name = name;
        this.expiteDate = expiteDate;
        this.internetOrIrl = internetOrIrl;
        this.description = description;
    }


}
