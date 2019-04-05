package com.mobile.letsbone.Matches;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mobile.letsbone.Entities.ProfileData;
import com.mobile.letsbone.R;

import java.util.ArrayList;

public class CardSwipe extends Fragment {

    private ArrayAdapter<String> arrayAdapter;
    private PictureArrayAdapter mAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String currentUserKey;
    ArrayList<ProfileData> al;

    Dialog itsAMatchPopup;

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

        //pulling data from Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserKey = mAuth.getCurrentUser().getUid();


        al = new ArrayList<>();

        //when view is created you wont be able to swipe on your own card
        databaseReference
                .child(currentUserKey)
                .child("Connections")
                .child("No")
                .child(currentUserKey)
                .setValue(true);

        //initiating gender check method
        genderCheck();

        //choose your favorite adapter

        mAdapter = new PictureArrayAdapter(getActivity(), R.layout.card_item, al);

        //Card Swipe Library call
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

            //When user swipes left on card
            @Override
            public void onLeftCardExit(Object dataObject) {
                ProfileData profileObject = (ProfileData)dataObject;
                String userKey = profileObject.getUserId();

                //creates a child No for opposite users with currents users key set to true
                databaseReference
                        .child(userKey)
                        .child("Connections")
                        .child("No")
                        .child(currentUserKey)
                        .setValue(true);
            }

            //When users swipe right on card
            @Override
            public void onRightCardExit(Object dataObject) {
                ProfileData profileObject = (ProfileData) dataObject;
                String userKey = profileObject.getUserId();

                //creates a child Yes for opposite users with currents user set to true
                databaseReference
                        .child(userKey)
                        .child("Connections")
                        .child("Yes")
                        .child(currentUserKey)
                        .setValue(true);

                matchUsers(userKey);
            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
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
    private String lookingFor;
    // check the current users gender
    public void genderCheck(){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference user =  databaseReference.child(userId);


        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("LookingFor").getValue()!= null){
                        usersGender = dataSnapshot.child("LookingFor").getValue().toString();
                        switch (usersGender){
                            case "Male":
                                lookingFor ="Male";
                                break;
                            case "Female":
                                lookingFor = "Female";
                                break;
                        }
                    }
                    addUserCards();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }//end of genderCheck method

    //pull users from firebase based on gender
    public  void addUserCards(){
            databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists()
                        && dataSnapshot.child("Gender").getValue().toString().equals(lookingFor)
                        && !dataSnapshot.child("Connections").child("No").hasChild(currentUserKey)
                        && !dataSnapshot.child("Connections").child("Yes").hasChild(currentUserKey)){

                    al.add(new ProfileData(dataSnapshot.getKey(),
                            dataSnapshot.child("FirstName").getValue().toString(),
                            dataSnapshot.child("LastName").getValue().toString(),
                            dataSnapshot.child("Gender").getValue().toString(),
                            dataSnapshot.child("Age").getValue().toString(),
                            "New WestMinister",
                            11,
                            dataSnapshot.child("LookingFor").getValue().toString(),
                            dataSnapshot.child("ImageUrl").getValue().toString()));

                    mAdapter.notifyDataSetChanged();
                }

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
    }// end of addUserCards method

    //Its a Match pop up
    public void itsAMatchPopup(){
        itsAMatchPopup = new Dialog(getContext());
        itsAMatchPopup.setContentView(R.layout.itsamatchpopup);
        itsAMatchPopup.show();
        itsAMatchPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //connect users that liked each other
    private void matchUsers(String userKey){
        DatabaseReference userConnections = databaseReference.child(currentUserKey).child("Connections").child("Yes").child(userKey);

        userConnections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    String messageId = FirebaseDatabase.getInstance().getReference().child("Messages").push().getKey();

                    databaseReference
                            .child(dataSnapshot.getKey())
                            .child("Connections")
                            .child("Matches")
                            .child(currentUserKey)
                            .child("MessageId")
                            .setValue(messageId);

                    databaseReference
                            .child(currentUserKey)
                            .child("Connections")
                            .child("Matches")
                            .child(dataSnapshot.getKey())
                            .child("MessageId")
                            .setValue(messageId);

                    itsAMatchPopup();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//end of matchUser method

}