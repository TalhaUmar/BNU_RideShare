package Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.MainActivity;
import com.example.shjunaid.bnurideshare.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

import API.AcceptRejectRideRequestCall;
import Models.JoinRidersModel;


/**
 * Created by Talha on 1/19/2017.
 */
public class RideRequestsAdapter extends ArrayAdapter {
    private List<JoinRidersModel> riderequestsModelList;
    private int res;

    public RideRequestsAdapter(Context context, List<JoinRidersModel> objects) {
        super(context, R.layout.custom_list_ride_requests, objects);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater stdinflate = LayoutInflater.from(getContext());
        View customView = stdinflate.inflate(R.layout.custom_list_ride_requests, parent, false);
        final JoinRidersModel single_std = (JoinRidersModel) getItem(position);
        TextView stdtxt1 = (TextView)customView.findViewById(R.id.requested_rider_pickup);
        TextView stdtxt2 = (TextView)customView.findViewById(R.id.requested_rider_destination);
        TextView stdtxt3 = (TextView)customView.findViewById(R.id.requested_rider_name_txt);
        Button viewbtn = (Button)customView.findViewById(R.id.view_requested_rider_profile);
        Button acceptbtn = (Button)customView.findViewById(R.id.rider_request_accept_btn);
        Button rejectbtn = (Button)customView.findViewById(R.id.rider_request_reject_btn);

        stdtxt1.setText(single_std.getPickupLocation());
        stdtxt2.setText(single_std.getDestination());
        stdtxt3.setText(single_std.getUserName());

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = parent.getContext();
                FragmentManager fm = ((Activity) context).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                UserDetailFragment prf = new UserDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("passengerid", single_std.getPassengerId());
                prf.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                ft.replace(R.id.main_content_area, prf);
                ft.addToBackStack("prf");
                ft.commit();

            }
        });
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "AcceptRequest";
                AcceptRejectRideRequestCall acceptRideRequest = new AcceptRejectRideRequestCall();
                acceptRideRequest.execute(single_std.getPassengerId(), single_std.getRideId(), action);
                try {
                    String data = (String) acceptRideRequest.get();
                    if(data != "404")
                    {
                        Toast.makeText(parent.getContext(), "Your response submitted successfully", Toast.LENGTH_LONG).show();
                        Context context = parent.getContext();
                        FragmentManager fm = ((Activity) context).getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        RideRequestsFragment rrf = new RideRequestsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("passengerid", single_std.getPassengerId());
                        rrf.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                        ft.replace(R.id.main_content_area, rrf);
                        ft.commit();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "RejectRequest";
                AcceptRejectRideRequestCall rejectRideRequest = new AcceptRejectRideRequestCall();
                rejectRideRequest.execute(single_std.getPassengerId(),single_std.getRideId(),action);
                try {
                    String data = (String) rejectRideRequest.get();
                    if(data != "404")
                    {
                        Toast.makeText(parent.getContext(), "Your response submitted successfully", Toast.LENGTH_LONG).show();
                        Context context = parent.getContext();
                        FragmentManager fm = ((Activity) context).getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        RideRequestsFragment rrf = new RideRequestsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("passengerid", single_std.getPassengerId());
                        rrf.setArguments(bundle);
                        ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                        ft.replace(R.id.main_content_area, rrf);
                        ft.commit();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
//        stdtxt5.setText(single_std.getAvailableSeats()+" seats");
//        stdtxt6.setText(single_std.getCostPerKm()+"/Km");
        return customView;

    }
}
