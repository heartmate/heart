package com.HeartmatePack.heartmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;



public class PatientSignUp2Activity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int birthDay;
    int birthMonth;
    int birthYear;
    EditText wEditText,minrate,maxrate;
    CheckBox aid1,aid2,aid3,aid4,aid5;
    private FirebaseAuth auth;
    String aids="";

    private DatabaseReference tasksRef;
    String age;
    String p_wieght,p_minrate,p_maxrate;
    String gender;


    TextView ageTextView;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up2);
        auth = FirebaseAuth.getInstance();

        // get views from activity
        ageTextView = (TextView) findViewById(R.id.text_BirthDate);
        wEditText = (EditText) findViewById(R.id.input_weght);
        minrate = (EditText) findViewById(R.id.minrate);
        maxrate = (EditText) findViewById(R.id.maxrate);

        // first aid checkbox
        aid1=findViewById(R.id.aid1);
        aid2=findViewById(R.id.aid2);
        aid3=findViewById(R.id.aid3);
        aid4=findViewById(R.id.aid4);
        aid5=findViewById(R.id.aid5);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        // select BirthDate by clicking icon
        final ImageView iconeDate = (ImageView) findViewById(R.id.icon_date);
        iconeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                birthDateDialog(iconeDate);
            }
        });

        //set BirthDate by click text view
        final TextView texteDate = (TextView) findViewById(R.id.text_BirthDate);
        texteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDateDialog(iconeDate);
            }
        });

        // Button
        Button signupButton = (Button) findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set first aid value
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

                // get value of views
                age = ageTextView.getText() + "";
                p_wieght = wEditText.getText() + "";
                p_maxrate = maxrate.getText() + "";
                p_minrate = minrate.getText() + "";

                // gender
                gender =
                        (radioGroup.getCheckedRadioButtonId() == 0 ? "Male" : "Female");
                if (!(age.isEmpty() ||p_maxrate.isEmpty()||p_minrate.isEmpty()|| (wEditText.getText() + "").isEmpty())) {


                    // waiting panel
                    Util.showprogress(PatientSignUp2Activity.this, "SignUp...");

                    auth.createUserWithEmailAndPassword(PatientSignUp1Activity.p_email, PatientSignUp1Activity.p_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // update user's information if sign in success
                                Log.d("Patient SignUp2", "create user success");
                                FirebaseUser user = auth.getCurrentUser();

                                // save users data
                                Constant.patient.setPatient_id(user.getUid());
                                Constant.patient.setAge(age);
                                Constant.patient.setGender(gender);
                                Constant.patient.setWeight(p_wieght);
                                Constant.patient.setMax(p_maxrate);
                                Constant.patient.setMin(p_minrate);
                                Constant.patient.setAids(aids);
                                Constant.patient.setType("0");
                                Constant.type=0;
                                initDatabase();

                                // send data to firebase
                                tasksRef.child(Constant.patient.getPatient_id()).setValue(Constant.patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Util.dismissprogress();
                                        Toast.makeText(PatientSignUp2Activity.this, "Saved: ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // completed
                                Util.dismissprogress();
                                Toast.makeText(PatientSignUp2Activity.this, " SignUp Success.",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(PatientSignUp2Activity.this, Login_Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Constant.type=0;
                                startActivity(intent);
                            } else {
                                // display a message if sign in failed
                                Util.dismissprogress();
                                Log.w("P. SIgnUp2", "create user failed", task.getException());
                                Toast.makeText(PatientSignUp2Activity.this, " failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                // if not all filed are filled
                else {
                    Util.dismissprogress();
                    Toast.makeText(getApplicationContext(), "Please enter all fields....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // date of birth dialog method
    public void birthDateDialog(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // show date picker dialog to set date of birth
                DatePickerDialog dialog = new DatePickerDialog(
                        PatientSignUp2Activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                birthYear = i;
                birthDay = i2;
                birthMonth = i1 + 1;
                DisplayDate();
            }

        };
    }

    // display date of birth method
    private void DisplayDate() {
        ageTextView.setText(birthDay + "/" + birthMonth + "/" + birthYear);
    }

    private void initDatabase() {
        // set reference on patient in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.PATIENT_FIREBASE);
    }
}
