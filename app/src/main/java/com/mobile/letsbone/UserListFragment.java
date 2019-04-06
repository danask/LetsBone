package com.mobile.letsbone;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserListFragment extends Fragment {

    private Context context;
    private ListCustomAdapter adapter;
    private ListView listView;
    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserKey;
    private UserProfile currentUser;
    private Map userInfo = new HashMap<>();

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    ArrayList<String> myList = new ArrayList<>();
    ArrayList<Integer> myImageList = new ArrayList<>();
    ArrayList<String> myDirList = new ArrayList<>();
    ArrayList<String> userKeyList = new ArrayList<>();

//    ArrayList<String> myList = new ArrayList<>(Arrays.asList(
//            "Bob (M)",
//            "Joe (M)",
//            "Buddy (M)",
//            "Blake (F)",
//            "Anna (F)"
//    ));
//    ArrayList<Integer> myImageList = new ArrayList<>(Arrays.asList(
//            R.drawable.dog1,
//            R.drawable.dog2,
//            R.drawable.dog3,
//            R.drawable.dog4,
//            R.drawable.dog5
//    ));
//
//    ArrayList<String> myDirList = new ArrayList<>(Arrays.asList(
//            "Miniature Schnauzer, Male",
//            "Yorkshire Terrier, Female",
//            "Labrador Retriever, Male",
//            "Chihuahua, Female",
//            "Poodle, Female")
//    );
//
//    ArrayList<String> userKeyList = new ArrayList<>(Arrays.asList(
//            "1",
//            "2",
//            "3",
//            "4",
//            "5"
//    ));

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Friends List");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserKey = firebaseAuth.getCurrentUser().getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                DatabaseReference matchId = FirebaseDatabase.getInstance().getReference().
                        child("Users").
                        child(currentUserKey).
                        child("Connections").
                        child("Matches");

                matchId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot match : dataSnapshot.getChildren())
                            {
                                getMatchInfo(match.getKey());
                            }

                            myList.clear();
                            myImageList.clear();
                            myDirList.clear();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                listItem.getClass();
                Intent i  = new Intent(getContext(),ChatBoxActivity.class);

                i.putExtra("NICKNAME", listItem.toString());
                i.putExtra("USER_KEY", userKeyList.get(position));

//                Intent intent;
//                NotificationService.doServiceStart(new Intent(), 9992, "test");
//                triggerService();
                updateChat(userKeyList.get(position));
                startActivity(i);
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);  // list on list fragment

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String currentUser = sharedPref.getString("currentUser", "-");
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);

        return view;
    }

    public void getMatchInfo(String key)
    {
        DatabaseReference matchInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        matchInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myList.add(dataSnapshot.child("FirstName").getValue().toString() + " " + dataSnapshot.child("LastName").getValue().toString()
                            + " " + ((dataSnapshot.child("Gender").getValue().toString().equals("Male"))? "(M)" : "(F)"));
                    myImageList.add(R.drawable.dog3);
                    myDirList.add(dataSnapshot.child("DogBreed").getValue().toString() + ", "
                            + dataSnapshot.child("DogGender").getValue().toString());
                    userKeyList.add(dataSnapshot.getKey());


                    adapter = new ListCustomAdapter(myList, myImageList, myDirList);

                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void updateChat(String partnerUserKey)
    {
        final String pKey = partnerUserKey;
        DatabaseReference currentUserInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserKey);


        currentUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                currentUser = dataSnapshot.getValue(UserProfile.class);

                userInfo.put("FirstName", currentUser.getFirstName());
                userInfo.put("LastName", currentUser.getLastName());
                userInfo.put("EmailAddress", currentUser.getEmailAddress());
                userInfo.put("Gender", currentUser.getGender());
                userInfo.put("LookingFor", currentUser.getLookingFor());
                userInfo.put("Age", currentUser.getAge());
                userInfo.put("DogName", currentUser.getDogName());
                userInfo.put("DogBreed", currentUser.getDogBreed());
                userInfo.put("DogGender", currentUser.getDogGender());
                userInfo.put("DogAge", currentUser.getDogAge());
                userInfo.put("Likes", 0);

                if(currentUser.getChat() != null && currentUser.getChat().equalsIgnoreCase(pKey))
                    userInfo.put("Chat", "");
                else
                    userInfo.put("Chat", pKey);

                userInfo.put("Status", 1);

                DatabaseReference updateUserInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserKey);
                updateUserInfo.updateChildren(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    // AsyncTask
    private class CustomProgressWheel extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void value)
        {
            super.onPostExecute(value);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            try
            {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
