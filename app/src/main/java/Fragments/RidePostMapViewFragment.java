package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import API.ApiCallForRidePost;
import API.IApiCaller;
import Models.RideModel;
import Models.UserModel;
import Modules.DirectionFinder;
import Modules.Route;

/**
 * Created by Talha on 12/28/2016.
 */
public class RidePostMapViewFragment extends Fragment {
    private String latlng1,latlng2;
    private List<Address> add;
    private GoogleMap mMap;
    private List<Route> routes;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private Polyline polylinepoints;
    private Marker originMarker;
    private Marker destinationMarker;
    private MapView mapView;
    private EditText source,destination;
    Polyline poly;

    private Button postButton;
    private TextView checkpointerrorshow;
    private RideModel rideModel = new RideModel();;
    private String ridemodelstring;
    private Button getroute;
    String s1 = "";
    String d1 = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ridemodelstring = getArguments().getString("RideModelObj");
        View v = inflater.inflate(R.layout.ridepost_mapview_fragment,container,false);
        source = (EditText)v.findViewById(R.id.source_place);
        destination = (EditText)v.findViewById(R.id.destination_place);
        checkpointerrorshow = (TextView) v.findViewById(R.id.checkpoint_error_txt);
        getroute = (Button)v.findViewById(R.id.get_route);
        final Gson g = new Gson();
        rideModel = g.fromJson(ridemodelstring, RideModel.class);
        routes = new ArrayList<Route>();
        add = new ArrayList<>();
//        Geocoder geocoder = new Geocoder(getActivity());
//        add = new ArrayList<>();
//        try {
//            add = geocoder.getFromLocationName(rideModel.getSource().toString(), 1);
//            Address location1 = add.get(0);
//            latlng1 = Double.toString(location1.getLatitude());
//            latlng1 = latlng1.concat(",");
//            latlng1 = latlng1.concat(Double.toString(location1.getLongitude()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        add.clear();
//        try {
//            add = geocoder.getFromLocationName(rideModel.getDestination().toString(), 1);
//            Address location2 = add.get(0);
//            latlng2 = Double.toString(location2.getLatitude());
//            latlng2 = latlng2.concat(",");
//            latlng2 = latlng2.concat(Double.toString(location2.getLongitude()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mapView = (MapView) v.findViewById(R.id.ride_post_map);
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
//                routes = new ArrayList<Route>();
//                try {
//                    routes = new DirectionFinder(latlng1,latlng2).execute();
//                    onPathFound(routes);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                    public void onPolylineClick(Polyline polyline) {
                        //int strokeColor = polyline.getColor() ^ 0x0000CC00;
                        for (int i = 0; i < polylinePaths.size(); i++){
                            poly = polylinePaths.get(i);
                            if (poly.getPoints().toString().equals(polyline.getPoints().toString())){
                                polyline.setColor(Color.parseColor("#2cb22c"));
                            }
                            else{
                                poly.setColor(Color.parseColor("#2051E1"));
                            }

                        }
                        rideModel.setCheckpoints(polyline.getPoints().toString());
                        Log.e("TAG", "Polyline points @ " + polyline.getPoints());
                    }
                });
            }
        });
        return v;
    }

    public void getroute(String l1 , String l2){


        try {
            routes = new DirectionFinder(l1,l2).execute();
            onPathFound(routes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void onPathFound(List<Route> routes) {
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
        LatLng a = new LatLng(31.392003, 74.287901);
        LatLng b = new LatLng(31.600712, 74.293237);
        final LatLngBounds lahorebounds = new LatLngBounds(a,b);
        final AutocompleteFilter typefilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE).build();
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(lahorebounds).build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setBoundsBias(lahorebounds).build(getActivity());
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        postButton = (Button) getActivity().findViewById(R.id.postRide_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rideModel.getCheckpoints()!= null){
                    ApiCallForRidePost apiCallForRidePost = new ApiCallForRidePost(getActivity(), new IApiCaller() {
                        @Override
                        public void onResult(String data) {
                            if (data.equals("200")){
                                Toast.makeText(getActivity(),"Ride posted successfully.",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(), HomeActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        }
                    });
                    apiCallForRidePost.execute(rideModel);

                }
                else {
                    checkpointerrorshow.setText("Select a route to post the ride !");
                }

            }
        });

        getroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String a = source.getText().toString();
                String b  = destination.getText().toString();
                if(a.toString().equals("")){

                        source.setError("Enter source");
                        flag=false;

                }
                if(b.toString().equals("")){
                    destination.setError("Enter destination");
                    flag = false;
                }
                if(a.equals(b)){
                    source.setError("Source and destination cannot be same");
                    flag = false;
                }
                if(!(a.equals("Beaconhouse National University Tarogil Campus") || b.equals("Beaconhouse National University Tarogil Campus"))){
                    source.setError("You cannot post rides out of the university");
                    flag = false;
                }
                if(flag == true){
                    rideModel.setSource(a);
                    rideModel.setDestination(b);
                    add.clear();
                    Geocoder geocoder = new Geocoder(getActivity());
                    add = new ArrayList<>();
                    try {
                        add = geocoder.getFromLocationName(rideModel.getSource().toString(), 1);
                        Address location1 = add.get(0);
                        latlng1 = Double.toString(location1.getLatitude());
                        latlng1 = latlng1.concat(",");
                        latlng1 = latlng1.concat(Double.toString(location1.getLongitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    add.clear();
                    try {
                        add = geocoder.getFromLocationName(rideModel.getDestination().toString(), 1);
                        Address location2 = add.get(0);
                        latlng2 = Double.toString(location2.getLatitude());
                        latlng2 = latlng2.concat(",");
                        latlng2 = latlng2.concat(Double.toString(location2.getLongitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    routes.clear();
                    mMap.clear();
                    getroute(latlng1,latlng2);
                }





            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                source.setText(place.getName());
                source.setError(null);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }


        }
        else if (requestCode == 2) {
            if (resultCode == getActivity().RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                //Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                destination.setText(place.getName());
                destination.setError(null);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }
}
