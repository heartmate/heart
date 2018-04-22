package com.HeartmatePack.heartmate;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference tasksRef;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView doc_name, doc_phone, doc_hos;
    Button delet_my_doctor_btn;
    FrameLayout con;

    private OnFragmentInteractionListener mListener;

    public DoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorFragment newInstance(String param1, String param2) {
        DoctorFragment fragment = new DoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
        doc_name = view.findViewById(R.id.doc_name);
        doc_phone = view.findViewById(R.id.doc_phone);
        doc_hos = view.findViewById(R.id.doc_hos);
        delet_my_doctor_btn = view.findViewById(R.id.delet_my_doctor_btn);

        // set data in views
        doc_name.setText(Constant.patient_doctor.getFirst_name()+" "+Constant.patient_doctor.getLast_name());
        doc_phone.setText(Constant.patient_doctor.getPhone());
        doc_hos.setText(Constant.patient_doctor.getHospital());
        con=view.findViewById(R.id.con);

        // when click delete doctor button
        delet_my_doctor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Remove Doctor:")
                        .setMessage("Are you sure ? your Doctor will Removed ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                initDatabasep();

                                // remove doctor from patient
                                Constant.patient.setDoctor("");

                                // remove doctor from patient in firebase
                                tasksRef.child(Constant.patient.getPatient_id()).child("doctor").removeValue();

                                // set no doctor fragment
                                NoDoctorFragment fragment2 = new NoDoctorFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                con.removeAllViews();
                                fragmentTransaction.replace(R.id.con, fragment2);

                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if(Constant.patient.getDoctor()!=null && !Constant.patient.getDoctor().isEmpty()){
            Util.getDoctor();
        }
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


    private void initDatabasep() {
        // set reference on patient in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.PATIENT_FIREBASE);
    }
}
