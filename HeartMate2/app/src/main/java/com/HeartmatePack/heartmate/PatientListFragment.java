package com.HeartmatePack.heartmate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Doctor;
import com.HeartmatePack.heartmate.bean.Patient;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PatientListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DatabaseReference tasksRef;
    private OnListFragmentInteractionListener mListener;

    // array list of patients for doctor
    private ArrayList<Patient> patients = new ArrayList<>();
    private MyPatientRecyclerViewAdapter myPatientRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PatientListFragment newInstance(int columnCount) {
        PatientListFragment fragment = new PatientListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_list, container, false);

        // Set the adapter
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            myPatientRecyclerViewAdapter = new MyPatientRecyclerViewAdapter(getActivity(), patients, mListener);
            recyclerView.setAdapter(myPatientRecyclerViewAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initDatabase();
        loadData();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Patient item);
    }

    private void initDatabase() {
        // set database reference in Patient
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.PATIENT_FIREBASE);
    }

    private void loadData() {
        patients.clear();

        tasksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("PatientsList", "onChildAdded: " + dataSnapshot.getValue());
                Patient patient = (Patient) dataSnapshot.getValue(Patient.class);

                // fill patients Array list
                if (!patients.contains(patient)) {
                    if (patient.getDoctor() != null && patient.getDoctor().equals(Constant.Doctor.getDoctor_id())) {
                        patients.add(patient);
                        myPatientRecyclerViewAdapter.notifyDataSetChanged();
                        // increase number of patients
                        Constant.pationtNumber++;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Doctor remoteItem = dataSnapshot.getValue(Doctor.class);

                // find the local item by uid
                int idx = patients.indexOf(remoteItem);
                myPatientRecyclerViewAdapter.notifyItemChanged(idx);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Doctor remoteItem = dataSnapshot.getValue(Doctor.class);
                int idx = patients.indexOf(remoteItem);

                // remove the patient from array list
                if (idx > -1) {
                    patients.remove(idx);
                    myPatientRecyclerViewAdapter.notifyItemRemoved(idx);
                }
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
