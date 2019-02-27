package com.example.shjunaid.bnurideshare;

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

import API.GetRideDetailCall;
import API.IApiCaller;
import API.JoinedRidersCall;
import Models.JoinRidersModel;
import Models.RideModel;
import Models.RideModelFirebase;

/**
 * Created by Sh Junaid on 1/23/2017.
 */
public class ViewDriverLocation extends AppCompatActivity {
    private DatabaseReference mdatabase ;
    private BroadcastReceiver broadcastReceiver;
    private Button startbtn, stopbtn;
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
        setContentView(R.layout.view_driver_location_activity);
        mapView = (MapView) findViewById(R.id.driver_location_map);

//        start = getIntent().getStringExtra("StartLocation");
//        End  = getIntent().getStringExtra("DestLocation");
        rideId = getIntent().getIntExtra("RideId", 0);
//        poly = getIntent().getStringExtra("Checkpoints");
        GetRideDetailCall rideDetail = new GetRideDetailCall();
       rideDetail.execute(rideId);
        String data1 = null;
        try {
            data1 = (String) rideDetail.get();
            Gson gson = new Gson();
            RideModel rm = gson.fromJson(data1,RideModel.class);
            start = rm.getSource();
            End  = rm.getDestination();
            poly = rm.getCheckpoints();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        new ArrayList<JoinRidersModel>();
        JoinedRidersCall joinedRiders = new JoinedRidersCall(ViewDriverLocation.this, new IApiCaller() {
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

                //marker = mMap.addMarker(new MarkerOptions().title("Driver").position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 13));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getSource())
                        .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getDestination())
                        .position(new LatLng(location2.getLatitude(), location2.getLongitude())));
                for (JoinRidersModel rm : rlist) {
                    getPickupLocation(rm.getPickupLocation());
                    mMap.addMarker(new MarkerOptions()
                            .title(rm.getUserName())
                            .position(new LatLng(pickuplocation.getLatitude(), pickuplocation.getLongitude())));
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


                //RenderMapAndMarkers();
                mdatabase = FirebaseDatabase.getInstance().getReference();
                mdatabase = FirebaseDatabase.getInstance().getReference();
                String rideid = Integer.toString(rideId);
                mdatabase.child("rides").child(rideid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RideModelFirebase rm = new RideModelFirebase();
                        rm = dataSnapshot.getValue(RideModelFirebase.class);
                        double a = (double) rm.getLat();
                        double b = (double) rm.getLng();
                        if(marker != null){
                            marker.setPosition(new LatLng(a,b));
                        }
                        else {
                            marker = mMap.addMarker(new MarkerOptions().title("Driver").position(new LatLng(a, b)));

                        }

                        //mMap.clear();
                        //RenderMapAndMarkers();

                        //MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(a,b));
                        //marker.setPosition(new LatLng(a,b));


//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 13));


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                mMap.setMyLocationEnabled(true);
                //LatLng x = new LatLng(a.getLatitude(),a.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x,16));
            }
        });



    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void RenderMapAndMarkers(){

        mMap.addMarker(new MarkerOptions()
                .title(sm.getSource())
                .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
        mMap.addMarker(new MarkerOptions()

                .title(sm.getDestination())
                .position(new LatLng(location2.getLatitude(), location2.getLongitude())));
        for(JoinRidersModel rm : rlist){
            getPickupLocation(rm.getPickupLocation());
            mMap.addMarker(new MarkerOptions()
                    .title(rm.getUserName())
                    .position(new LatLng(pickuplocation.getLatitude(), pickuplocation.getLongitude())));
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

