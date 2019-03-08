package com.mobile.letsbone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class CardSwipe extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private PictureArrayAdapter mAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_swipe);

        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer =(SwipeFlingAdapterView)findViewById(R.id.cardFrame) ;

        final ArrayList<ProfileData> al = new ArrayList<>();
        //al.add(new ProfileData("Bob", "25","Vancouver, BC", R.drawable.dog1));
        //al.add(new ProfileData("Joe", "21","Surrey, BC", R.drawable.dog2));
        //al.add(new ProfileData("Buddy", "27","Coquitlam, BC", R.drawable.dog3));
        //al.add(new ProfileData("Tyson", "19","Surrey, BC", R.drawable.dog4));
        //al.add(new ProfileData("Tyrone", "30","Vancouver, BC", R.drawable.dog5));
        al.add(new ProfileData("Bob", "Sura", "M", "25", "Vancouver, BC",
                5, R.drawable.dog1, "F"));
        al.add(new ProfileData("Joe", "Dumars", "M", "21", "Surrey, BC",
                10, R.drawable.dog2, "F"));
        al.add(new ProfileData("Buddy", "Hield", "M", "27", "Coquitlam, BC",
                8, R.drawable.dog3, "F"));
        al.add(new ProfileData("Blake", "Lively", "F", "27", "Burnaby, BC",
                15, R.drawable.dog4, "M"));
        al.add(new ProfileData("Anna", "Kendrick", "F", "26", "New Westminster, BC",
                11, R.drawable.dog5, "M"));




        //choose your favorite adapter

        mAdapter = new PictureArrayAdapter(this, al);

        //set the listener and the adapter
        flingContainer.setAdapter(mAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(CardSwipe.this, "Dislike!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(CardSwipe.this, "Like!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                /*al.add(new ProfileData("No More Swipes", null, null, null, null, 0, 0, null));
                mAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;*/
                return;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

    }
}