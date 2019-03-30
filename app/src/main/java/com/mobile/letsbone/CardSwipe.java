package com.mobile.letsbone;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class CardSwipe extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private PictureArrayAdapter mAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUserKey;
    ArrayList<ProfileData> al;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_card_swipe, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Match");


        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserKey = mAuth.getCurrentUser().getUid();


        al = new ArrayList<>();

        checkUserSex();

        //al.add(new ProfileData("Bob", "25","Vancouver, BC", R.drawable.dog1));
        //al.add(new ProfileData("Joe", "21","Surrey, BC", R.drawable.dog2));
        //al.add(new ProfileData("Buddy", "27","Coquitlam, BC", R.drawable.dog3));
        //al.add(new ProfileData("Tyson", "19","Surrey, BC", R.drawable.dog4));
        //al.add(new ProfileData("Tyrone", "30","Vancouver, BC", R.drawable.dog5));


        /*
        al.add(new ProfileData("Bob", "Sura", "M", "25", "Vancouver, BC",
                5, R.drawable.dog6, "F"));
        al.add(new ProfileData("Joe", "Dumars", "M", "21", "Surrey, BC",
                10, R.drawable.dog6, "F"));
        al.add(new ProfileData("Buddy", "Hield", "M", "27", "Coquitlam, BC",
                8, R.drawable.dog6, "F"));
        al.add(new ProfileData("Blake", "Lively", "F", "27", "Burnaby, BC",
                15, R.drawable.dog6, "M"));
        al.add(new ProfileData("Anna", "Kendrick", "F", "26", "New Westminster, BC",
                11, R.drawable.dog6, "M"));

        */


        //choose your favorite adapter

        mAdapter = new PictureArrayAdapter(getActivity(), R.layout.card_item, al);

        SwipeFlingAdapterView flingContainer =(SwipeFlingAdapterView) getView().findViewById(R.id.cardFrame) ;

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
                ProfileData profileObject = (ProfileData)dataObject;
                String userId = profileObject.getUserId();
                usersDb.child(userId).child("Connections").child("Nah").child(currentUserKey).setValue(true);
                Toast.makeText(getActivity(), "Dislike!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                ProfileData profileObject = (ProfileData) dataObject;
                String userId = profileObject.getUserId();
                usersDb.child(userId).child("Connections").child("Yee").child(currentUserKey).setValue(true);
                Toast.makeText(getActivity(), "Like!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private String usersGender;
    private String oppositeUserSex;
    public void checkUserSex(){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference user =  usersDb.child(userId);


        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("Gender").getValue()!= null){
                        usersGender = dataSnapshot.child("Gender").getValue().toString();
                        switch (usersGender){
                            case "Male":
                                oppositeUserSex ="Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                    }

                    getOppositeSexUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //DatabaseReference userDb =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        /*
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String fName = dataSnapshot.child("FirstName").getValue().toString();

                al.add(new ProfileData(fName, "Sura", "M", "25", "Vancouver, BC",
                        5, R.drawable.dog6, "F"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        //DatabaseReference userDb = ref.child("")
        /*
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("Gender").getValue() != null){

                        userSex = dataSnapshot.child("Gender").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }

    public  void getOppositeSexUsers(){
            usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists() && dataSnapshot.child("Gender").getValue().toString().equals(oppositeUserSex)){

                    al.add(new ProfileData(dataSnapshot.getKey(),dataSnapshot.child("FirstName").getValue().toString(),
                            dataSnapshot.child("LastName").getValue().toString(),dataSnapshot.child("Gender").getValue().toString(),
                            dataSnapshot.child("Age").getValue().toString(),"New WestMinister", 11, R.drawable.dog6,"M"));

                    mAdapter.notifyDataSetChanged();
                }

               /* if(dataSnapshot.child("Gender").getValue() != null){
                    if(dataSnapshot.exists()&& !dataSnapshot.child("Matches").child("Nah").hasChild(currentUid)
                    && !dataSnapshot.child("Matches").child("Yee").hasChild(currentUid)
                            && dataSnapshot.child("Gender").getValue().toString().equals(oppositeUserSex)){
                        String profileImageUrl = "default";

                        if(!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }

                        ProfileData profileData = new ProfileData(dataSnapshot.child("FirstName").getValue().toString(),
                                dataSnapshot.child("LastName").getValue().toString(),dataSnapshot.child("Gender").getValue().toString(),
                                dataSnapshot.child("Age").getValue().toString(),"New WestMinister", 11, R.drawable.dog6,"M");

                        al.add(profileData);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }*/

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}