package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shjunaid.bnurideshare.R;

import org.w3c.dom.Text;

import API.UserCredentialUpdateCall;
import Models.AppConstants;

/**
 * Created by Sh Junaid on 1/13/2017.
 */
public class AmountCalculateDialog extends DialogFragment {
    private Button okbtn;
    private TextView distance,amount;
    private int dis;
    private String am;
    LayoutInflater inflater;
    View v,v1;

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.calculate_amount, null);
        v1 = inflater.inflate(R.layout.custom_estimate_title, null);
        okbtn = (Button) v.findViewById(R.id.amount_ok_btn);
        distance = (TextView)v.findViewById(R.id.estdistance);
        amount = (TextView)v.findViewById(R.id.estamount);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCustomTitle(v1);
        return builder.create();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment pre = getFragmentManager().findFragmentByTag("amount_dialog");
                if (pre != null) {
                    DialogFragment df = (DialogFragment) pre;
                    df.dismiss();
                }
            }
        });
        distance.setText("Distance : "+Integer.toString(getDis())+" Km");
        amount.setText("Total Amount : Rs."+getAm());
    }
}
