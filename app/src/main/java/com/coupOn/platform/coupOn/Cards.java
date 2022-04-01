package com.coupOn.platform.coupOn;

public class Cards { //Cards need to have values like picture and so on
    private String userId;
    private String name;
    private String couponName;
    private String interests;
    private String description;

    public Cards(String name, String couponName, String interests, String description) {
        this.name = name;
        this.couponName = couponName;
        this.interests = interests;
        this.description = description;
    }

    public Cards(String userId, String name){
        this.userId = userId;
        this.name = name;
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

}
