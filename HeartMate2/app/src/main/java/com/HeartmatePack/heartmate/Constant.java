package com.HeartmatePack.heartmate;

import com.HeartmatePack.heartmate.bean.Doctor;
import com.HeartmatePack.heartmate.bean.Patient;

import java.util.ArrayList;



public class Constant {

    public static final String PATIENT_FIREBASE="patient";
    public static final String RATE_FIREBASE="heart_rate";
    public static final String DOCTOR_FIREBASE="doctor";
    public static final String LOGIN="login.php";
    public static final String SIGNUP="signup.php";

    public static final String EMAIL="Email";
    public static final String PASSWORD="Password";

    public static final String FIRST_NAME="First_Name";
    public static final String LAST_NAME="Last_Name";
    public static final String AGE="Age";
    public static final String GENDER="Gender";
    public static final String WEIGHT="Weight";
    public static final String PHONE="Phone";
    public static final String TYPE="Type";


    public static final String patient_id="patient_id";
    public static final String first_name="first_name";
    public static final String last_name="last_name";
    public static final String age="age";
    public static final String gender="gender";
    public static final String phone="phone";
    public static final String weight="weight";
    public static final String email="email";
    public static final String wristband_id="wristband_id";
    public static final String email_activation="email_activation";

    public static int type;  ///0 patient 1 doctor
    public static int pationtNumber;

    public static Patient patient=new Patient();
    public static Doctor Doctor=new Doctor();
    public static Doctor patient_doctor=new Doctor();
    public static ArrayList<Patient> patientArrayList=new ArrayList<>();
    public static Patient Selected_patient=new Patient();


}
