package com.example.shjunaid.bnurideshare;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Models.AppConstants;

public class MainActivity extends Activity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    sharedPreferences = getSharedPreferences(AppConstants.loginpref_name,MODE_PRIVATE);
                    String id = sharedPreferences.getString(AppConstants.key_stdempid, "");
                    String pswd = sharedPreferences.getString(AppConstants.key_password,"");
                    if (id == "" && pswd == ""){
                        Intent i = new Intent(MainActivity.this,FirstActivity.class );
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });t.start();
    }
}


