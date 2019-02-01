package com.mobile.letsbone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // to quit command
        if(getIntent().getBooleanExtra("Exit", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        Button buttonSignIn = (Button)findViewById(R.id.buttonSignIn);

        final EditText editTextEmailSignIn = (EditText)findViewById(R.id.editTextEmailSignIn);
        final EditText editTextPwdSignIn = (EditText)findViewById(R.id.editTextPwdSignIn);
        final TextView textViewSignIn = (TextView)findViewById(R.id.textViewSignIn);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        databaseHelper = new DatabaseHelper(this);

        editTextEmailSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextEmailSignIn.getText()))
                {
                    editTextEmailSignIn.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextPwdSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextPwdSignIn.getText()))
                {
                    editTextPwdSignIn.setError(getString(R.string.error_field_required));
                }
            }
        });


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // DB check whether it exists
                String userEmail = "bone@douglascollege.ca";//editTextEmailSignIn.getText().toString();

                if(!userEmail.isEmpty() && !editTextPwdSignIn.getText().toString().isEmpty())
                {
//                    Cursor cursorUser = databaseHelper.getUserProfileByEmail(userEmail);
                    String currentUserAmountMonth = sharedPref.getString("currentUserAmountMonth", null);
                    String currentMonth = formattedDate.substring(0, 2);

//                    if(cursorUser.getCount() > 0 )
                    if(true)
                    {
//                        while(cursorUser.moveToNext())
                        {
                            String userFName = "Douglas";
                            String userLName = "College";
                            String arg = "0";


                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("currentUser", userEmail);
                            editor.putString("currentUserName", userFName + " " + userLName);
                            editor.putString("currentUserExtra", arg);

//                            if(currentUserAmountMonth.equalsIgnoreCase(null) ||
//                                    !currentUserAmountMonth.substring(0,2).equalsIgnoreCase(currentMonth))
//                                editor.putString("currentUserAmountMonth", currentMonth+userMonthlyIncome);

                            editor.commit();
                        }

                        new CustomProgressWheel().execute();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                        builder.setMessage("Invalid input. Do you want to create an account?");
                        builder.setCancelable(true);
                        builder.setTitle("Error");

                        builder.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
                                    }
                                });

                        builder.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }

                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    builder.setMessage("Invalid input. Do you want to create an account?");
                    builder.setCancelable(true);
                    builder.setTitle("Error");

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
                                }
                            });

                    builder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
            }
        });

    }

    private class CustomProgressWheel extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                    TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
