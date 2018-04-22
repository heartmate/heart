package com.HeartmatePack.heartmate;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // identify index of current nav menu item
    public static int navItemIndex = 0;
    private static final String TAG = "MainActivity";

    private static final String TAG_HOME = "home";
    private static final String TAG_DOCTOR = "doctor";
    private static final String TAG_EMGCONTACT = "emgContact";
    private static final String TAG_EDITPROFILE = "editProfile";
    private static final String TAG_HELP = "help";
    public static String CURRENT_TAG = TAG_HOME;
    private Handler mHandler;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //...
        mHandler = new Handler();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set user's name in nav header
        View navHeader = navigationView.getHeaderView(0);
        TextView txtName = (TextView) navHeader.findViewById(R.id.name);

        // remove item for patient
        if (Constant.type == 0) {
            navigationView.getMenu().getItem(3).setVisible(false);
        }
        // remove items for doctor
        else {
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(false);
        }
        txtName.setText(getname());

        // go directly to Home Page
        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment(drawer);
            } else
                super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // My page
        if (id == R.id.nav_my_page || id == R.id.nav_list_pa) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            Log.e(TAG, "onNavigationItemSelected: here");
        }
        // my doctor = 1
        else if (id == R.id.nav_my_doc) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_DOCTOR;
        }
        // emergency contacts = 2
        else if (id == R.id.nav_em) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_EMGCONTACT;
        }
        // edit profile = 3
        else if (id == R.id.nav_edit) {
            navItemIndex = 3;
            CURRENT_TAG = TAG_EDITPROFILE;
        }
        // help = 4
        else if (id == R.id.nav_help) {
            navItemIndex = 4;
            CURRENT_TAG = TAG_HELP;
        }
        // log out = 5
        else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //...
        loadHomeFragment(drawer);
        return true;
    }

    // method used to load the fragment
    private void loadHomeFragment(DrawerLayout drawer) {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);

                fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG);
                Log.e(TAG, "onNavigationItemSelected" + CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }


        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                if (Constant.type == 0) {
                    HomeFragment homeFragment = new HomeFragment();
                    Log.e(TAG, "Patient: selected 0");
                    return homeFragment;
                }
            case 1:
                // My doctor fragment
                DoctorFragment doctorfragment = new DoctorFragment();
                Log.e(TAG, "patient: selected 1");
                return doctorfragment;


            case 2:
                // emergency contacts fragment
                EmgFragment emgcontactfragment = new EmgFragment();
                Log.e(TAG, "selected 2");
                return emgcontactfragment;
            case 3:
                // edit profile fragment
                ProfileFragment profileFragment = new ProfileFragment();
                Log.e(TAG, "selected 3");
                return profileFragment;

            case 4:
                // help fragment
                HelpFragment helpFragment = new HelpFragment();
                Log.e(TAG, "selected 4");
                return helpFragment;
                //TODO: help activity
            default:
                return new HomeFragment();
        }
    }

    // user's name
    public static String getname() {
        String name = "";
        // patient
        if (Constant.type == 0 && Constant.patient != null) {
            name = Constant.patient.getFirst_name() + " " + Constant.patient.getLast_name();
        }
        // doctor
        else if (Constant.type == 1 && Constant.Doctor != null) {
            name = Constant.Doctor.getFirst_name() + " " + Constant.Doctor.getLast_name();
        }
        return name;
    }

}
