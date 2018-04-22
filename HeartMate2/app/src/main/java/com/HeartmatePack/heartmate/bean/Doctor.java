package com.HeartmatePack.heartmate.bean;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Doctor {
    String doctor_id;
    String first_name;
    String last_name;
    String specialty;
    String phone;
    String hospital;
    String email;
    ArrayList<Patient> patients;
    String type;


    public Doctor() {
    }

    public Doctor(String doctor_id, String first_name, String last_name, String specialty, String email) {
        this.doctor_id = doctor_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.specialty = specialty;
        this.email = email;
    }

    public Doctor(String first_name, String last_name, String specialty, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.specialty = specialty;
        this.email = email;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("doctor_id", doctor_id);
        result.put("first_name", first_name);
        result.put("last_name", last_name);
        result.put("specialty", specialty);
        result.put("phone", phone);
        result.put("hospital", hospital);
        result.put("email", email);
        result.put("type", type);

        return result;
    }
}
