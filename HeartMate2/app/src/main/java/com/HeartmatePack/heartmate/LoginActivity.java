package com.HeartmatePack.heartmate;

import android.content.Intent;
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

        patient_login=(LinearLayout)findViewById(R.id.patient_login);
        doctor_login=(LinearLayout)findViewById(R.id.doctor_login);


        // if patient
        patient_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Login_Activity.class);
                Constant.type=0;
                startActivity(intent);
            }
        });
        // if doctor
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
