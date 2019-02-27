package Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.shjunaid.bnurideshare.R;

import java.util.List;

import Models.JoinRidersModel;

/**
 * Created by Talha on 1/18/2017.
 */
public class JoinedRidersAdapter extends ArrayAdapter {
    private List<JoinRidersModel> joinriderModelList;
    private int res;

    public JoinedRidersAdapter(Context context, List<JoinRidersModel> objects) {
        super(context, R.layout.custom_list_joined_riders, objects);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater stdinflate = LayoutInflater.from(getContext());
        View customView = stdinflate.inflate(R.layout.custom_list_joined_riders, parent, false);
        final JoinRidersModel single_std = (JoinRidersModel) getItem(position);
        TextView stdtxt1 = (TextView)customView.findViewById(R.id.joinedrider_pickup);
        TextView stdtxt2 = (TextView)customView.findViewById(R.id.joinedrider_destination);
        TextView stdtxt3 = (TextView)customView.findViewById(R.id.joinedrider_name_txt);
        TextView stdtxt4 = (TextView)customView.findViewById(R.id.totalamount_txt);
        Button   viewbtn = (Button)customView.findViewById(R.id.view_joined_user);
        Button   kickbtn = (Button)customView.findViewById(R.id.kick_user);


        stdtxt1.setText(single_std.getPickupLocation());
        stdtxt2.setText(single_std.getDestination());
        stdtxt3.setText(single_std.getUserName());
        stdtxt4.setText(Integer.toString(single_std.getTotalAmount()));

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = parent.getContext();
                FragmentManager fm = ((Activity) context).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                UserDetailFragment prf = new UserDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("passengerid", single_std.getPassengerId());
                bundle.putInt("rideid", single_std.getRideId());
                prf.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                ft.replace(R.id.main_content_area, prf);
                ft.addToBackStack("prf");
                ft.commit();

            }
        });
        kickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = parent.getContext();
                FragmentManager fm = ((Activity) context).getFragmentManager();
                RemoveRiderDialog leaveRideDialog = new RemoveRiderDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("rideid",single_std.getRideId());
                bundle.putInt("passengerid",single_std.getPassengerId());
                leaveRideDialog.setArguments(bundle);
                leaveRideDialog.show(fm, "remove_ride_dialog");
            }
        });
//        stdtxt5.setText(single_std.getAvailableSeats()+" seats");
//        stdtxt6.setText(single_std.getCostPerKm()+"/Km");
        return customView;

    }
}