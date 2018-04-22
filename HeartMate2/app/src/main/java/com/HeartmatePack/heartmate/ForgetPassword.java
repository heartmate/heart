package com.HeartmatePack.heartmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_forget_password);

        //btn_reset_password
        Button resetPasswordButton = (Button) findViewById(R.id.btn_reset_password);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Email has been sent", Toast.LENGTH_SHORT).show();
                TextView sentPassword = (TextView) findViewById(R.id.done_sent_password_text);
                sentPassword.setText("Please check your Email to recover your password");
            }
        });
    }
}
