package com.HeartmatePack.heartmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView input_name, input_Age, input_gender, input_wight, input_phone, input_email,min,max,aidsT;
    Button btn_edit;
    TableRow age, kg, gender;
    LinearLayout heart_rate,aids;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // get views from fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        input_name = view.findViewById(R.id.input_name);
        input_Age = view.findViewById(R.id.input_Age);
        input_gender = view.findViewById(R.id.input_gender);
        input_wight = view.findViewById(R.id.input_wight);
        input_phone = view.findViewById(R.id.input_phone);
        input_email = view.findViewById(R.id.input_email);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit = view.findViewById(R.id.btn_edit);
        age = view.findViewById(R.id.age);
        kg = view.findViewById(R.id.kg);
        gender = view.findViewById(R.id.gender);
        min=view.findViewById(R.id.minrate);
        max=view.findViewById(R.id.maxrate);
        heart_rate=view.findViewById(R.id.heart_rate);
        aids=view.findViewById(R.id.aids);
        aidsT=view.findViewById(R.id.aidsText);

        // get local information and set in views in fragment
        // Patient
        if (Constant.type == 0 && Constant.patient != null) {
            input_name.setText("Name: " + Constant.patient.getFirst_name() + " " + Constant.patient.getLast_name());
            input_Age.setText("Birthday: " + Constant.patient.getAge());
            input_gender.setText("Gender: " + Constant.patient.getGender());
            input_wight.setText(Constant.patient.getWeight() + " Kg");
            input_phone.setText("phone Number: " + Constant.patient.getPhone());
            input_email.setText("email: " + Constant.patient.getEmail());
            min.setText("Min. Rate="+Constant.patient.getMin());
            max.setText("Max. Rate="+Constant.patient.getMax());
            aidsT.setText(Constant.patient.getAids());
        }
        // Dcotor
        else if (Constant.type == 1 && Constant.Doctor != null) {
            input_name.setText("Name: " + Constant.Doctor.getFirst_name() + " " + Constant.Doctor.getLast_name());
            input_email.setText("email: " + Constant.Doctor.getEmail());
            input_phone.setText("Specialty: " + Constant.Doctor.getSpecialty());

            // remove views from doctor
            age.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            kg.setVisibility(View.GONE);
            input_Age.setVisibility(View.GONE);
            input_gender.setVisibility(View.GONE);
            input_wight.setVisibility(View.GONE);
            heart_rate.setVisibility(View.GONE);
            aids.setVisibility(View.GONE);
        }

        // update button
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // get local information and set in views in fragment
        // Patient
        if (Constant.type == 0 && Constant.patient != null) {
            input_name.setText("Name: " + Constant.patient.getFirst_name() + " " + Constant.patient.getLast_name());
            input_Age.setText("Birthday: " + Constant.patient.getAge());
            input_gender.setText("Gender: " + Constant.patient.getGender());
            input_wight.setText(Constant.patient.getWeight() + " Kg");
            input_phone.setText("phone Number: " + Constant.patient.getPhone());
            input_email.setText("email: " + Constant.patient.getEmail());
            min.setText("Min. Rate="+Constant.patient.getMin());
            max.setText("Max. Rate="+Constant.patient.getMax());
            aidsT.setText(Constant.patient.getAids());
        }

        // Doctor
        else if (Constant.type == 1 && Constant.Doctor != null) {
            input_name.setText("Name: " + Constant.Doctor.getFirst_name() + " " + Constant.Doctor.getLast_name());
            input_email.setText("email: " + Constant.Doctor.getEmail());
            input_phone.setText("Specialty: " + Constant.Doctor.getSpecialty());

            // remove views for doctor
            age.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            kg.setVisibility(View.GONE);
            input_Age.setVisibility(View.GONE);
            input_gender.setVisibility(View.GONE);
            input_wight.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
