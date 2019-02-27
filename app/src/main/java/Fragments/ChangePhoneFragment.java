package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.R;

import API.UserCredentialUpdateCall;
import Models.AppConstants;
import Models.RegexCheck;

/**
 * Created by Talha on 1/2/2017.
 */
public class ChangePhoneFragment extends DialogFragment {
    private Button updatephonebtn,cancelbtn;
    private EditText phone;
    private Switch phoneStatus;
    private TextView phoneStatus_txt;
    SharedPreferences sharedPreferences;
    LayoutInflater inflater;
    View v;
    View v1;
    DialogInterface dialogInterface;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.change_phone_dialog, null);
        updatephonebtn = (Button) v.findViewById(R.id.change_phone_update_btn);
        cancelbtn = (Button) v.findViewById(R.id.change_phone_cancel_btn);
        phone = (EditText) v.findViewById(R.id.change_phone);
        phoneStatus = (Switch) v.findViewById(R.id.phoneStatus_id1);
        phoneStatus_txt = (TextView) v.findViewById(R.id.Switch_txt1);
        v1 = inflater.inflate(R.layout.custom_title, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCustomTitle(v1);
        return builder.create();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
        final String id = sharedPreferences.getString(AppConstants.key_stdempid, "");
        phoneStatus.setChecked(false);
        phoneStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    phoneStatus_txt.setText("Private");
                }
                else{
                    phoneStatus_txt.setText("Public");
                }

            }
        });
        updatephonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone = phone.getText().toString();
                String PhoneStatus = "0";
                int callId = 1;
                if (!RegexCheck.PhoneCheck(Phone)) {
                    phone.setError("Enter valid phone#");
                }
                if (phoneStatus.isChecked()) {
                    PhoneStatus = "1";
                }
                UserCredentialUpdateCall userCredentialUpdateCall = new UserCredentialUpdateCall();
                userCredentialUpdateCall.execute(id, callId, Phone, PhoneStatus);
                Toast.makeText(getActivity(), "phone updated", Toast.LENGTH_LONG).show();
                Fragment pre = getFragmentManager().findFragmentByTag("my_dialog");
                if (pre != null) {
                    DialogFragment df = (DialogFragment) pre;
                    df.dismiss();
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ProfileFragment prf = new ProfileFragment();
                ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right, 0);
                ft.replace(R.id.main_content_area, prf);
                ft.addToBackStack("prf");
                ft.commit();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment pre = getFragmentManager().findFragmentByTag("my_dialog");
                if (pre != null) {
                    DialogFragment df = (DialogFragment) pre;
                    df.dismiss();
                }
            }
        });
    }

//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//        dialog.dismiss();
//    }

}

