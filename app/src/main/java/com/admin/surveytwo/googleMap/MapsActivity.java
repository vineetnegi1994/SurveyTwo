package com.admin.surveytwo.googleMap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.admin.surveytwo.R;
import com.admin.surveytwo.activities.Artist;
import com.admin.surveytwo.activities.ListActivity;
import com.admin.surveytwo.activities.NotificationHelper;
import com.admin.surveytwo.sevices.IserviceTwo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {


    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int NOTI_PRIMARY1 = 1100;
    private static final int NOTI_PRIMARY2 = 1101;
    private static final int NOTI_SECONDARY1 = 1200;
    private static final int NOTI_SECONDARY2 = 1201;
    private NotificationHelper noti;
    LatLng latLngA;
    DatabaseReference databaseArtists;

    IserviceTwo myService;
     GoogleMap mMap;
     LocationManager locationManager;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    NotificationChannel mChannel;
    Notification notification;
    FusedLocationProviderClient mFusedLocationClient;
    Date currentTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        noti = new NotificationHelper(this);
        databaseArtists = FirebaseDatabase.getInstance().getReference("artist");
         mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFrag.getMapAsync(this);
        mapFrag.getView().setVisibility(View.GONE);

         currentTime = Calendar.getInstance().getTime();
        System.out.println("Real Time Date = "+currentTime);


    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }




    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();


            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
              //  Log.i("MapsActivity", "Location: " + 28.5445 + " " + 77.2642);
                mLastLocation = location;

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(28.536957, 77.271521);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Tache Technologies");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));


                 latLngA = new LatLng(location.getLatitude(),location.getLongitude());
                LatLng latLngB = new LatLng(28.536957,77.271521);

                Location locationA = new Location("point A");
                locationA.setLatitude(latLngA.latitude);
                locationA.setLongitude(latLngA.longitude);
                Location locationB = new Location("point B");
                locationB.setLatitude(latLngB.latitude);
                locationB.setLongitude(latLngB.longitude);

                System.out.println("dddddddddddddd  ======   "+latLngA);
                System.out.println("Live Location ==== "+locationA);
                System.out.println("Set Location ==== "+locationB);

                float distance = locationA.distanceTo(locationB);

                System.out.println(" My Distance == "+distance);

                Circle circle1 = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(28.536957,77.271521))
                        .radius(10)
                        .strokeColor(Color.RED)
                );
                Circle circle2 = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(28.536957,77.271521))
                        .radius(50)
                        .strokeColor(Color.RED)
                );



                if(distance>=circle2.getRadius()){

                    sendNotification2(NOTI_SECONDARY1, getTitleSecondaryText());
                    addArtist();
                  //  finish();
                  //  System.exit(1);


                }
                if(distance<=circle2.getRadius()){

                    sendNotification(NOTI_SECONDARY1, getTitleSecondaryText());
                    addArtist();
                  //  finish();
                  //  System.exit(1);


                }




                /*Geofence geofence = new Geofence.Builder()
                        .setRequestId(String.valueOf(1)) // Geofence ID
                        .setCircularRegion( 28.536957, 77.271521, 200) // defining fence region
                        .setExpirationDuration( 5 ) // expiring date
                        // Transition types that it should look for
                        .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                        .build();

                System.out.println(" Hello Google =  "+geofence);
               System.out.println("=========== "+geofence.getRequestId());*/


            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                    if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

    private void addArtist(){

       // String name = editText1.getText().toString().trim();
            String name = latLngA.toString().trim();
            String timeDate = String.valueOf(currentTime.toString().trim());
        if(!TextUtils.isEmpty(name)){
            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id,name,timeDate);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(getApplicationContext(), "Artist Added", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Enter Name Firest", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(int id, String title) {
        Notification.Builder nb = null;
        Notification.Builder mb = null;

        nb = noti.getNotification2(title, getString(R.string.secondary1_body));
        if (nb != null) {
            noti.notify(id, nb);
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification2(int id, String title) {
        Notification.Builder nb = null;
        Notification.Builder mb = null;


        mb = noti.getNotification2(title, getString(R.string.secondary2_body));
        if (mb != null) {
            noti.notify(id, mb);
        }

    }




    private String getTitleSecondaryText() {

        return "";
    }


    public void pushNotification(){

        String url = "http://www.tachetechnologies.com/internal/firebase.php?id=AAAAcBY2nt8:APA91bFiFh3N-9Q2Wpm4HwhTtVCz-pAIx0gxVDQvPGhc8HEXvGRJEXVcfgekxasfJlM0PEl7HIbIaNChpuerJU0jW6iaAOOjyy7DZXqBkYSCOj8LktKLOaOAad5Rt6lBm9qDGAYsA3TF";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("Response ===  "+response.toString());
                    JSONObject object = new JSONObject(response.toString());

                    String string = object.getString("success");

                    if(string.equals("1")){
                        Toast.makeText(getApplicationContext(), "Yoy are Successfully entered in 20 metere range", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network issue", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);



    }
}
