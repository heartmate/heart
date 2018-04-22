package com.HeartmatePack.heartmate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity {
    LinearLayout patient_login,doctor_login;
    public static String TYPE="type";
    public static String PATIENT="patient";
    public static String  DOCTOR="doctor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get views from activity
        patient_login=(LinearLayout)findViewById(R.id.patient_login);
        doctor_login=(LinearLayout)findViewById(R.id.doctor_login);


        // access permission for location and SMS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_LOGS, android.Manifest.permission.READ_EXTERNAL_STORAGE},10
            );
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,  android.Manifest.permission.ACCESS_FINE_LOCATION},2
            );
        }

        // if patient set type = 0
        patient_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Login_Activity.class);
                Constant.type=0;
                startActivity(intent);
            }
        });
        // if doctor set type = 1
        doctor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Login_Activity.class);
                Constant.type=1;
                startActivity(intent);
            }
        });

    }
}
