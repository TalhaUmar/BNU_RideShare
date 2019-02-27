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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.R;

import API.UserCredentialUpdateCall;
import Models.AppConstants;

/**
 * Created by Sh Junaid on 12/28/2016.
 */
public class ChangePasswordFragment extends DialogFragment {

    private Button updatepswdbtn,cancelbtn;
    private EditText pswd,confirmpswd;
    SharedPreferences sharedPreferences;
    LayoutInflater inflater;
    View v;
    View v1;
    DialogInterface dialogInterface;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.change_password_dialog, null);
        updatepswdbtn = (Button) v.findViewById(R.id.change_password_update_btn);
        cancelbtn = (Button) v.findViewById(R.id.change_password_cancel_btn);
        pswd = (EditText) v.findViewById(R.id.change_password);
        confirmpswd = (EditText) v.findViewById(R.id.change_password_confirm);
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
        updatepswdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pswd.getText().toString();
                int callId = 2;
                String ConfirmPswd = confirmpswd.getText().toString();
                if (ConfirmPswd.equals(password)) {
                    UserCredentialUpdateCall userCredentialUpdateCall = new UserCredentialUpdateCall();
                    userCredentialUpdateCall.execute(id,callId,password);
                    Toast.makeText(getActivity(),"password updated",Toast.LENGTH_LONG).show();
                    Fragment pre = getFragmentManager().findFragmentByTag("dialog_1");
                    if (pre != null) {
                        DialogFragment df = (DialogFragment) pre;
                        df.dismiss();
                    }
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ProfileFragment prf = new ProfileFragment();
                    ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
                    ft.replace(R.id.main_content_area, prf);
                    ft.addToBackStack("prf");
                    ft.commit();
                }
                else {
                    confirmpswd.setError("Password does not match");
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment pre = getFragmentManager().findFragmentByTag("dialog_1");
                if(pre !=null){
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
