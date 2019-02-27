package Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import API.RideRequestCall;
import API.RideRequestExistCall;
import API.UserProfileRatingCall;
import Models.AppConstants;
import Models.RideModel;
import Models.RideRequestModel;
import Models.UserProfileRatingModel;
import Modules.DirectionFinder;
import Modules.Route;

/**
 * Created by Sh Junaid on 12/22/2016.
 */
public class RideDetailFragment extends Fragment  {
    private String latlng1,latlng2;
    private List<Address> add;
    private RideModel sm;
    private GoogleMap mMap;
    private List<Route> routes;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Marker originMarker;
    private Marker destinationMarker;
    private MapView mapView;
    private String SearchPickup,SearchDestination,pamount;
    private int pdistance;

    private Address location1,location2,spickups,sdestination;
    SharedPreferences sharedPreferences;
    private UserProfileRatingModel userProfile = new UserProfileRatingModel();

    public RideDetailFragment() {
        sm = new RideModel();
    }

    public RideModel getSm() {
        return sm;
    }

    public void setSm(RideModel sm) {
        this.sm = sm;
    }

    private TextView tx1,tx2,tx3,tx4,txsmoking,txfood,txtype,txseats,txcost,txamount;
    private Button pickridebtn,estimatebtn,viewriderbtn;
    private String action = "RideRequest";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ride_detail_fragment,container,false);
        tx1 = (TextView)v.findViewById(R.id.date);
        tx2 = (TextView)v.findViewById(R.id.time);
        tx3 = (TextView)v.findViewById(R.id.source);
        tx4 = (TextView)v.findViewById(R.id.destination);
        txsmoking = (TextView)v.findViewById(R.id.ride_Detail_smoking);
        txfood = (TextView)v.findViewById(R.id.ride_Detail_food);
        txtype = (TextView)v.findViewById(R.id.ride_Detail_type);
        txseats = (TextView)v.findViewById(R.id.ride_Detail_seats);
        txcost = (TextView)v.findViewById(R.id.ride_Detail_cost);


        pickridebtn = (Button) v.findViewById(R.id.pickRide_button);
        estimatebtn = (Button) v.findViewById(R.id.btn_amount);
        viewriderbtn = (Button) v.findViewById(R.id.view_rider_button);

        SearchPickup = getArguments().getString("PickupLocation");
        SearchDestination = getArguments().getString("Destination");




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
            add = geocoder.getFromLocationName(SearchPickup.toString(), 1);
            spickups = add.get(0);
//            latlng1 = Double.toString(location1.getLatitude());
//            latlng1 = latlng1.concat(",");
//            latlng1 = latlng1.concat(Double.toString(location1.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        try {
            add = geocoder.getFromLocationName(SearchDestination.toString(), 1);
            sdestination = add.get(0);
//            latlng1 = Double.toString(location1.getLatitude());
//            latlng1 = latlng1.concat(",");
//            latlng1 = latlng1.concat(Double.toString(location1.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        add.clear();
        try {
            add = geocoder.getFromLocationName(sm.getSource().toString(), 1);
            location1 = add.get(0);
//            latlng1 = Double.toString(location1.getLatitude());
//            latlng1 = latlng1.concat(",");
//            latlng1 = latlng1.concat(Double.toString(location1.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        try {
            add = geocoder.getFromLocationName(sm.getDestination().toString(), 1);
            location2 = add.get(0);
//            latlng2 = Double.toString(location2.getLatitude());
//            latlng2 = latlng2.concat(",");
//            latlng2 = latlng2.concat(Double.toString(location2.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        double dis = SphericalUtil.computeDistanceBetween(new LatLng(spickups.getLatitude(), spickups.getLongitude()), new LatLng(sdestination.getLatitude(), sdestination.getLongitude()));
        dis = dis/1000;
        int h =(int)dis;
        int am = Integer.parseInt(sm.getCostPerKm());
        am = am*h;
        String z  = Integer.toString(am);
        pdistance = h;
        pamount = z;

        mapView = (MapView) v.findViewById(R.id.ride_detail_map);
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()),11));
                mMap.addMarker(new MarkerOptions()
                        .title(sm.getSource())
                        .position(new LatLng(location1.getLatitude(), location1.getLongitude())));
                mMap.addMarker(new MarkerOptions()

                        .title(sm.getDestination())
                        .position(new LatLng(location2.getLatitude(), location2.getLongitude())));
                PolylineOptions polylineOptions = new PolylineOptions();

                polylineOptions.
                        geodesic(true).
                        color(Color.parseColor("#2cb22c")).
                        width(20).
                        clickable(true);
                List<LatLng> latLngList = new ArrayList<LatLng>();
                ArrayList<Location> locations = new ArrayList<Location>();
                String parsestring = sm.getCheckpoints();
                parsestring = parsestring.replace("[","");
                parsestring = parsestring.replace("]","");
                parsestring = parsestring.replace("lat/lng: (","");
                parsestring = parsestring.replace(")","");
                ArrayList<String> stringArrayList = new ArrayList<String>(Arrays.asList(parsestring.split(",")));
                for (int i = 0; i< stringArrayList.size(); i+=2){
                    //Location j = new Location("");
                    //j.setLatitude(Double.parseDouble(stringArrayList.get(i)));
                    //j.setLongitude(Double.parseDouble(stringArrayList.get(i+1)));
                    LatLng abc = new LatLng(Double.parseDouble(stringArrayList.get(i)),Double.parseDouble(stringArrayList.get(i+1)));
                    latLngList.add(abc);
                    //locations.add(new Location(j));
                }

                for (int j = 0; j < latLngList.size(); j++)
                {
                    polylineOptions.add(latLngList.get(j));
                }

                polylinePaths.add(mMap.addPolyline(polylineOptions));
//                routes = new ArrayList<Route>();
//                try {
//                    //routes = new DirectionFinder(latlng1,latlng2).execute();
//                    onPathFound(routes);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//                    public void onPolylineClick(Polyline polyline) {
//                        //int strokeColor = polyline.getColor() ^ 0x0000CC00;
//                        for (int i = 0; i < polylinePaths.size(); i++){
//                            Polyline poly = polylinePaths.get(i);
//                            if (poly.getPoints().equals(polyline.getPoints())){
//                                polyline.setColor(Color.parseColor("#2cb22c"));
//                            }
//                            else{
//                                polyline.setColor(Color.parseColor("#2051E1"));
//                            }
//
//                        }
//                        Log.e("TAG", "Polyline points @ " + polyline.getPoints());
//                    }
//                });
            }
        });
        return v;
    }



    public void onPathFound(List<Route> routes){
        Route abc = new Route();
        int colorcode;
        for (int i = 0; i < routes.size(); i++) {
            abc = routes.get(i);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(abc.startLocation, 11));

            mMap.addMarker(new MarkerOptions()
                    .title(abc.startAddress)
                    .position(abc.startLocation));
            mMap.addMarker(new MarkerOptions()

                    .title(abc.endAddress)
                    .position(abc.endLocation));
            PolylineOptions polylineOptions = new PolylineOptions();

            polylineOptions.
                    geodesic(true).
                    color(Color.parseColor("#2051E1")).
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
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
        final int userid = sharedPreferences.getInt(AppConstants.key_userid, 0);
        final int rideid = sm.getId();

        pickridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                RideRequestExistCall requestExist = new RideRequestExistCall();
                requestExist.execute(userid, rideid);
                try {
                    String data = (String) requestExist.get();
                    if (data.equals("200")){
                        Toast.makeText(getActivity(),"You cannot request again",Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String ploc = SearchPickup;
                String des = SearchDestination;
                RideRequestModel rideRequestModel = new RideRequestModel();
                rideRequestModel.setPassengerId(userid);
                rideRequestModel.setRideId(rideid);
                rideRequestModel.setPickupLocation(ploc);
                rideRequestModel.setDestination(des);
                rideRequestModel.setTotalAmount(Integer.parseInt(pamount));
                if(userid != sm.getUserId()){
                    if (flag == true){
                        RideRequestCall rideRequestCall = new RideRequestCall();
                        rideRequestCall.execute(rideRequestModel);
                        try {
                            String data = (String) rideRequestCall.get();
                            if(data != "404")
                            {
                                Toast.makeText(getActivity(),"Your ride join request submitted successfully",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(),HomeActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(),"you cannot request for this ride",Toast.LENGTH_LONG).show();
                }

            }
        });
        estimatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmountCalculateDialog myDialog = new AmountCalculateDialog();
                myDialog.setDis(pdistance);
                myDialog.setAm(pamount);
                myDialog.show(getFragmentManager(), "amount_dialog");
            }
        });

        viewriderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                DriverDetailFragment ddf = new DriverDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("driverid", sm.getUserId());
                bundle.putInt("rideid", sm.getId());
                ddf.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                ft.replace(R.id.main_content_area, ddf);
                ft.addToBackStack("ddf");
                ft.commit();
            }
        });


    }

}
