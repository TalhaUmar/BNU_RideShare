package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.R;
import com.example.shjunaid.bnurideshare.RealtimeLocation;
import com.example.shjunaid.bnurideshare.ViewDriverLocation;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Models.RideModel;
import Modules.Route;

/**
 * Created by Talha on 1/6/2017.
 */
public class JoinedRideDetailFragment extends Fragment {
    private String latlng1,latlng2;
    private List<Address> add;
    private RideModel sm;
    private GoogleMap mMap;
    private List<Route> routes;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Marker originMarker;
    private Marker destinationMarker;
    private MapView mapView;
    private Address location1,location2;
    SharedPreferences sharedPreferences;

    public JoinedRideDetailFragment() {
        sm = new RideModel();
    }

    public RideModel getSm() {
        return sm;
    }

    public void setSm(RideModel sm) {
        this.sm = sm;
    }

    private TextView tx1,tx2,tx3,tx4,txsmoking,txfood,txtype,txseats,txcost;
    private Button leaveridebtn,driverlocationbtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.joined_ride_detail_fragment,container,false);
        //tx1 = (TextView)v.findViewById(R.id.joined_ride_date);
        tx2 = (TextView)v.findViewById(R.id.joined_ride_detail_date);
        tx3 = (TextView)v.findViewById(R.id.joined_ride_source);
        tx4 = (TextView)v.findViewById(R.id.joined_ride_destination);
        txsmoking = (TextView)v.findViewById(R.id.joined_ride_Detail_smoking);
        txfood = (TextView)v.findViewById(R.id.joined_ride_Detail_food);
        txtype = (TextView)v.findViewById(R.id.joined_ride_Detail_type);
        txseats = (TextView)v.findViewById(R.id.joined_ride_Detail_seats);
        txcost = (TextView)v.findViewById(R.id.joined_ride_Detail_cost);
        leaveridebtn = (Button) v.findViewById(R.id.leave_ride_button);
        driverlocationbtn = (Button) v.findViewById(R.id.view_driver_realtimelocation_button);
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
        mapView = (MapView) v.findViewById(R.id.joined_ride_detail_map);
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(abc.startLocation, 13));
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
        leaveridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sm.getRideStatusId().equals("1")) {
                    LeaveRideDialog sure = new LeaveRideDialog();
                    int rideid = sm.getId();
                    Bundle bundle = new Bundle();
                    bundle.putInt("rideid", rideid);
                    sure.setArguments(bundle);
                    sure.show(getFragmentManager(), "leave_dialog");
                } else {
                    Toast.makeText(getActivity(), "you cannot leave this ride", Toast.LENGTH_LONG).show();
                }


            }
        });
        driverlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sm.getRideStatusId().equals("2")) {
                    Intent i = new Intent(getActivity(), ViewDriverLocation.class);
                    i.putExtra("RideId",sm.getId());
                    i.putExtra("Checkpoints",sm.getCheckpoints());
                    i.putExtra("StartLocation",sm.getSource().toString());
                    i.putExtra("DestLocation",sm.getDestination().toString());
                    startActivity(i);
                }
                else {
                    Toast.makeText(getActivity(), "Ride is not started yet.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng hcmus = new LatLng(10.762963, 106.682394);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
//
//
//
//    }
}

