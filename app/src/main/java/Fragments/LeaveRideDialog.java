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
import android.widget.EditText;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.HomeActivity;
import com.example.shjunaid.bnurideshare.R;

import java.util.concurrent.ExecutionException;

import API.LeaveRideCall;
import API.RideRequestCall;
import API.UserCredentialUpdateCall;
import Models.AppConstants;

/**
 * Created by Talha on 1/8/2017.
 */
public class LeaveRideDialog extends DialogFragment {
    private Button yes,no;
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
                int userid = sharedPreferences.getInt(AppConstants.key_userid, 0);
                int rideid = getArguments().getInt("rideid");
                LeaveRideCall leaveRideCall = new LeaveRideCall();
                leaveRideCall.execute(userid, rideid);
                try {
                    String data = (String) leaveRideCall.get();
                    if (data.equals("200")) {
                        Toast.makeText(getActivity(), "You successfully left this ride", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        startActivity(i);
                        getActivity().finish();
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
                Fragment pre = getFragmentManager().findFragmentByTag("leave_dialog");
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

