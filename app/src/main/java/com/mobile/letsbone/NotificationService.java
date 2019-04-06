package com.mobile.letsbone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NotificationService extends Service {

    private static final int CHAT_NOTIFY = 0x1001;
    private static final String DEBUG_TAG = "NotificationService";
//    private static final String EXTRA_UPDATE_RATE = "update-rate";

    //    private LocationManager location = null;
    private NotificationManager notifier = null;

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private String signInUser  = "";
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDb;
    private FirebaseAuth firebaseAuth;
    private String currentUserKey;

    public NotificationService() {
    }

    // init
    @Override
    public void onCreate()
    {
        super.onCreate();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");

        firebaseAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserKey = firebaseAuth.getCurrentUser().getUid();

        //        location = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);

        // to ask this to OS (system)
        notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.w("NOTI", "Noti-------------------------------------------");

//        Toast.makeText(getApplicationContext(), "NOTI start with "+ currentUser, Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  // oreo
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "My Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notifier.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");  //
        return null;
    }


    // UnbindService, newer: onStartCommand, cf. onStart: old
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.w(DEBUG_TAG, "onStartCommand called.");

        if(flags !=0)
        {
            Log.w(DEBUG_TAG, "Redelivered or retrying service start: " + flags);
        }

        final Intent tempIntent = intent;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String currentUser = sharedPref.getString("currentUser", "-");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true)
                {
                    try {
                        Thread.sleep(10000);

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                UserProfile users = null;

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    users = child.getValue(UserProfile.class);

                                    if(users.getChat() != null && users.getChat().equalsIgnoreCase(currentUserKey)) {
                                        doServiceStart(tempIntent, 1, users.getFirstName() + " " + users.getLastName());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

//                    if(isRunning){
//                        Log.i(TAG, "Service running");
//                    }
                }

                //Stop service once it finishes its task (in 5sec)
//                stopSelf(); // by itself
            }
        }).start();

//        return Service.START_STICKY;
        return Service.START_REDELIVER_INTENT;
    }


    // notification -> intent
    public void doServiceStart(Intent intent, int id, String name)
    {


        Intent toLaunch = new Intent(getApplicationContext(), MainActivity.class);

        // for foreign app with your permission
        PendingIntent intentBack = PendingIntent.getActivity(
                getApplicationContext(), 0, toLaunch, 0);

        // Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),NOTIFICATION_CHANNEL_ID);

       // currently support for local user
        builder.setTicker("Chat Alarm");
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Chat Alarm");
        builder.setContentText(name + " want to talk with you");

        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notifier.notify(CHAT_NOTIFY, notify);

    }

}
