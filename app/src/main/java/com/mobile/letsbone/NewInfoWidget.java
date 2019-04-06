package com.mobile.letsbone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewInfoWidget extends AppWidgetProvider {

    // Name of shared preferences file & key
    private static final String SHARED_PREF_FILE =
                                "com.mobile.letsbone.infowidget";
    private static final String COUNT_KEY = "count";
    private static int noOfCount = 0;

    static void updateAppWidget(final Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId)
    {
        // 1. LOAD & VIEW
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_FILE, 0);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);





        // Construct the RemoteViews object.
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_info_widget);

        // Get count
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        views.setTextViewText(R.id.app_widget_text, widgetText);

        // Get the current time.
        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        // TODO: People count from Firebase
        int numOfPeople = 0;

        DatabaseReference databaseReference;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String currentUserKey = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    noOfCount++;
//                    System.out.println(noOfCount);
////                    int count = prefs.getInt("NumOfPeople", 0);
////                    count++;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//        final UserProfile user = null;



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                int count = 0;
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    count++;
                    Log.e(snap.getKey(),snap.getChildrenCount() + " total :" + count);
                }
                databaseHelper.setCount(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Cursor c = databaseHelper.getCount();
        String countValue = "";
        if(c.getCount() > 0) {


            while (c.moveToNext()) {
                countValue = c.getString(c.getColumnIndex("Count"));
            }

            System.out.println("--------------------- " + countValue  + "--------------------");
        }

//        System.out.println(user.getLikes());

        views.setTextViewText(R.id.appwidget_id, countValue);  // this should be from # of chat
        views.setTextViewText(R.id.appwidget_update,
                                context.getResources().getString(
                                                        R.string.date_count_format,
                                                        count,
                                                        dateString));


        // 2. Save count back to prefs.
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();


        // 3. INTENT UPDATE via PENDING INTENT
        // Setup update button to send an update request as a pending intent.
        Intent intentUpdate = new Intent(context, NewInfoWidget.class);

        // The intent action must be an app widget update.
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        // Include the widget ID to be updated as an intent extra.
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);


        // PENDING INTENT
        // Wrap it all in a pending intent to send a broadcast.
        // Use the app widget ID as the request code (third argument) so that
        // each intent is unique.
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context,
                                                    appWidgetId,
                                                    intentUpdate,
                                                    PendingIntent.FLAG_UPDATE_CURRENT);


        // 4. Update by BUTTON: view <- pendingUpdate
        // Assign the pending intent to the button onClick handler
        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }


}

