package com.HeartmatePack.heartmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    Button btn_update;
    EditText input_f_name, input_l_name, input_wight, input_phone, input_email, input_p_specialty, input_p_hospital,minrate,maxrate;
    TextView phone;
    TableRow wight, hospital, speial;
    LinearLayout heart_rate,aidsL;
    CheckBox aid1,aid2,aid3,aid4,aid5;
    String aids="";
    private DatabaseReference tasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get views from activity
        setContentView(R.layout.activity_edit);
        wight = findViewById(R.id.wight);
        hospital = findViewById(R.id.hospital);
        speial = findViewById(R.id.speial);
        btn_update = findViewById(R.id.btn_update);
        input_f_name = findViewById(R.id.input_f_name);
        input_l_name = findViewById(R.id.input_l_name);
        input_wight = findViewById(R.id.input_wight);
        input_phone = findViewById(R.id.input_phone);
        input_email = findViewById(R.id.input_email);
        minrate=findViewById(R.id.minrate);
        maxrate=findViewById(R.id.maxrate);
        phone = findViewById(R.id.phone);
        input_p_specialty = findViewById(R.id.input_p_specialty);
        input_p_hospital = findViewById(R.id.input_p_hospital);
        heart_rate=findViewById(R.id.heart_rate);
        aidsL=findViewById(R.id.aids);
        input_email.setEnabled(false);
        aid1=findViewById(R.id.aid1);
        aid2=findViewById(R.id.aid2);
        aid3=findViewById(R.id.aid3);
        aid4=findViewById(R.id.aid4);
        aid5=findViewById(R.id.aid5);

        // Patient
        if (Constant.type == 0 && Constant.patient != null) {

            // set data in views
            input_f_name.setText(Constant.patient.getFirst_name());
            input_l_name.setText(Constant.patient.getLast_name());
            input_wight.setText(Constant.patient.getWeight());
            input_phone.setText(Constant.patient.getPhone());
            input_email.setText(Constant.patient.getEmail());
            maxrate.setText(Constant.patient.getMax());
            minrate.setText(Constant.patient.getMin());
            hospital.setVisibility(View.GONE);
            speial.setVisibility(View.GONE);

            // first aid check box
            if(Constant.patient.getAids().contains(getString(R.string.aid1))){
                aid1.setChecked(true);
            }
            if(Constant.patient.getAids().contains(getString(R.string.aid2))){
                aid2.setChecked(true);
            }
            if(Constant.patient.getAids().contains(getString(R.string.aid3))){
                aid3.setChecked(true);
            }
            if(Constant.patient.getAids().contains(getString(R.string.aid4))){
                aid4.setChecked(true);
            }
            if(Constant.patient.getAids().contains(getString(R.string.aid5))){
                aid5.setChecked(true);
            }

            // Doctor
        } else if (Constant.type == 1 && Constant.Doctor != null) {
            input_f_name.setText(Constant.Doctor.getFirst_name());
            input_l_name.setText(Constant.Doctor.getLast_name());
            input_email.setText(Constant.Doctor.getEmail());
            input_phone.setText(Constant.Doctor.getPhone());
            input_p_hospital.setText(Constant.Doctor.getHospital());
            input_p_specialty.setText(Constant.Doctor.getSpecialty());

            wight.setVisibility(View.GONE);
            heart_rate.setVisibility(View.GONE);
            aidsL.setVisibility(View.GONE);
        }

        // update button
        btn_update.setOnClickListener(new View.OnClickListener() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            @Override
            public void onClick(View view) {
                if (Constant.type == 0) {
                    if(aid1.isChecked()){
                        aids+="*"+getString(R.string.aid1)+"\n";
                    }
                    if(aid2.isChecked()){
                        aids+="*"+getString(R.string.aid2)+"\n";
                    }
                    if(aid3.isChecked()){
                        aids+="*"+getString(R.string.aid3)+"\n";
                    }
                    if(aid4.isChecked()){
                        aids+="*"+getString(R.string.aid4)+"\n";
                    }
                    if(aid5.isChecked()){
                        aids+="*"+getString(R.string.aid5)+"\n";
                    }

                    // get new data from activity
                    Constant.patient.setFirst_name(input_f_name.getText() + "");
                    Constant.patient.setLast_name(input_l_name.getText() + "");
                    Constant.patient.setPhone(input_phone.getText() + "");
                    Constant.patient.setWeight(input_wight.getText() + "");
                    Constant.patient.setMax(maxrate.getText()+"");
                    Constant.patient.setAids(aids);
                    Constant.patient.setMin(minrate.getText()+"");

                    tasksRef = database.getReference(Constant.PATIENT_FIREBASE);

                    // save data in map and send to firebase
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(Constant.patient.getPatient_id(), Constant.patient.toMap());
                    tasksRef.updateChildren(childUpdates);
                    finish();

                    // Doctor
                } else {

                    // get new data from activity
                    Constant.Doctor.setFirst_name(input_f_name.getText() + "");
                    Constant.Doctor.setLast_name(input_l_name.getText() + "");
                    Constant.Doctor.setPhone(input_phone.getText() + "");
                    Constant.Doctor.setHospital(input_p_hospital.getText() + "");
                    Constant.Doctor.setSpecialty(input_p_specialty.getText() + "");

                    tasksRef = database.getReference(Constant.DOCTOR_FIREBASE);

                    // save data in map and send to firebase
                    Map <String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(Constant.Doctor.getDoctor_id(), Constant.Doctor.toMap());
                    tasksRef.updateChildren(childUpdates);
                    finish();
                }
            }
        });
    }
}
