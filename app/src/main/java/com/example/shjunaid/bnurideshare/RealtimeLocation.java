package com.example.shjunaid.bnurideshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import API.IApiCaller;
import API.JoinedRidersCall;
import API.RideEndCall;
import API.RideStartedCall;
import Fragments.JoinedRidersAdapter;
import Fragments.RideJoiningsFragment;
import Models.AppConstants;
import Models.JoinRidersModel;
import Models.RideModel;
import Models.RideModelFirebase;

/**
 * Created by Sh Junaid on 1/23/2017.
 */
public class RealtimeLocation extends AppCompatActivity {
    private DatabaseReference mdatabase ;
    private BroadcastReceiver broadcastReceiver;
    private Button startbtn, stopbtn,passengerbtn;
    private TextView txt;
    private GoogleMap mMap;
    private MapView mapView;
    private Address location1, location2,pickuplocation;
    private List<Address> add;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Marker marker;
    RideModel sm = new RideModel();
    private String start;
    private String End;
    private int rideId;
    private String poly;
    private List<JoinRidersModel> rlist;


    //mdatabase= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realtime_location_activity);
        startbtn = (Button) findViewById(R.id.startservice);
        stopbtn = (Button) findViewById(R.id.stopservice);
        passengerbtn = (Button) findViewById(R.id.passengers_btn);
        mapView = (MapView) findViewById(R.id.realtimelocation_map);

        start = getIntent().getStringExtra("StartLocation");
        End  = getIntent().getStringExtra("DestLocation");
        rideId = getIntent().getIntExtra("RideId", 0);
        poly = getIntent().getStringExtra("Checkpoints");

        new ArrayList<JoinRidersModel>();
        JoinedRidersCall joinedRiders = new JoinedRidersCall(RealtimeLocation.this, new IApiCaller() {
            @Override
            public void onResult(String data) {
                if (data != "404") {
                    Gson gson = new Gson();
                    rlist = gson.fromJson(data, new TypeToken<ArrayList<JoinRidersModel>>() {}.getType());

                }
            }
        });
        joinedRiders.execute(rideId);
//        try {
//            String data = (String) joinedRiders.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        Geocoder geocoder = new Geocoder(this);
        add = new ArrayList<>();
        try {
            add = geocoder.getFromLocationName(start, 1);
            location1 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        try {
            add = geocoder.getFromLocationName(End, 1);
            location2 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {

            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(RealtimeLocation.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RealtimeLocation.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 13));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getSource())
                        .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                mMap.addMarker(new MarkerOptions()

                        .title(sm.getDestination())
                        .position(new LatLng(location2.getLatitude(), location2.getLongitude())));
                if(!rlist.isEmpty()){
                    for(JoinRidersModel rm : rlist){
                        getPickupLocation(rm.getPickupLocation());
                        mMap.addMarker(new MarkerOptions()
                                .title(rm.getUserName()+" Pickup Location")
                                .position(new LatLng(pickuplocation.getLatitude(), pickuplocation.getLongitude())));
                    }
                }

                PolylineOptions polylineOptions = new PolylineOptions();

                polylineOptions.
                        geodesic(true).
                        color(Color.parseColor("#2051E1")).
                        width(20).
                        clickable(true);
                List<LatLng> latLngList = new ArrayList<LatLng>();
                ArrayList<Location> locations = new ArrayList<Location>();
                String parsestring = poly;
                parsestring = parsestring.replace("[", "");
                parsestring = parsestring.replace("]", "");
                parsestring = parsestring.replace("lat/lng: (", "");
                parsestring = parsestring.replace(")", "");
                ArrayList<String> stringArrayList = new ArrayList<String>(Arrays.asList(parsestring.split(",")));
                for (int i = 0; i < stringArrayList.size(); i += 2) {
                    LatLng abc = new LatLng(Double.parseDouble(stringArrayList.get(i)), Double.parseDouble(stringArrayList.get(i + 1)));
                    latLngList.add(abc);

                }

                for (int j = 0; j < latLngList.size(); j++) {
                    polylineOptions.add(latLngList.get(j));
                }

                polylinePaths.add(mMap.addPolyline(polylineOptions));







//                mMap.setMyLocationEnabled(true);
                //LatLng x = new LatLng(a.getLatitude(),a.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,16));
            }
        });

        if(!runtime_permissions())
            enable_maps();


//        StudentBean bean = new StudentBean();
//        bean.setId(1);
//        bean.setPhone("03227365199");
//        bean.setName("junaid");
//        bean.setPassword("abc123");
//        mdatabase= FirebaseDatabase.getInstance().getReference();
//        mdatabase.child("ride").setValue(bean);
//        mdatabase= FirebaseDatabase.getInstance().getReference();
//        mdatabase.child("rides").child("1004").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                RideModelFirebase rm = new RideModelFirebase();
////                rm = dataSnapshot.getValue(RideModelFirebase.class);
////                double a = (double)rm.getLat();
////                double b = (double)rm.getLng();
////                mMap.clear();
////
////
////
////                //MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(a,b));
////                //marker.setPosition(new LatLng(a,b));
////
////                mMap.addMarker(new MarkerOptions().title("my location").position(new LatLng(a,b)));
////
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 16));
////
////
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
      //  });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    RideModelFirebase bean = new RideModelFirebase();
                    String rideid = Integer.toString(rideId);
                    double a = (double)intent.getExtras().get("Latitude");
                    double b = (double)intent.getExtras().get("Longitude");
                    bean.setLat(a);
                    bean.setLng(b);
                    mdatabase= FirebaseDatabase.getInstance().getReference();
                    mdatabase.child("rides").child(rideid).setValue(bean);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a,b), 13));


                    //txt.append("\n" + intent.getExtras().get("Latitude"));

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

    }


    private void enable_maps(){
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                startService(i);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(RealtimeLocation.this);
                Intent resultIntent = new Intent(RealtimeLocation.this, RealtimeLocation.class);
                resultIntent.putExtra("RideId", rideId);
                resultIntent.putExtra("Checkpoints", poly);
                resultIntent.putExtra("StartLocation", start);
                resultIntent.putExtra("DestLocation", End);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(RealtimeLocation.this);
                mBuilder.setSmallIcon(R.mipmap.appicon);
                mBuilder.setContentTitle("Tracking Location");
                mBuilder.setContentText("getting current location");
                mBuilder.setOngoing(true);
                stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManager.notify(12, mBuilder.build());

                RideStartedCall rideStarted = new RideStartedCall();
                rideStarted.execute(rideId);

            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                stopService(i);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(12);
                RideEndCall rideEnd = new RideEndCall();
                rideEnd.execute(rideId);
                finish();

            }
        });

        passengerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RideJoiningsFragment rjf = new RideJoiningsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("RideId",rideId);
                rjf.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.realtime_content_area, rjf);
                ft.addToBackStack("rjf");
                ft.commit();
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_maps();
            }else {
                runtime_permissions();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Address getPickupLocation(String pickuploc){
        pickuplocation = null;
        Geocoder geocoder = new Geocoder(this);
        add = new ArrayList<>();
        try {
            add = geocoder.getFromLocationName(pickuploc, 1);
            pickuplocation = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        return pickuplocation;
    }

}

