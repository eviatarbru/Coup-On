package com.coupOn.platform.coupOn.Notification;

// class for recyclerview of notification
public class ModelNotification {
    String pId, timestamp, pUid, notification, sName, sEmail, sImage;

    public ModelNotification(String pId, String timestamp, String pUid, String notification, String sName, String sEmail, String sImage) {
        this.pId = pId;
        this.timestamp = timestamp;
        this.pUid = pUid;
        this.notification = notification;
        this.sName = sName;
        this.sEmail = sEmail;
        this.sImage = sImage;
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

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }
}
