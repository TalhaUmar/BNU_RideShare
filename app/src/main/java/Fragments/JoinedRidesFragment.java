package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
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
import API.ApiCallForJoinedRides;
import API.IApiCaller;
import Models.AppConstants;
import Models.RideModel;

/**
 * Created by Talha on 1/8/2017.
 */
public class JoinedRidesFragment extends Fragment {
    private ArrayList<RideModel> lst_std;
    private ListView lst_view;
    private List<Address> add;
    private List<Address> add1;
    private Address location1,location2;
    private List<RideModel> rlist;
    SharedPreferences sharedPreferences;
    private boolean checkresultflag = false;
    private TextView noresultfound;
    private ImageView noresultimage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_ride_fragment, container, false);
        //View v2 = inflater.inflate(R.layout.no_result_found_fragment,container,false);
        noresultfound = (TextView) v.findViewById(R.id.no_result_found_txt);
        noresultimage = (ImageView) v.findViewById(R.id.noresultfound_image);
        lst_view  = (ListView) v.findViewById(R.id.rides_list);
        new ArrayList<RideModel>();
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt(AppConstants.key_userid, 0);
        ApiCallForJoinedRides joinedRides = new ApiCallForJoinedRides(getActivity(), new IApiCaller() {
            @Override
            public void onResult(String data) {
                if (data != "404" && data != "") {
                    Gson gson = new Gson();
                    rlist = gson.fromJson(data, new TypeToken<ArrayList<RideModel>>() {}.getType());
                    lst_std = new ArrayList<RideModel>();
                    for(RideModel rm : rlist){
                        lst_std.add(rm);
                    }
                    ListAdapter lst = new RideAdapter(getActivity(),lst_std);
                    lst_view.setAdapter(lst);
                }
                else {
                    noresultfound.setText("No Result Found!!");
                    noresultimage.setVisibility(View.VISIBLE);
                }
            }
        });
        joinedRides.execute(userid);
//        try {
//            String data = (String) joinedRides.get();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

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
                JoinedRideDetailFragment prf = new JoinedRideDetailFragment();
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                ft.replace(R.id.main_content_area, prf);
                prf.setSm(sm);
                ft.addToBackStack("prf");
                ft.commit();
            }
        });
    }
}
