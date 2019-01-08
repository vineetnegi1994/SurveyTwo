package com.admin.surveytwo.activities;

public class Users {

    public String name;
    public String latLng;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Users() {
    }

    public Users(String name, String latLng) {
        this.name = name;
        this.latLng = latLng;
    }
}
