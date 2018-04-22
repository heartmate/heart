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

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DoctorListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    //
    private DatabaseReference tasksRef;
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private MyDoctorListRecyclerViewAdapter myDoctorListRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DoctorListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DoctorListFragment newInstance(int columnCount) {
        DoctorListFragment fragment = new DoctorListFragment();
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
        View view = inflater.inflate(R.layout.fragment_doctorlist_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myDoctorListRecyclerViewAdapter = new MyDoctorListRecyclerViewAdapter(getActivity(),this,getFragmentManager(),doctors, mListener);
            recyclerView.setAdapter(myDoctorListRecyclerViewAdapter);
        }
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
        void onListFragmentInteraction(Doctor item);
    }

    private void initDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.DOCTOR_FIREBASE);
    }

    private void loadData() {
        doctors.clear();

        // fetch from firebase as remote data changes
        tasksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("DoctorList", "onChildAdded: " + dataSnapshot.getValue());
                Doctor doctor = (Doctor) dataSnapshot.getValue(Doctor.class);

                 // check it is not already saved locally
                if (!doctors.contains(doctor)) {
                    doctors.add(doctor);
                    myDoctorListRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Doctor remoteItem = dataSnapshot.getValue(Doctor.class);

                // find the local item by uid
                int idx = doctors.indexOf(remoteItem);
                myDoctorListRecyclerViewAdapter.notifyItemChanged(idx);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Doctor remoteItem = dataSnapshot.getValue(Doctor.class);
                int idx = doctors.indexOf(remoteItem);
                if (idx > -1) {
                    doctors.remove(idx);
                    myDoctorListRecyclerViewAdapter.notifyItemRemoved(idx);
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
