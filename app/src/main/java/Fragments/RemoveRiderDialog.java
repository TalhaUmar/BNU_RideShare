package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;

import java.util.concurrent.ExecutionException;

import API.LeaveRideCall;
import API.RemovePassengerCall;
import Models.AppConstants;

/**
 * Created by Talha on 1/19/2017.
 */
public class RemoveRiderDialog extends DialogFragment {
    private Button yes,no;
    private TextView qtxt;
    SharedPreferences sharedPreferences;
    LayoutInflater inflater;
    View v;
    View v1;
    DialogInterface dialogInterface;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.are_you_sure_dialog, null);
        yes = (Button) v.findViewById(R.id.yes_btn);
        no = (Button) v.findViewById(R.id.no_btn);
        qtxt = (TextView) v.findViewById(R.id.are_you_sure_txt);
        qtxt.setText("Are you sure to remove this passenger");
        v1 = inflater.inflate(R.layout.custom_title, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCustomTitle(v1);
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
                int userid = getArguments().getInt("passengerid");
                int rideid = getArguments().getInt("rideid");
                RemovePassengerCall removePassenger = new RemovePassengerCall();
                removePassenger.execute(userid, rideid);
                try {
                    String data = (String) removePassenger.get();
                    if (data.equals("200")) {
                        Toast.makeText(getActivity(), "Passenger successfully removed", Toast.LENGTH_LONG).show();
                        Fragment pre = getFragmentManager().findFragmentByTag("remove_ride_dialog");
                        if (pre != null) {
                            DialogFragment df = (DialogFragment) pre;
                            df.dismiss();
                        }
                        getFragmentManager().popBackStack();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment pre = getFragmentManager().findFragmentByTag("remove_ride_dialog");
                if (pre != null) {
                    DialogFragment df = (DialogFragment) pre;
                    df.dismiss();
                }
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dialog.dismiss();
    }
}

