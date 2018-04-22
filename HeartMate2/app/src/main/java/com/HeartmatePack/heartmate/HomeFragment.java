package com.HeartmatePack.heartmate;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Heart_Rate;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
TextView heart_rate;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        // get views from activity
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView txtName = (TextView) view.findViewById(R.id.name);
        heart_rate=view.findViewById(R.id.heart_rate);
        Button doctor_chat = view.findViewById(R.id.doctor_chat);

        // chat with doctor button
        doctor_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String revicever = "";

                if (Constant.type == 0 && Constant.patient != null) {
                    if(Constant.patient_doctor != null && !Constant.patient_doctor.equals("")){
                        revicever = Constant.patient_doctor.getEmail().toString();
                        Log.e("P. home" , "have doctor");
                        Log.e("P. home", "R: "+revicever);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {revicever});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HeartMate");
                        startActivity(emailIntent);
                    }
                    else {
                        Toast.makeText(view.getContext(), "You have no doctor", Toast.LENGTH_SHORT).show();
                        Log.e("P. home", "you have no doctor");
                    }
                }
            }
        });
        txtName.setText(Util.getname());
        getRate();
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

    public void getRate() {

        // get the date of today
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = simpleDateFormat.format(new Date());

        // set reference on heart_rate in firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.RATE_FIREBASE);

        Log.e(ContentValues.TAG, "onDataChange: " + Constant.patient.getWrist());
        Log.e("Home", "onChildAdded: " + today);
        databaseReference.child(Constant.patient.getWrist()).orderByChild("rate_date").equalTo(today).limitToLast(1)

                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("Home", "onChildAdded: " + dataSnapshot.getValue());

                        // get data from database ans set in activity
                        Heart_Rate heart_rate1 = (Heart_Rate) dataSnapshot.getValue(Heart_Rate.class);
                        heart_rate.setText(heart_rate1.getRate()+" bpm");
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
}
