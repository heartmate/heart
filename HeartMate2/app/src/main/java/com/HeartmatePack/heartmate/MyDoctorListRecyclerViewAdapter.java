package com.HeartmatePack.heartmate;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.DoctorListFragment.OnListFragmentInteractionListener;
import com.HeartmatePack.heartmate.bean.Doctor;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDoctorListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoctorListRecyclerViewAdapter.ViewHolder> {

    private final List<Doctor> mValues;
    private final OnListFragmentInteractionListener mListener;
    private DatabaseReference tasksRef;
    private android.support.v4.app.FragmentManager fragmentManager;
    Fragment fragment1;
    Activity activity;

    public MyDoctorListRecyclerViewAdapter(Activity activit, Fragment fragment, FragmentManager fragmentManager1, List<Doctor> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        //...
        fragmentManager = fragmentManager1;
        fragment1 = fragment;
        activity = activit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doctorlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText((position + 1) + "-");

        holder.select.setTag(holder.mItem);

        // set doctor's name in item
        holder.mContentView.setText(mValues.get(position).getFirst_name() + " " + mValues.get(position).getLast_name());
        initDatabase();

        // set the action for select button
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // add doctor for patient
                Constant.patient_doctor = (Doctor) view.getTag();
                Constant.patient.setDoctor(Constant.patient_doctor.getDoctor_id());
                tasksRef.child(Constant.patient.getPatient_id()).child("doctor").setValue(Constant.patient_doctor.getDoctor_id());

                // set doctor fragment
                DoctorFragment fragment2 = new DoctorFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment2);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                Log.e("Log", "onClick: " + Constant.patient_doctor.getDoctor_id());

            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        //...
        public final Button select;
        public Doctor mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            //...
            select = (Button) view.findViewById(R.id.select);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void initDatabase() {
        // set reference on patient in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.PATIENT_FIREBASE);
    }
}
