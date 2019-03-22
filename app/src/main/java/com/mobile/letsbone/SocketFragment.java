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

public class SocketFragment extends Fragment {

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

//                case R.id.navigation_image:
//                    fragment = new ChatTabFragment();
//                    currentFragmentName = "ChatTabFragment";
//                    break;
                case R.id.navigation_profile:
                    fragment = new ChatTabFragment();
                    currentFragmentName = "ChatTabFragment";
                    break;
                case R.id.navigation_leave:
                    fragment = new UserListFragment();
                    currentFragmentName = "UserListFragment";
                    break;
            }

            editor.putString("currentFragment", currentFragmentName);
            editor.commit();

            return loadFragment(fragment, currentFragmentName);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_tabs, container, false);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // initial fragment
        Fragment fragment = new ChatTabFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        fragment.setArguments(args);

        String currentFragmentName = "ChatTabFragment";
        editor.putString("currentFragment", "ChatTabFragment");
        editor.commit();

        loadFragment(fragment, currentFragmentName);

        return view;
    }

    private boolean loadFragment(Fragment fragment, String fragmentType)
    {
        if(fragment != null)
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if(fragmentType.equals("UserListFragment"))
                ft.replace(R.id.content_main, fragment);
            else
                ft.replace(R.id.ChatTabFragment_containter, fragment);

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

        // TODO: load

        return  arrayList;
    }

}