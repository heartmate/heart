package com.HeartmatePack.heartmate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Doctor;
import java.util.List;


public class Util {
    private static ProgressDialog pd;
    
    // method to return the user's name
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


    // method to show progress icon
    public static void showprogress(Context context, String message) {
        pd = ProgressDialog.show(context, "", message);
    }

    // method to show dismissprogress icon
    public static void dismissprogress() {
        if (pd != null) {
            pd.dismiss();
        }
    }


    public static void getDoctor() {

        // set reference on doctor in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference(Constant.DOCTOR_FIREBASE);

        tasksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("Util", "onChildAdded: " + dataSnapshot.getValue());

                // get doctor information from firebase and save locally
                Doctor doctor = (Doctor) dataSnapshot.getValue(Doctor.class);
                if (doctor != null && doctor.getDoctor_id().equals(Constant.patient.getDoctor())) {
                    Constant.patient_doctor = doctor;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // accept or reject patient's adding to add or remove from database
    public static void acceptOrRejectPatient(boolean state) {
        // set reference on patient in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tasksRef = database.getReference(Constant.PATIENT_FIREBASE);

        // true (accepted)
        if (state) {
            Log.e("Util", Constant.Selected_patient + "acceptOrRejectPatient: " + Constant.Doctor);

            // add the doctor in patient
            tasksRef.child(Constant.Selected_patient.getPatient_id()).child("doctor").setValue(Constant.Doctor.getDoctor_id());
            // doctor_ver = 1 (patient was accepted)
            tasksRef.child(Constant.Selected_patient.getPatient_id()).child("doctor_ver").setValue("1");
        }
        // false (reject)
        else {
            // rmove doctor from patient
            tasksRef.child(Constant.Selected_patient.getPatient_id()).child("doctor").removeValue();
            tasksRef.child(Constant.Selected_patient.getPatient_id()).child("doctor_ver").removeValue();
        }
    }


    // method to get last known location
    public static Location getLastKnownLocation(Context context) {

        LocationManager mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location
                bestLocation = l;
            }
        }
        return bestLocation;

    }
    }