package com.HeartmatePack.heartmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PatientSelectedInfoActivity extends AppCompatActivity {
    TextView input_name, input_Age, input_gender, input_wight, input_phone, input_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_selected_info);

        // get views from activity
        input_name = findViewById(R.id.input_name);
        input_Age = findViewById(R.id.input_Age);
        input_gender = findViewById(R.id.input_gender);
        input_wight = findViewById(R.id.input_wight);
        input_phone = findViewById(R.id.input_phone);
        input_email = findViewById(R.id.input_email);

        // set text in views
        input_name.setText("Name: " + Constant.Selected_patient.getFirst_name() + " " + Constant.Selected_patient.getLast_name());
        input_Age.setText("Birthday: " + Constant.Selected_patient.getAge());
        input_gender.setText("Gender: " + Constant.Selected_patient.getGender());
        input_wight.setText(Constant.Selected_patient.getWeight() + " Kg");
        input_phone.setText("phone Number: " + Constant.Selected_patient.getPhone());
        input_email.setText("email: " + Constant.Selected_patient.getEmail());
    }

}
