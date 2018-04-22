package com.HeartmatePack.heartmate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.HeartmatePack.heartmate.PatientListFragment.OnListFragmentInteractionListener;
import com.HeartmatePack.heartmate.bean.Patient;
import com.HeartmatePack.heartmate.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPatientRecyclerViewAdapter extends RecyclerView.Adapter<MyPatientRecyclerViewAdapter.ViewHolder> {

    private final List<Patient> mValues;
    private final OnListFragmentInteractionListener mListener;
    Activity activity;

    public MyPatientRecyclerViewAdapter(Activity activity1,List<Patient> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        activity=activity1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.e("P. recycle", "onCreateView: "+getItemCount() );

        // set the patient's position
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(position + "-");

        // set the patient's name
        holder.mContentView.setText(mValues.get(position).getFirst_name() + " " + mValues.get(position).getLast_name());

        // if doctor is already accepted the patient
        if (holder.mItem.getDoctor_ver() != null && !holder.mItem.getDoctor_ver().isEmpty() && holder.mItem.getDoctor_ver().equals("1")) {
            holder.control.setVisibility(View.GONE);
        } else {
            holder.control.setVisibility(View.VISIBLE);
        }

        holder.patient_selected.setTag(mValues.get(position));
        holder.reject.setTag(mValues.get(position));
        holder.accept.setTag(mValues.get(position));

        // select button to select a patient
        holder.patient_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the selected patient and save locally in selected_patient
                Constant.Selected_patient = (Patient) view.getTag();
                Intent intent = new Intent(activity, PatientSelectedActivity.class);
               activity.startActivity(intent);

            }
        });

        // when reject the patient adding
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // show confirmation message
                new AlertDialog.Builder(activity)
                        .setTitle("Patient Acceptance:")
                        .setMessage("Are you sure ? this Patient will Rejected ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Constant.Selected_patient=(Patient) view.getTag();

                                // reject patient and remove doctor from patient in database
                                Util.acceptOrRejectPatient(false);
                                mValues.remove(Constant.Selected_patient);
                                
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        // when accept the patient adding
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Patient Acceptance:")
                        .setMessage("Are you sure ? this Patient will Accepted ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Constant.Selected_patient=(Patient) view.getTag();

                                // accept patient and add doctor to patient in database
                                Util.acceptOrRejectPatient(true);
                                mValues.remove(Constant.Selected_patient);
                                Constant.Selected_patient.setDoctor_ver("1");

                                // update database
                                mValues.add(Constant.Selected_patient);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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
        public final Button accept, reject;
        public final LinearLayout control;
        public final RelativeLayout patient_selected;
        public Patient mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            accept = view.findViewById(R.id.accept);
            reject = view.findViewById(R.id.reject);
            control = view.findViewById(R.id.control);
            patient_selected=view.findViewById(R.id.patient_selected);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
