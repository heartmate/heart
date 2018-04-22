package com.HeartmatePack.heartmate;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Heart_Rate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientSelectedActivity extends AppCompatActivity {
    TextView name, heart_rate;
    LinearLayout patient_chat, patient_history, patient_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_selected);

        // get views from activity
        patient_chat = findViewById(R.id.patient_chat);
        patient_history = findViewById(R.id.patient_history);
        patient_info = findViewById(R.id.patient_info);
        name = findViewById(R.id.name);
        heart_rate = findViewById(R.id.heart_rate);

        // set patient name
        name.setText(Constant.Selected_patient.getFirst_name() + " " + Constant.Selected_patient.getLast_name());

        // chat button
        patient_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String revicever = "";

                if (Constant.type == 1) {
                    revicever = Constant.Selected_patient.getEmail().toString();
                    Log.e("P. selected", "have doctor");
                }

                Log.e("P. selected", "R: " + revicever);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{revicever});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HeartMate");
                startActivity(emailIntent);

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
        getRate();
    }

    public void getRate() {

        // set date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = simpleDateFormat.format(new Date());

        // set reference on heart_rate in firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.RATE_FIREBASE);
        Log.e(ContentValues.TAG, "onDataChange: " + Constant.Selected_patient.getWrist());

        // get last rate today
        databaseReference.child(Constant.Selected_patient.getWrist()).orderByChild("rate_date").equalTo(today).limitToLast(1)

                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("P. selected", "onChildAdded: " + dataSnapshot.getValue());

                        // get the value
                        Heart_Rate heart_rate1 = (Heart_Rate) dataSnapshot.getValue(Heart_Rate.class);

                        // set the value in activity
                        heart_rate.setText(heart_rate1.getRate() + " bpm");
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
