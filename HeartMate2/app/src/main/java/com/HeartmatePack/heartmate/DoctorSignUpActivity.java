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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DoctorSignUpActivity extends AppCompatActivity {
    String f_p_name;
    String l_p_name;
    String p_email;
    String p_phone;
    String p_hospital;
    String p_specialty;
    String p_password;
    String p_confirmPassword;
    EditText f_nameEditText;
    EditText l_nameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText spEditText;
    EditText hospitalEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;

    ProgressDialog progressDialog;
    private FirebaseAuth auth;

    private DatabaseReference tasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);
        auth = FirebaseAuth.getInstance();

        f_nameEditText = (EditText) findViewById(R.id.input_p_f_name);
        l_nameEditText = (EditText) findViewById(R.id.input_p_l_name);
        emailEditText = (EditText) findViewById(R.id.input_p_email);
        phoneEditText = (EditText) findViewById(R.id.input_p_phone);
        spEditText = (EditText) findViewById(R.id.input_p_specialty);
        hospitalEditText = (EditText) findViewById(R.id.input_p_hospital);
        passwordEditText = (EditText) findViewById(R.id.input_p_password);
        //confirm password
        confirmPasswordEditText = (EditText) findViewById(R.id.input_p_password_confirm);



        progressDialog = new ProgressDialog(this);
        Button signupButton = (Button) findViewById(R.id.btn_signup);

        // sign up
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_p_name = f_nameEditText.getText() + "";
                l_p_name = l_nameEditText.getText() + "";
                p_email = emailEditText.getText() + "";
                p_phone = phoneEditText.getText() + "";
                p_hospital = hospitalEditText.getText() + "";
                p_specialty = spEditText.getText() + "";
                p_password = passwordEditText.getText() + "";
                p_confirmPassword = confirmPasswordEditText.getText() + "";



                if (!(f_p_name.isEmpty() || l_p_name.isEmpty() || p_email.isEmpty() || p_specialty.isEmpty() || p_password.isEmpty() || p_confirmPassword.isEmpty())) {
                    if (p_password.equals(p_confirmPassword)) {

                        progressDialog.setMessage("SignUp...");
                        progressDialog.show();

                        auth.createUserWithEmailAndPassword(p_email, p_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();

                                    Constant.Doctor.setDoctor_id(user.getUid());
                                    Constant.Doctor.setFirst_name(f_p_name);
                                    Constant.Doctor.setLast_name(l_p_name);
                                    Constant.Doctor.setSpecialty(p_specialty);
                                    Constant.Doctor.setHospital(p_hospital);
                                    Constant.Doctor.setPhone(p_phone);
                                    Constant.Doctor.setEmail(p_email);
                                    Constant.Doctor.setType("1");
                                    Constant.type = 1;

                                    initDatabase();
                                    tasksRef.child(Constant.Doctor.getDoctor_id()).setValue(Constant.Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(DoctorSignUpActivity.this, "Saved: ", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    progressDialog.dismiss();
                                    Toast.makeText(DoctorSignUpActivity.this, " SignUp Success.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DoctorSignUpActivity.this, Login_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    Constant.type = 1;
                                    startActivity(intent);
                                } else {
                                    // if sign in fails
                                    progressDialog.dismiss();
                                    Log.w("D. Sign up", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(DoctorSignUpActivity.this, " failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Password not matched ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter all fields....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.DOCTOR_FIREBASE);
    }
}
