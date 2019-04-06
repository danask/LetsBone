package com.mobile.letsbone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    DatabaseHelper databaseHelper;
    String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
    String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");
//        String currentUserName = sharedPref.getString("currentUserName", "-");

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
        String currentUserName = /*sharedPref.getString("currentUserName", "Douglas") + " / " +*/
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
        final ImageView imageViewDrawer = (ImageView)headerView.findViewById(R.id.imageViewDrawer);
        imageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PhotoUploadActivity.class));
            }
        });

        String currentUserKey = auth.getCurrentUser().getUid();
        DatabaseReference user = databaseReference.child(currentUserKey);

        //pulling image url from firebase and assigning it to imageViewDrawer
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("ImageUrl").getValue() != null){
                        String imageUrl = dataSnapshot.child("ImageUrl").getValue().toString();
                        Picasso.with(MainActivity.this).load(imageUrl).fit().centerCrop().placeholder(R.drawable.user).into(imageViewDrawer);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });






//        DownloadImageTask downloadImageTask = new DownloadImageTask();
//        PhotoUploadActivity photoUploadActivity = new PhotoUploadActivity();
//        photoURL = photoUploadActivity.getRandomUID();
//        Toast.makeText(this, photoURL, Toast.LENGTH_SHORT).show();
//        downloadImageTask.execute(new String[] { photoURL });
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            Bitmap userPhotoBitmap = null;
//            for (String string : strings) {
//                userPhotoBitmap = downloadImage(photoURL);
//            }
//            return userPhotoBitmap;
//        }
//    }
//
//    private Bitmap downloadImage(String photoURL) {
//        Bitmap bitmap = null;
//        InputStream inputStream = null;
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inSampleSize = 1;
//
//        try {
//            inputStream = getHttpConnection(photoURL);
//            bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions);
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    private InputStream getHttpConnection(String photoURL) throws IOException{
//        InputStream inputStream = null;
//        URL url = new URL(photoURL);
//        URLConnection connection = url.openConnection();
//
//        try {
//            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
//            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.connect();
//
//            if(httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK) {
//                inputStream = httpURLConnection.getInputStream();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return inputStream;
//    }

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
        {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

//        if(id == R.id.actionDelUser);
//            // TODO from FB
//
//        if(id == R.id.actionReset);
//            // TODO from FB

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
            case R.id.nav_home:
                fragment = new UserListFragment();
                break;
            case R.id.nav_match:
                fragment = new CardSwipe();
                break;
            case R.id.nav_friendsList:
                fragment = new UserListFragment();
                break;
//            case R.id.nav_chat:
//                startActivity(new Intent(getApplicationContext(), ChatBoxActivity.class));
//                break;
            case R.id.nav_user:
                fragment = new UserFragment();
                break;



            case R.id.nav_settings_reset:
//                deleteAllWithoutUser(currentUser);
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                break;

            case R.id.nav_settings_delete:
                Intent intent = new Intent(this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit", true);
                startActivity(intent);
                finish();
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
