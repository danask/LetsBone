package com.mobile.letsbone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserListFragment extends Fragment {

    private Context context;
    private ListCustomAdapter adapter;
    private ListView listView;

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


//        loadListView(view);
//        onClickEvent(view);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);  // list on list fragment

        adapter = new ListCustomAdapter(myList, myImageList, myDirList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, new SocketFragment());
                ft.commit();

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
