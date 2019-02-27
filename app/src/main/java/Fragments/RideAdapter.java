package Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shjunaid.bnurideshare.R;

import java.util.List;

import Models.RideModel;
import Models.UserModel;

/**
 * Created by Sh Junaid on 12/17/2016.
 */
public class RideAdapter extends ArrayAdapter {
    private List<RideModel> studentList;
    private int res;

    public RideAdapter(Context context, List<RideModel> objects) {
        super(context, R.layout.custom_row, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater stdinflate = LayoutInflater.from(getContext());
        View customView = stdinflate.inflate(R.layout.custom_row, parent, false);
        RideModel single_std = (RideModel) getItem(position);
        TextView stdtxt1 = (TextView)customView.findViewById(R.id.ride_source);
        TextView stdtxt2 = (TextView)customView.findViewById(R.id.ride_destination);
        TextView stdtxt3 = (TextView)customView.findViewById(R.id.ride_date);
        TextView stdtxt4 = (TextView)customView.findViewById(R.id.ride_time);
        TextView stdtxt5 = (TextView)customView.findViewById(R.id.ride_seats);
        TextView stdtxt6 = (TextView)customView.findViewById(R.id.ride_cost);


        stdtxt1.setText(single_std.getSource());
        stdtxt2.setText(single_std.getDestination());
        stdtxt3.setText(single_std.getDepartureDate());
        stdtxt4.setText(single_std.getDepartureTime());
        stdtxt5.setText(single_std.getAvailableSeats()+" seats");
        stdtxt6.setText(single_std.getCostPerKm()+"/Km");
        return customView;

    }
}
