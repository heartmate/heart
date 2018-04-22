package com.HeartmatePack.heartmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.HeartmatePack.heartmate.bean.Doctor;
import com.HeartmatePack.heartmate.bean.Patient;


public class Login_Activity extends AppCompatActivity {
    EditText passwordField, emailField;
    ProgressDialog progressDialog;
    String p_email, p_password;
    private FirebaseAuth mAuth;
    private DatabaseReference tasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.input_p_email);
        passwordField = (EditText) findViewById(R.id.input_p_password);
        final Button loginButton = (Button) findViewById(R.id.btn_p_login);
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_email = emailField.getText().toString();
                p_password = passwordField.getText().toString();
                if (p_email.trim().equals("") || p_password.trim().equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter all fields....", Toast.LENGTH_SHORT).show();
                else {
                    login(p_email, p_password);
                }
            }
        });


        // new Account link
        Button createAccountLink = (Button) findViewById(R.id.link_p_signup);
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Constant.type == 0)
                    intent = new Intent(Login_Activity.this, PatientSignUp1Activity.class);
                else
                    intent = new Intent(Login_Activity.this, DoctorSignUpActivity.class);
                intent.putExtra(LoginActivity.TYPE, LoginActivity.PATIENT);
                startActivity(intent);
            }
        });
        TextView forgetPasswordLink = (TextView) findViewById(R.id.link_forgetPassword);
        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Login_Activity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void login(String email, String password) {

        progressDialog.setMessage("Login...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String uid = user.getUid();

                            // Patient
                            if (Constant.type == 0) {
                                tasksRef = initDatabase().getReference(Constant.PATIENT_FIREBASE);
                                tasksRef.addChildEventListener(new ChildEventListener() {



                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Log.e("Log", "onChildAdded: " + dataSnapshot.getValue());
                                        Patient patient = (Patient) dataSnapshot.getValue(Patient.class);

                                        if (patient != null && "0".equals(patient.getType()) && patient.getPatient_id().equals(uid)) {
                                            Constant.patient = patient;

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "welcome  " + Constant.patient.getFirst_name() + " " + Constant.patient.getLast_name(), Toast.LENGTH_SHORT).show();

                                            String[] emg = new String[3];
                                            int i = 0;
                                            if (Constant.patient.getContact1() != null && !Constant.patient.getContact1().isEmpty()) {
                                                emg[i] = Constant.patient.getContact1();
                                                i++;
                                            }
                                            if (Constant.patient.getContact2() != null && !Constant.patient.getContact2().isEmpty()) {
                                                emg[i] = Constant.patient.getContact2();
                                                i++;
                                            }
                                            if (Constant.patient.getContact3() != null && !Constant.patient.getContact3().isEmpty()) {
                                                emg[i] = Constant.patient.getContact3();
                                                i++;
                                            }
                                            startActivity(new Intent(Login_Activity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            mAuth.signOut();
                                            progressDialog.dismiss();
                                        }

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
                            // Doctor
                            else {
                                tasksRef = initDatabase().getReference(Constant.DOCTOR_FIREBASE);
                                tasksRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        Log.e("Log", "onChildAdded: " + dataSnapshot.getValue());
                                        Doctor doctor = (Doctor) dataSnapshot.getValue(Doctor.class);
                                        if (doctor != null && "1".equals(doctor.getType()) && doctor.getDoctor_id().equals(uid)) {
                                            Constant.Doctor = doctor;
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "welcome  " + Constant.Doctor.getFirst_name() + " " + Constant.Doctor.getLast_name(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login_Activity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            mAuth.signOut();
                                        }
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
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private FirebaseDatabase initDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database;
    }
}
