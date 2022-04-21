package com.coupOn.platform.coupOn.Notification;

import com.coupOn.platform.coupOn.SwipeCards;

// class for recyclerview of notification
public class ModelNotification {
    String pId, timestamp, pUid, notification, sName, sEmail;

    public ModelNotification(String pId, String timestamp, String pUid, String notification, String sName, String sEmail, String sImage) {
        this.pId = pId;
        this.timestamp = timestamp; //get current time
        this.pUid = pUid;
        this.notification = "Liked your coupon. Do you want to open a chat with him?";
        this.sName = sName;
        this.sEmail = sEmail;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

}
