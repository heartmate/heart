package com.HeartmatePack.heartmate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.HeartmatePack.heartmate.bean.Heart_Rate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    PendingIntent pIntent;
    Intent intents;
    String ma, mi, aids;
    int max, min;
    String[] emg;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        emg = intent.getStringArrayExtra("EMG");

        ma = intent.getStringExtra("MAX");
        max = Integer.parseInt(ma);
        mi = intent.getStringExtra("MIN");
        min = Integer.parseInt(mi);
        aids = intent.getStringExtra("AIDS");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        intents = new Intent(this, MainActivity.class);
        pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intents, 0);
        getRate();

        return super.onStartCommand(intent, flags, startId);
    }

    public void getRate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = simpleDateFormat.format(new Date());

        // set reference on rate in firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.RATE_FIREBASE);
        Log.e(ContentValues.TAG, "onDataChange: " + Constant.patient.getWrist());
        databaseReference.child(Constant.patient.getWrist()).orderByChild("rate_date").equalTo(today).limitToLast(1)

                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.e("Service", "onChildAdded: " + dataSnapshot.getValue());
                        Heart_Rate heart_rate1 = (Heart_Rate) dataSnapshot.getValue(Heart_Rate.class);
                        int rate = Integer.parseInt(heart_rate1.getRate());

                        // the condition to notify for first aid and send SMS
                        if (rate > max || rate < min) {
                            // build notification for first aid
                            Notification n = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("First Aid")
                                    .setContentText(aids)
                                    .setContentIntent(pIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setAutoCancel(true).build();

                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            // start to notify
                            notificationManager.notify(rate, n);

                            // get location for SMS
                            Location location = Util.getLastKnownLocation(getApplicationContext());
                            String uri = "";
                            if (location != null) {
                                uri = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude();

                            }
                            Log.e("Location", uri);


                            // send the message
                            if (emg != null) {
                                for (int i = 0; i < emg.length; i++) {
                                    if (emg[i] != null && !emg[i].isEmpty()) {
                                        SmsManager sms = SmsManager.getDefault();
                                        sms.sendTextMessage(emg[i], null, "I am " + Util.getname() + "\n I need immediate help\n" + uri, null, null);
                                    }
                                }
                            }

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
}
