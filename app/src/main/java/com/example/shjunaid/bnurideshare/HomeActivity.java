package com.example.shjunaid.bnurideshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import Fragments.JoinedRideDetailFragment;
import Fragments.JoinedRidesFragment;
import Fragments.LoginFragment;
import Fragments.PickupDestinationFragment;
import Fragments.PostRideFragment;
import Fragments.PostedRidesFragment;
import Fragments.ProfileFragment;
import Fragments.RideRequestsFragment;
import Fragments.SearchRidesFragment;
import Models.AppConstants;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView username_headerhome_txt;
    SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        username_headerhome_txt = (TextView) findViewById(R.id.username_header_home_txt);
        sharedPreferences = getSharedPreferences(AppConstants.loginpref_name, MODE_PRIVATE);
        String user_name = sharedPreferences.getString(AppConstants.key_username, "");
        username_headerhome_txt.setText(user_name);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PickupDestinationFragment prf = new PickupDestinationFragment();
        ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
        ft.replace(R.id.main_content_area, prf);
        ft.addToBackStack("prf");
        ft.commit();
//        String fragmentName = getIntent().getStringExtra("FragmentName");
//        if (fragmentName!=null){
//            //String fragmentName = extras.getString("FragmentName");
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//                if (fragmentName.equals("ReceiveRideRequestFragment")) {
//                    ReceiveRideRequestFragment rrrf = new ReceiveRideRequestFragment();
//                   // rrrf.setArguments(extras);
//                    ft.replace(R.id.main_content_area, rrrf);
//                }
//                if (fragmentName.equals("JoinedRideDetailFragment")) {
//                    JoinedRideDetailFragment jrdf = new JoinedRideDetailFragment();
//                    //jrdf.setArguments(extras);
//                    ft.replace(R.id.main_content_area, jrdf);
//                }
//        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if ( getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.post_ride_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PostRideFragment prf = new PostRideFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);

            ft.replace(R.id.main_content_area, prf);
            ft.addToBackStack("prf");
            ft.commit();
        } else if (id == R.id.search_ride_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PickupDestinationFragment prf = new PickupDestinationFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
            ft.replace(R.id.main_content_area, prf);
            ft.addToBackStack("prf");
            ft.commit();

        } else if (id == R.id.profile_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ProfileFragment prf = new ProfileFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
            ft.replace(R.id.main_content_area, prf);
            ft.addToBackStack("prf");
            ft.commit();
        }

        else if (id == R.id.posted_rides_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PostedRidesFragment prf = new PostedRidesFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
            ft.replace(R.id.main_content_area, prf);
            ft.addToBackStack("prf");
            ft.commit();
        }

        else if (id == R.id.ride_requests_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            RideRequestsFragment rrf = new RideRequestsFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
            ft.replace(R.id.main_content_area, rrf);
            ft.addToBackStack("rrf");
            ft.commit();
        }

        else if (id == R.id.join_rides_nav) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            JoinedRidesFragment jrf = new JoinedRidesFragment();
            ft.setCustomAnimations(R.anim.slidein_left, 0, R.anim.slidein_right,0);
            ft.replace(R.id.main_content_area, jrf);
            ft.addToBackStack("jrf");
            ft.commit();
        }

        else if (id == R.id.logout_nav) {
            SharedPreferences preferences =getSharedPreferences(AppConstants.loginpref_name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent i = new Intent(this,FirstActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
