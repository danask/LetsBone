package com.mobile.letsbone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper databaseHelper;
    String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");
        String currentUserAmount = sharedPref.getString("currentUserAmount", "-");

        // main menu
        databaseHelper = new DatabaseHelper(this);

        // Drawer Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Navigation bar
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Header of drawer
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) headerView.findViewById(R.id.textViewNavUser);
        String currentUserName = sharedPref.getString("currentUserName", "Douglas") + " / " +
                                sharedPref.getString("currentUser", "dan@douglas.com");
        navUsername.setText(currentUserName);

        TextView textViewNavUserSignOut = (TextView)headerView.findViewById(R.id.textViewNavUserSignOut);
        textViewNavUserSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        //imageViewDrawer
        ImageView imageViewDrawer = (ImageView)headerView.findViewById(R.id.imageViewDrawer);
        imageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void deleteAll(String currentUser)
    {
        databaseHelper = new DatabaseHelper(this);

        // TODO from FB and Local

    }

    public void deleteAllWithoutUser(String currentUser)
    {
        databaseHelper = new DatabaseHelper(this);

        // TODO from FB

        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");

        if(id == R.id.actionSignOut)
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));

        if(id == R.id.actionDelUser);
            // TODO from FB

        if(id == R.id.actionReset);
            // TODO from FB

        if(id == R.id.actionQuit)
        {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit", true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id)
    {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");
        Fragment fragment = null;

        switch (id)
        {
            case R.id.nav_match:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_friendsList:
                // TODO
                break;
            case R.id.nav_chat:
//                fragment = new ChatMenuFragment();
                fragment = new SocketFragment();
                break;
            case R.id.nav_user:
                fragment = new UserFragment();
                break;

            case R.id.nav_settings_delete:
                deleteAll(currentUser);
                break;
            case R.id.nav_settings_reset:
                deleteAllWithoutUser(currentUser);
                break;
            default:
                break;
        }

        if(fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

        return true;
    }


}
