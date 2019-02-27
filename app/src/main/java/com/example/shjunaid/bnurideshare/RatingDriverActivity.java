package com.example.shjunaid.bnurideshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import API.IApiCaller;
import API.RatingInsertCall;
import Models.Rating;

/**
 * Created by Talha on 1/24/2017.
 */
public class RatingDriverActivity extends AppCompatActivity {
    private Button submit;
    private RatingBar ratingBar;
    private int UserId;
    private TextView name,source,destination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_driver);
        submit = (Button) findViewById(R.id.submit_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        UserId = getIntent().getIntExtra("driverid",0);
        name = (TextView)findViewById(R.id.rate_driver_name);
        source = (TextView)findViewById(R.id.rate_driver_source);
        destination = (TextView)findViewById(R.id.rate_driver_destination);

        name.setText(getIntent().getStringExtra("drivername"));
        source.setText(getIntent().getStringExtra("source"));
        destination.setText(getIntent().getStringExtra("destination"));
        init();
    }

    private void init() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rating r = new Rating();
                r.setUserId(UserId);
                int ratingvalue = (int) ratingBar.getRating();
                if (ratingvalue == 1){r.setOneStar(1);}
                else if (ratingvalue == 2){r.setTwoStar(1);}
                else if (ratingvalue == 3){r.setThreeStar(1);}
                else if (ratingvalue == 4){r.setFourStar(1);}
                else if (ratingvalue == 5){r.setFiveStar(1);}
                //String ratingvalue = String.valueOf(ratingBar.getRating());
                RatingInsertCall ratingInsert = new RatingInsertCall(RatingDriverActivity.this, new IApiCaller() {
                    @Override
                    public void onResult(String data) {
                        if (data.equals("200")){
                            Toast.makeText(RatingDriverActivity.this,"Thank you for rating.",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
                ratingInsert.execute(r);


            }
        });
    }

}
