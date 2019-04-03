package com.mobile.letsbone.FriendsList;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.letsbone.Entities.UserProfile;
import com.mobile.letsbone.R;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter listCustomAdapter;
    private ArrayList<UserProfile> userProfileMatches = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUserKey;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        listCustomAdapter = new FriendsListCustomAdapter(userProfileMatches, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(listCustomAdapter);

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
            getContext()
        ));



        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Friends List");

        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserKey = mAuth.getCurrentUser().getUid();

        //getting current users key and setting it to userKey string
        //userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getMatches();

    }

    //get id of the current users matches in firebase
   private void getMatches(){

        DatabaseReference matchId = usersDb.child(currentUserKey).child("Connections").child("Matches");
        matchId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        getMatchesInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get matches information
    private void getMatchesInformation(String key){
        DatabaseReference matchInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        matchInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userKey = dataSnapshot.getKey();
                    String firstName  = dataSnapshot.child("FirstName").getValue().toString();
                    String lastName = dataSnapshot.child("LastName").getValue().toString();
                    int profileImage = R.drawable.dog6;


                    userProfileMatches.add(new UserProfile(userKey, firstName, lastName, profileImage));
                    listCustomAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //list object of User Profile to return match results
    private List<UserProfile> getListOfMatch(){
        return userProfileMatches;
    }





}