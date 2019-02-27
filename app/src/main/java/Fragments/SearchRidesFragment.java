package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.MainActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import API.ApiCallForAllRides;
import Models.RideModel;

/**
 * Created by Sh Junaid on 12/17/2016.
 */
public class SearchRidesFragment extends Fragment {
    private ArrayList<RideModel> lst_std;
    private ListView lst_view;
    private String PickLocation,Destination;
    private List<Address> add;
    private List<Address> add1;
    private Address location1,location2;
    List<RideModel> listtodisplay = new ArrayList<RideModel>();
    private List<RideModel> rlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_ride_fragment,container,false);

        new ArrayList<RideModel>();

        ApiCallForAllRides apiCallForAllRides = new ApiCallForAllRides(getActivity());
        apiCallForAllRides.execute();
        try {
            String data = (String) apiCallForAllRides.get();
            if (data != "404") {
                Gson gson = new Gson();
                rlist = gson.fromJson(data, new TypeToken<ArrayList<RideModel>>() {}.getType());
            }
            else {
                Toast.makeText(getActivity(),"No Search Results Found!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Geocoder geocoder = new Geocoder(getActivity());

        add = new ArrayList<>();


        try {
            add = geocoder.getFromLocationName(PickLocation, 1);

            location1 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        add.clear();
        try {
            add = geocoder.getFromLocationName(Destination, 1);

            location2 = add.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<LatLng> latLngList = new ArrayList<LatLng>();
        ArrayList<Location> locations = new ArrayList<Location>();
        for(RideModel rm : rlist){
            latLngList.clear();
            locations.clear();

            String parsestring = rm.getCheckpoints();
            parsestring = parsestring.replace("[","");
            parsestring = parsestring.replace("]","");
            parsestring = parsestring.replace("lat/lng: (","");
            parsestring = parsestring.replace(")","");
            ArrayList<String> stringArrayList = new ArrayList<String>(Arrays.asList(parsestring.split(",")));
            for (int i = 0; i< stringArrayList.size(); i+=2){
                LatLng abc = new LatLng(Double.parseDouble(stringArrayList.get(i)),Double.parseDouble(stringArrayList.get(i+1)));
                latLngList.add(abc);
            }
            LatLng point = new LatLng(location1.getLatitude(),location1.getLongitude());
            LatLng point2 = new LatLng(location2.getLatitude(),location2.getLongitude());
            boolean chk =PolyUtil.isLocationOnPath(point,latLngList,false,500);
            boolean chk2 =PolyUtil.isLocationOnPath(point2,latLngList,false,500);

            if(chk == true && chk2 == true){
                listtodisplay.add(rm);
            }

        }
        lst_std = new ArrayList<RideModel>();
        for(RideModel rm : listtodisplay){
            lst_std.add(rm);
        }

        lst_view  = (ListView)v.findViewById(R.id.rides_list);
        ListAdapter lst = new RideAdapter(getActivity(),lst_std);
        lst_view.setAdapter(lst);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RideModel sm = (RideModel) adapterView.getItemAtPosition(i);
                //Toast.makeText(getActivity(), sm.getSource(), Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RideDetailFragment prf = new RideDetailFragment();
                Bundle data = new Bundle();
                data.putString("PickupLocation",PickLocation);
                data.putString("Destination",Destination);
                prf.setArguments(data);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.main_content_area, prf);
                prf.setSm(sm);
                ft.addToBackStack("prf");
                ft.commit();
            }
        });
    }

    public String getPickLocation() {
        return PickLocation;
    }

    public void setPickLocation(String pickLocation) {
        PickLocation = pickLocation;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }
}
