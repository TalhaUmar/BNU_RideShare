package com.example.shjunaid.bnurideshare;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Talha on 12/21/2016.
 */
public class MyAndroidFirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService{
    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}
