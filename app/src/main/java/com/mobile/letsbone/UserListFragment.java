package com.mobile.letsbone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class UserListFragment extends Fragment {

    private Context context;
    private ListCustomAdapter adapter;
    private ListView listView;
    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private String userId;

//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("message");

    ConstraintLayout myLayout;
    ArrayList<String> myList = new ArrayList<>(Arrays.asList(
            "Bob (M)",
            "Joe (M)",
            "Buddy (M)",
            "Blake (F)",
            "Anna (F)"));
    ArrayList<Integer> myImageList = new ArrayList<>(Arrays.asList(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5));

    ArrayList<String> myDirList = new ArrayList<>(Arrays.asList(
            "Vancouver, BC",
            "Surrey, BC",
            "Coquitlam, BC",
            "Burnaby, BC",
            "New Westminster, BC")
    );

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Friends List");


        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });



//        loadListView(view);
//        onClickEvent(view);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);  // list on list fragment

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String currentUser = sharedPref.getString("currentUser", "-");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        // Temp for test
//        String userID = firebaseAuth.getCurrentUser().getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserProfile").child(userID);
//
//        Map userInfo = new HashMap<>();
//        userInfo.put("FirstName", "Jane");
//        userInfo.put("LastName", "March");
//        userInfo.put("EmailAddress", "jane@dc.com");
//        userInfo.put("Gender", "Female");
//        userInfo.put("LookingFor", "Female");
//        userInfo.put("Age", 20);
//        userInfo.put("DogName", "Jenny");
//        userInfo.put("DogBreed", "BBB");
//        userInfo.put("DogGender", "Male");
//        userInfo.put("DogAge", "Less than 1");
//        userInfo.put("Likes", 0);
//        userInfo.put("Matches", "iG4CGgTASLSQvBZe9PA7wmj9xTF3");
//        userInfo.put("Status", 1);
//
//        databaseReference.updateChildren(userInfo);



        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("UserProfile");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserProfile users = null;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    users = child.getValue(UserProfile.class);
                    userId = child.getKey();

                    // Display newly updated name and email

                    if(!users.getEmailAddress().equalsIgnoreCase(currentUser)) {
                        myList.add(users.getFirstName() + " " + users.getLastName() + ", " + users.getEmailAddress());
                        myImageList.add(R.drawable.dog3);
                        myDirList.add("Looking for " + users.getLookingFor());
                    }
                }



                adapter = new ListCustomAdapter(myList, myImageList, myDirList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);

//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_main, new SocketFragment());
//                ft.commit();

                listItem.getClass();


                Intent i  = new Intent(getContext(),ChatBoxActivity.class);
                //retreive nickname from textview and add it to intent extra
                i.putExtra("NICKNAME", listItem.toString());

                startActivity(i);

                //Toast.makeText(getActivity(), "selected chat "+position, Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


    private void loadListView(View view)
    {
        adapter = new ListCustomAdapter(myList, myImageList, myDirList);
        listView.setAdapter(adapter);

//        Button addButton = (Button)view.findViewById(R.id.addExpenseItemButton);
//        ImageButton calButton = (ImageButton)view.findViewById(R.id.imageCalendarButton);
//        final TextView textViewDate = (TextView)view.findViewById(R.id.textViewChosenDate);

//        // Default date
//        String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
//        textViewDate.setText(formattedDate);
//        date = formattedDate;

        // Calendar event
//        calButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                datePickerDialog = new DatePickerDialog(getActivity(),
//
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
////                                Toast.makeText(getActivity(), dayOfMonth +"", Toast.LENGTH_SHORT).show();
//                                String tempDate = ((month < 9)? "0"+(month+1): month+1) + "/" + ((dayOfMonth < 10)? "0"+(dayOfMonth): dayOfMonth) + "/" + year;
//
//                                textViewDate.setText(tempDate);
//                                date = tempDate;  // to save
//                                loadRecordFromDB(listView, tempDate); // reload
//                            }
//                        },
//                        year, month, day);
//                datePickerDialog.show();
//
//            }
//        });

        // load records
//        loadRecordFromDB(listView, date);

//        // Add new items
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                addNewRecord(listView);
//            }
//        });
    }
}
