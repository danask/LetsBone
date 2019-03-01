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
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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


    public NotificationService() {
    }

    // init
    @Override
    public void onCreate()
    {
        super.onCreate();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");

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
                    int id = new Random().nextInt(100);

                    try {
                        Thread.sleep(1000);

                        // TODO: comparing with user came from Firebase
//                        if(!signInUser.equals(currentUser))
                            doServiceStart(tempIntent, id, currentUser);
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
        builder.setTicker("Chat User Tracking");
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Chat User Tracking");
        builder.setContentText(name + "-"+id + " signed in Lets Bone");

        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notifier.notify(CHAT_NOTIFY, notify);

    }

}
