package com.admin.surveytwo.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.admin.surveytwo.R;
import com.admin.surveytwo.googleMap.MapsActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();

    private static final int NOTI_PRIMARY1 = 1100;
    private static final int NOTI_PRIMARY2 = 1101;
    private static final int NOTI_SECONDARY1 = 1200;
    private static final int NOTI_SECONDARY2 = 1201;
    private NotificationHelper noti;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private Firebase mRef;
    private String userId;

    CardView cardView1, cardView2, cardView3;
    String tittle = "Hello";
    String subject = "Nearby";
    String url = "http://www.tachetechnologies.com/internal/firebase.php?id=AAAAcBY2nt8:APA91bFiFh3N-9Q2Wpm4HwhTtVCz-pAIx0gxVDQvPGhc8HEXvGRJEXVcfgekxasfJlM0PEl7HIbIaNChpuerJU0jW6iaAOOjyy7DZXqBkYSCOj8LktKLOaOAad5Rt6lBm9qDGAYsA3TF";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Firebase.setAndroidContext(this);
        noti = new NotificationHelper(this);

        cardView1 = (CardView) findViewById(R.id.cardOne);
        cardView2 = (CardView) findViewById(R.id.cardTwo);
        cardView3 = (CardView)findViewById(R.id.cardThree);

        mFirebaseInstance = FirebaseDatabase.getInstance();

       /* // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("https://surveytwo.firebaseio.com/");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("https://surveytwo.firebaseio.com/").setValue("surveyTwo");*/

        // app_title change listener
       /* mFirebaseInstance.getReference("https://surveytwo.firebaseio.com/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });*/



        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendFCMPush();

                pushNotification();

                sendNotification(NOTI_SECONDARY1, getTitleSecondaryText());

            }
        });


        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),DataListOne.class);
                startActivity(intent);

                    /*String name = "Rohan";
                    String latlng = "20.3456";

                    if(TextUtils.isEmpty(userId)){
                        createUser(name, latlng);
                    }*/

            }
        });

    }

    private void createUser(String name, String latlng) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
            Toast.makeText(ListActivity.this, "User created", Toast.LENGTH_SHORT).show();
        }

        Users user = new Users(name, latlng);

        mFirebaseDatabase.child(userId).setValue(user);

      //  addUserChangeListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(int id, String title) {
        Notification.Builder nb = null;

        nb = noti.getNotification2(title, getString(R.string.secondary1_body));
        if (nb != null) {
            noti.notify(id, nb);
        }

    }




    private String getTitleSecondaryText() {
       /* if (titlePrimary != null) {
            return titleSecondary.getText().toString();
        }*/
        return "";
    }



    public void pushNotification(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String string = object.getString("success");

                    if(string.equals("1")){
                        Toast.makeText(ListActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);



    }

}
