package com.admin.surveytwo;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.solver.widgets.Helper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.admin.surveytwo.activities.ListActivity;
import com.admin.surveytwo.user.LoginPage;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth auth;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {


               /* if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashScreen.this, ListActivity.class));
                    finish();
                }*/
                Intent i = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
        auth = FirebaseAuth.getInstance();
    }


}
