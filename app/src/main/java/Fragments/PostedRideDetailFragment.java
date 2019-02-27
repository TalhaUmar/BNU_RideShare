package Fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.GPS_Service;
import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.example.shjunaid.bnurideshare.RealtimeLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import API.CancelRideCall;
import API.RideRequestCall;
import Models.AppConstants;
import Models.RideModel;
import Models.RideModelFirebase;
import Modules.Route;

/**
 * Created by Talha on 1/8/2017.
 */
public class PostedRideDetailFragment extends Fragment {
    private String latlng1, latlng2;
    private List<Address> add;
    private RideModel sm;
    private GoogleMap mMap;
    private List<Route> routes;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Marker originMarker;
    private Marker destinationMarker;
    private MapView mapView;
    private Address location1, location2;
    SharedPreferences sharedPreferences;
    private BroadcastReceiver broadcastReceiver;
    private DatabaseReference mdatabase;

    public PostedRideDetailFragment() {
        sm = new RideModel();
    }

    public RideModel getSm() {
        return sm;
    }

    public void setSm(RideModel sm) {
        this.sm = sm;
    }

    private TextView tx1,tx2,tx3,tx4,txsmoking,txfood,txtype,txseats,txcost;
    private Button editridebtn, cancelridebtn, startridebtn,joiningsbtn,kickbtn;
    private String action = "RideRequest";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.posted_ride_detail_fragment, container, false);
        tx2 = (TextView)v.findViewById(R.id.posted_ride_detail_date);
        tx3 = (TextView)v.findViewById(R.id.posted_ride_source);
        tx4 = (TextView)v.findViewById(R.id.posted_ride_destination);
        txsmoking = (TextView)v.findViewById(R.id.posted_ride_Detail_smoking);
        txfood = (TextView)v.findViewById(R.id.posted_ride_Detail_food);
        txtype = (TextView)v.findViewById(R.id.posted_ride_Detail_type);
        txseats = (TextView)v.findViewById(R.id.posted_ride_Detail_seats);
        txcost = (TextView)v.findViewById(R.id.posted_ride_Detail_cost);
        editridebtn = (Button) v.findViewById(R.id.edit_Ride_button);
        cancelridebtn = (Button) v.findViewById(R.id.cancel_Ride_button);
        startridebtn = (Button) v.findViewById(R.id.start_Ride_button);
        joiningsbtn = (Button)v.findViewById(R.id.joinings_Ride_button);


        tx3.setText(" From : "+sm.getSource());
        tx4.setText(" To : "+sm.getDestination());

        if(sm.getSmoking().equals("1")){
            txsmoking.setText("Smoking is not allowed");
        }
        else{
            txsmoking.setText("Smoking is allowed");
        }
        if(sm.getFoodDrinks().equals("1")){
            txfood.setText("Food is not allowed");
        }
        else{
            txfood.setText("Food is allowed");
        }
        if(sm.getVehicleType().equals("2")){
            txtype.setText("Bike");
        }
        else{
            txtype.setText("Car");
        }
        txcost.setText(sm.getCostPerKm()+"/km");
        txseats.setText(sm.getAvailableSeats()+" seats available");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String depdate = sm.getDepartureDate().toString();
        String deptime = sm.getDepartureTime().toString();
        deptime = deptime.concat(":00");
        depdate = depdate.concat(" "+deptime);
        Date d1 = null;
        try {
            d1 = format.parse(depdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tx2.setText(d1.toString());

        Geocoder geocoder = new Geocoder(getActivity());
        add = new ArrayList<>();
        try {
            add = geocoder.getFromLocationName(sm.getSource().toString(), 1);
            location1 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        try {
            add = geocoder.getFromLocationName(sm.getDestination().toString(), 1);
            location2 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView = (MapView) v.findViewById(R.id.posted_ride_detail_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 13));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getSource())
                        .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getDestination())
                        .position(new LatLng(location2.getLatitude(), location2.getLongitude())));
                PolylineOptions polylineOptions = new PolylineOptions();

                polylineOptions.
                        geodesic(true).
                        color(Color.parseColor("#2051E1")).
                        width(20).
                        clickable(true);
                List<LatLng> latLngList = new ArrayList<LatLng>();
                ArrayList<Location> locations = new ArrayList<Location>();
                String parsestring = sm.getCheckpoints();
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


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
            }
        });



//        mdatabase= FirebaseDatabase.getInstance().getReference();
//        mdatabase.child("rides").child("1004").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                RideModelFirebase rm = new RideModelFirebase();
//                rm = dataSnapshot.getValue(RideModelFirebase.class);
//                double a = (double) rm.getLat();
//                double b = (double) rm.getLng();
//                //mMap.clear();
//
//
//                //MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(a,b));
//                //marker.setPosition(new LatLng(a,b));
//
//                //mMap.addMarker(new MarkerOptions().title("my location").position(new LatLng(a, b)));
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 16));
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return v;
    }




    public void onPathFound(List<Route> routes){
        Route abc = new Route();
        int colorcode;
        for (int i = 0; i < routes.size(); i++) {
            abc = routes.get(i);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(abc.startLocation, 13));
            mMap.addMarker(new MarkerOptions()
                    .title(abc.startAddress)
                    .position(abc.startLocation));
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker))
                    .title(abc.endAddress)
                    .position(abc.endLocation));
            PolylineOptions polylineOptions = new PolylineOptions();

            polylineOptions.
                    geodesic(true).
                    color(Color.parseColor("#2cb22c")).
                    width(20).
                    clickable(true);

            for (int j = 0; j < abc.points.size(); j++)
            {
                polylineOptions.add(abc.points.get(j));
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }


    }






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String currentdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                String depdate = sm.getDepartureDate();
                String deptime = sm.getDepartureTime();
                deptime = deptime.concat(":00");
                depdate = depdate.concat(" " + deptime);
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = format.parse(depdate);
                    d2 = format.parse(currentdate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int a = d2.compareTo(d1);
                if (a != 0 || a < 0) {

                    Intent i = new Intent(getActivity(), RealtimeLocation.class);
                    i.putExtra("RideId",sm.getId());
                    i.putExtra("Checkpoints",sm.getCheckpoints());
                    i.putExtra("StartLocation",sm.getSource().toString());
                    i.putExtra("DestLocation",sm.getDestination().toString());
                    startActivity(i);


                } else {
                    Toast.makeText(getActivity(), "ride cannot be started before time", Toast.LENGTH_LONG).show();
                }

            }
        });
        editridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                EditRideFragment erf = new EditRideFragment();
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.main_content_area, erf);
                erf.setRm(sm);
                ft.addToBackStack("erf");
                ft.commit();
            }
        });
        cancelridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sm.getRideStatusId().equals("2")) {
                    Toast.makeText(getActivity(),"You cannot cancel a started ride",Toast.LENGTH_LONG).show();
                }
                else{
                    CancelRideCall cancelRide = new CancelRideCall();
                    cancelRide.execute(sm.getId());
                    try {
                        String data = (String) cancelRide.get();
                        if (data.equals("200")){
                            Toast.makeText(getActivity(),"You successfully cancelled this ride",Toast.LENGTH_LONG).show();
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            PostedRidesFragment prf = new PostedRidesFragment();
                            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                            ft.replace(R.id.main_content_area, prf);
                            ft.commit();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        joiningsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RideJoiningsFragment rjf = new RideJoiningsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("RideId",sm.getId());
                rjf.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.main_content_area, rjf);
                ft.addToBackStack("rjf");
                ft.commit();

            }
        });
    }


}
