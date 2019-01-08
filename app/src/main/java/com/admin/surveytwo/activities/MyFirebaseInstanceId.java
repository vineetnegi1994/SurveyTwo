package com.admin.surveytwo.activities;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseInstanceId extends FirebaseMessagingService {

    private  static final String REG_TOKEN="REG_TOKEN";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
       System.out.println("New Token For the Newest Device =====  "+s);
        Log.d(REG_TOKEN,s);
    }



}
