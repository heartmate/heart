package com.HeartmatePack.heartmate;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.HeartmatePack.heartmate.bean.Heart_Rate;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    // get views from layout
    TextView name, rate_date;
    private DatabaseReference tasksRef;
    GraphView graph;
    TableLayout table;
    int rowsNum = 0;

    // object to draw graph
    private LineGraphSeries<DataPoint> series;

    // array list to store all haer rates from firebase
    ArrayList<Heart_Rate> heart_rates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        // get the views in the layout
        name = findViewById(R.id.name);
        rate_date = findViewById(R.id.rate_date);
        name.setText(Constant.Selected_patient.getFirst_name() + " " + Constant.Selected_patient.getLast_name());
        graph = (GraphView) findViewById(R.id.graph);
        table = (TableLayout) findViewById(R.id.table);

        series = new LineGraphSeries<DataPoint>();

        table.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow tr_head = new TableRow(this);



        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT
        ));

        // create table view in layout
        TextView textArray1 = new TextView(this);
        textArray1.setText("Time");
        textArray1.setGravity(Gravity.CENTER_HORIZONTAL);
        textArray1.setTextSize(20);

        textArray1.setTextColor(Color.BLACK);
        textArray1.setBackgroundResource(R.drawable.cell_shape);
        textArray1.setPadding(50, 20, 50, 20);
        tr_head.addView(textArray1);

        TextView textArray = new TextView(this);
        textArray.setText("Rate");
        textArray1.setGravity(Gravity.CENTER_HORIZONTAL);
        textArray.setTextColor(Color.BLACK);
        textArray.setTextSize(20);
        textArray.setPadding(50, 20, 50, 20);
        textArray.setBackgroundResource(R.drawable.cell_shape);
        tr_head.addView(textArray);

        // add row to table
        table.addView(tr_head);
        initDatabasep();
        loadData();

        // start draw the grapgh
        graph.addSeries(series);
    }

    private void initDatabasep() {
        // set reference on heart_rate in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference(Constant.RATE_FIREBASE);
    }

    private void loadData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String today = simpleDateFormat.format(new Date());
        Log.e("History", "onChildAdded: " + today);

        rate_date.setText(today);

        
        // set reference on heart_rate in firebase
        tasksRef.child(Constant.Selected_patient.getWrist()).orderByChild("rate_date")
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("History", "onChildAdded: " + dataSnapshot.getValue());
                        Heart_Rate heart_rate = (Heart_Rate) dataSnapshot.getValue(Heart_Rate.class);
                        if (!heart_rates.contains(heart_rate)) {
                            heart_rates.add(heart_rate);

                            Log.e("History", heart_rate.getRate_date() + ", " + heart_rate.getRate_time() + " = " + Double.parseDouble(heart_rate.getRate()));
                            addToTable();
                        }
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

    // method to add data to table view
    public void addToTable() {

        Heart_Rate heartRate = heart_rates.get(rowsNum);
        String time = heartRate.getRate_date() + " - " + heartRate.getRate_time();
        Double rate = Double.parseDouble(heartRate.getRate());

        TableRow tr = new TableRow(this);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.setId(rowsNum);

        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        ));


        TextView dateText = new TextView(this);
        dateText.setText(time);
        dateText.setTextColor(Color.BLACK);
        dateText.setBackgroundResource(R.drawable.cell_shape);
        dateText.setPadding(50, 20, 50, 20);
        tr.addView(dateText);

        TextView rateText = new TextView(this);
        rateText.setId(rowsNum);
        rateText.setText(rate+"");
        rateText.setTextColor(Color.BLACK);
        rateText.setPadding(50, 20, 50, 20);
        rateText.setBackgroundResource(R.drawable.cell_shape);
        tr.addView(rateText);

        Log.e("rate" , rate + "");
        Log.e("row number", rowsNum + "");

        table.addView(tr);
        rowsNum++;

        // add the point to series
        series.appendData(new DataPoint(rowsNum+0.0 , rate),false,heart_rates.size());
    }

}
