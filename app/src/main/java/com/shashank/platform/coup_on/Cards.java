package com.shashank.platform.coup_on;

public class Cards { //Cards need to have values like picture and so on
    private String userId;
    private String name;

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
