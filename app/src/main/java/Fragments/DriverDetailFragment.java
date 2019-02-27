package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import API.ApiCallForPostedRides;
import API.UserProfileRatingCall;
import Models.RideModel;
import Models.UserProfileRatingModel;

/**
 * Created by Sh Junaid on 1/18/2017.
 */
public class DriverDetailFragment extends Fragment {
    private UserProfileRatingModel userProfileRatingModel = new UserProfileRatingModel();
    private Button call;
    private TextView r1,r2,r3,r4,r5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.driver_detail_fragment,container,false);

        TextView name = (TextView)v.findViewById(R.id.driver_user_name);
        TextView ID = (TextView)v.findViewById(R.id.driver_std_id);

        call = (Button)v.findViewById(R.id.driver_call_btn);
        r1 = (TextView)v.findViewById(R.id.one_star_rating);
        r2 = (TextView)v.findViewById(R.id.two_star_rating);
        r3 = (TextView)v.findViewById(R.id.three_star_rating);
        r4 = (TextView)v.findViewById(R.id.four_star_rating);
        r5 = (TextView)v.findViewById(R.id.five_star_rating);

        int userid = getArguments().getInt("driverid");

        UserProfileRatingCall userProfileRatingCall = new UserProfileRatingCall();
        userProfileRatingCall.execute(userid);
        try {
            String data = (String) userProfileRatingCall.get();
            if (data != "404") {
                Gson gson = new Gson();
                userProfileRatingModel = gson.fromJson(data,UserProfileRatingModel.class);

            }
            else {
                Toast.makeText(getActivity(), "No Search Results Found!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        name.setText(userProfileRatingModel.getUserName());
        ID.setText(userProfileRatingModel.getStudent_EmployeeId());
        r1.setText(userProfileRatingModel.getOneStar()+" rides");
        r2.setText(userProfileRatingModel.getTwoStar()+" rides");
        r3.setText(userProfileRatingModel.getThreeStar()+" rides");
        r4.setText(userProfileRatingModel.getFourStar()+" rides");
        r5.setText(userProfileRatingModel.getFiveStar()+" rides");


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int phonestatus = userProfileRatingModel .getPhoneStatus();
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phonestatus == 0){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + userProfileRatingModel.getPhone()));
                    startActivity(callIntent);
                }
                else {
                    Toast.makeText(getActivity(),"Phone number is private",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
