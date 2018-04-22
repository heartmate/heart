package com.HeartmatePack.heartmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PatientSignUp1Activity extends AppCompatActivity {
   public static String f_p_name;
    public static String l_p_name;
    public static String p_email;
    public static String p_phone;
    public static  String p_password;
    public static  String p_confirmPassword;
    public static  String p_wrist;

    EditText f_nameEditText;
    EditText l_nameEditText;
    EditText emailEditText;
    EditText phoneEditText,input_p_wirst;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    ImageButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up1);
        next=(ImageButton)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                // get views from activity
                //name
                f_nameEditText = (EditText) findViewById(R.id.input_p_f_name);
                l_nameEditText = (EditText) findViewById(R.id.input_p_l_name);
                //email
                emailEditText = (EditText) findViewById(R.id.input_p_email);
                //phone
                phoneEditText = (EditText) findViewById(R.id.input_p_phone);
                // wristband serial number
                input_p_wirst = (EditText) findViewById(R.id.input_p_wirst);
                //password
                passwordEditText = (EditText) findViewById(R.id.input_p_password);

                //confirm password
                confirmPasswordEditText = (EditText) findViewById(R.id.input_p_password_confirm);

                // get text from views
                f_p_name = f_nameEditText.getText()+"";
                l_p_name = l_nameEditText.getText()+"";
                p_email = emailEditText.getText()+"";
                p_phone = phoneEditText.getText()+"";
                p_password = passwordEditText.getText()+"";
                p_confirmPassword = confirmPasswordEditText.getText()+"";
                p_wrist=input_p_wirst.getText()+"";

                // check if there is empty field
                if(f_p_name.isEmpty()||l_p_name.isEmpty()||p_email.isEmpty()||p_phone.isEmpty()||p_password.isEmpty()||p_confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter all fields....", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(p_password.equals(p_confirmPassword)){
                        Constant.patient.setFirst_name(f_p_name);
                        Constant.patient.setLast_name(l_p_name);
                        Constant.patient.setEmail(p_email);
                        Constant.patient.setPhone(p_phone);
                        Constant.patient.setWrist(p_wrist);
                        Intent intent = new Intent(PatientSignUp1Activity.this, PatientSignUp2Activity.class);
                        intent.putExtra(LoginActivity.TYPE,LoginActivity.PATIENT);
                        startActivity(intent);
                    }
                    // if password not matched display message
                    else {
                        Toast.makeText(getApplicationContext(), "Password not matched ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}
