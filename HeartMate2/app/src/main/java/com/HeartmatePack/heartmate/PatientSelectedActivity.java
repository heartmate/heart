package com.HeartmatePack.heartmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PatientSelectedActivity extends AppCompatActivity {
    TextView name,heart_rate;
    LinearLayout patient_chat,patient_history,patient_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_selected);

        // get views from activity
        patient_chat=findViewById(R.id.patient_chat);
        patient_history=findViewById(R.id.patient_history);
        patient_info=findViewById(R.id.patient_info);
        name=findViewById(R.id.name);
        heart_rate=findViewById(R.id.heart_rate);

        // set patient name
        name.setText(Constant.Selected_patient.getFirst_name()+" "+Constant.Selected_patient.getLast_name());

        // chat button
        patient_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: contact patient
            }
        });

        // history button
        patient_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientSelectedActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // patient information button
        patient_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientSelectedActivity.this, PatientSelectedInfoActivity.class);
                startActivity(intent);
            }
        });

    }

}
