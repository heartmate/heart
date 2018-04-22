package com.HeartmatePack.heartmate;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        // set the views in the layout
        name = findViewById(R.id.name);
        name.setText(Constant.Selected_patient.getFirst_name() + " " + Constant.Selected_patient.getLast_name());
    }
}