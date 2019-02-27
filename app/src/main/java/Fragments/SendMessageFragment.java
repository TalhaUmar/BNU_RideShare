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
import android.widget.Toast;

import com.example.shjunaid.bnurideshare.R;

import java.util.concurrent.ExecutionException;

import API.SendMessageCall;
import API.UserCredentialUpdateCall;
import Models.AppConstants;

/**
 * Created by Sh Junaid on 1/21/2017.
 */
public class SendMessageFragment extends DialogFragment {
    private Button sendbtn,cancelbtn;
    private EditText msgtext;
    LayoutInflater inflater;
    View v;
    View v1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.send_message_dialog, null);
        cancelbtn = (Button) v.findViewById(R.id.send_msg_cancel_btn);
        sendbtn = (Button) v.findViewById(R.id.send_msg_send_btn);
        msgtext = (EditText) v.findViewById(R.id.msg_txt);

        v1 = inflater.inflate(R.layout.custom_send_msg_title, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setCustomTitle(v1);
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msgtext.getText().toString();
                int rid = getArguments().getInt("RideId");
                int pid = getArguments().getInt("PassengerId");
                SendMessageCall sendMessage = new SendMessageCall();
                sendMessage.execute(pid, rid, message);
                try {
                    String data = (String) sendMessage.get();
                    if (data.equals("200")) {
                        Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                        Fragment pre = getFragmentManager().findFragmentByTag("send_msg");
                        if (pre != null) {
                            DialogFragment df = (DialogFragment) pre;
                            df.dismiss();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment pre = getFragmentManager().findFragmentByTag("send_msg");
                if (pre != null) {
                    DialogFragment df = (DialogFragment) pre;
                    df.dismiss();
                }
            }
        });
    }
}
