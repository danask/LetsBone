package com.mobile.letsbone;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatMenuFragment extends Fragment {

    private TextView mTextMessage;
//    private CustomListAdapter adapter;
    DatePickerDialog datePickerDialog;
    DatabaseHelper databaseHelper;
    private String date;
    Fragment fragment = null;
    String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Chat Room");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();

            date = formattedDate;
            String currentFragmentName = "ChatTabFragment";
            Bundle args = new Bundle();
            args.putString("date", date);

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragment = new ChatTabFragment();
                    currentFragmentName = "ChatTabFragment";
                    break;
                case R.id.navigation_dashboard:
                    fragment = new ChatTabFragment();
                    currentFragmentName = "ChatTabFragment";
                    break;
                case R.id.navigation_notifications:
                    fragment = new ChatTabFragment();
                    currentFragmentName = "ChatTabFragment";
                    break;
            }

            editor.putString("currentFragment", currentFragmentName);
            editor.commit();

            return loadFragment(fragment);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_tabs, container, false);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
//        final TextView textViewDate = (TextView)view.findViewById(R.id.textViewChosenDate);

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // initial fragment: frag to frag with bundle
        Fragment fragment = new ChatTabFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        fragment.setArguments(args);

        String currentFragmentName = "ChatTabFragment";
        editor.putString("currentFragment", "ChatTabFragment");
        editor.commit();

        // Dynamic Fragment
        loadFragment(fragment);

        return view;
    }

    private boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            // Dynamic Fragment
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.ChatTabFragment_containter, fragment);  // on the frameLayout
            ft.commit();

            return true;
        }
        return false;
    }

    private ArrayList<String> getItemList(String type)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = null;

        databaseHelper = new DatabaseHelper(getActivity());
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentUser = sharedPref.getString("currentUser", "-");

        // TODO: GET Chat history

        return  arrayList;
    }

}