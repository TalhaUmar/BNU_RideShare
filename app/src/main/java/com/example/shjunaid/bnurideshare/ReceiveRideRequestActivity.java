package com.example.shjunaid.bnurideshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import API.AcceptRejectRideRequestCall;
import API.RideRequestCall;

/**
 * Created by Talha on 1/8/2017.
 */
public class ReceiveRideRequestActivity extends AppCompatActivity {
    private TextView requesterName,pickupLocation,requesterDestination;
    private Button acceptbtn,rejectbtn;
    private int uid,rid;
    private String action = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_ride_request);
        requesterName = (TextView) findViewById(R.id.requester_name_txt);
        pickupLocation = (TextView) findViewById(R.id.requester_pickuplocation_txt);
        requesterDestination = (TextView) findViewById(R.id.requester_destination_txt);
        acceptbtn = (Button) findViewById(R.id.acceptResquest_button);
        rejectbtn = (Button) findViewById(R.id.rejectRequest_button);
        requesterName.setText(getIntent().getStringExtra("RequesterName"));
        String ploc = getIntent().getStringExtra("PickupLocation");
        String dest = getIntent().getStringExtra("Destination");
        pickupLocation.setText(ploc.replace(","," "));
        requesterDestination.setText(dest.replace(","," "));
        uid = Integer.parseInt(getIntent().getStringExtra("RequesterId"));
        rid = Integer.parseInt(getIntent().getStringExtra("RideId"));


        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "AcceptRequest";
                AcceptRejectRideRequestCall acceptRejectRideRequestCall = new AcceptRejectRideRequestCall();
                acceptRejectRideRequestCall.execute(uid,rid,action);
                try {
                    String data = (String) acceptRejectRideRequestCall.get();
                    if(data != "404")
                    {
                        Toast.makeText(ReceiveRideRequestActivity.this, "Your response submitted successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ReceiveRideRequestActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
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
                action = "RejectRequest";
                AcceptRejectRideRequestCall acceptRejectRideRequestCall = new AcceptRejectRideRequestCall();
                acceptRejectRideRequestCall.execute(uid,rid,action);
                try {
                    String data = (String) acceptRejectRideRequestCall.get();
                    if(data != "404")
                    {
                        Toast.makeText(ReceiveRideRequestActivity.this, "Your response submitted successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ReceiveRideRequestActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
