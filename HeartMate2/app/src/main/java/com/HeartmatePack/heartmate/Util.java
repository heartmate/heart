package com.HeartmatePack.heartmate;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Doctor;


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

}