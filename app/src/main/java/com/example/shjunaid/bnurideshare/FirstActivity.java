package com.example.shjunaid.bnurideshare;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import Fragments.JoinedRideDetailFragment;
import Fragments.LoginFragment;
import Fragments.SignupFragment;

/**
 * Created by Sh Junaid on 12/6/2016.
 */
public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
//        String activityName = getIntent().getStringExtra("ActivityName");
//        if (activityName!=null && activityName.equals("ResetPasswordActivity")){
//            Intent i = new Intent(this,ResetPasswordActivity.class);
//            startActivity(i);
//            finish();
//        }
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoginFragment lf = new LoginFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);

            ft.replace(R.id.login_fr, lf);
            ft.addToBackStack("lf");
            ft.commit();
    }
}
