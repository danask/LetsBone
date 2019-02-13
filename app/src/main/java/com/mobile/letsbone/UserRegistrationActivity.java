package com.mobile.letsbone;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[\\p{L} .'-]+$");
    public static final Pattern VALID_PWD_REGEX =
            Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}");  //(?=.*[@#$%^&+=]) for easy input

    final String EMAIL = "email";
    final String PHONE_NUMBER = "phone number";
    final String FNAME = "first name";
    final String LNAME = "last name";
    final String PWD = "password";
    final String GENDER = "gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2476a6")));

        final EditText editTextFName = (EditText)findViewById(R.id.editTextFName);
        final EditText editTextLName = (EditText)findViewById(R.id.editTextLName);
        final EditText editTextPwd = (EditText)findViewById(R.id.editTextPwd);
        final EditText editTextCPwd = (EditText)findViewById(R.id.editTextCPwd);
        final EditText editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        final EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        final EditText editTextGender = (EditText)findViewById(R.id.editTextGender);

        final TextView textViewLastUpdated = (TextView)findViewById(R.id.textViewLastUpdated);
        Button regButton = (Button)findViewById(R.id.userRegButton);
        databaseHelper = new DatabaseHelper(this);

        // initialize
        final String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        textViewLastUpdated.setText("Created on "+ formattedDate);
        editTextGender.setHint("M, F, or H");

        // Validation check
        editTextFName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextFName.getText()))
                {
                    editTextFName.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextPwd.getText()))
                {
                    editTextPwd.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextCPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextCPwd.getText()))
                {
                    editTextCPwd.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextPhoneNumber.getText()))
                {
                    editTextPhoneNumber.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextEmail.getText()))
                {
                    editTextEmail.setError(getString(R.string.error_field_required));
                }

                if (editTextEmail.getText().length() < 5)
                {
                    editTextEmail.setError(getString(R.string.error_range));
                }
            }
        });

        editTextGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextGender.getText()))
                {
                    editTextGender.setError(getString(R.string.error_field_required));
                }

                if (editTextGender.getText().length() < 3)
                {
                    editTextGender.setError(getString(R.string.error_range));
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = editTextEmail.getText().toString();
                String userFName = editTextFName.getText().toString();
                String userLName = editTextLName.getText().toString();
                String userPwd = editTextPwd.getText().toString();
                String userCPwd = editTextCPwd.getText().toString();
                String userPhone = editTextPhoneNumber.getText().toString();
                String userGender = editTextGender.getText().toString();

                if(!userEmail.isEmpty() && !userCPwd.isEmpty() && !userGender.isEmpty())
                {
//                    Cursor cursorUser = databaseHelper.getUserProfileByEmail(userEmail);
//
//                    if (cursorUser.getCount() > 0)
//                    {
//                        alertDialogPopUp("That e-mail address already exists. Please try again.");
//                    }
//                    else {
//
//                        double userIncomeNumber = 0.0;
//                        userIncomeNumber = Double.parseDouble(userIncome);
//
//                        if (userPwd.equalsIgnoreCase(userCPwd))
//                        {
//                            if(!isValid(userEmail, EMAIL))
//                                alertDialogPopUp(EMAIL);
//
//                            else if(!isValid(userFName, FNAME))
//                                alertDialogPopUp( FNAME);
//
//                            else if(!isValid(userFName, LNAME))
//                                alertDialogPopUp(LNAME);
//
//                            else if(!isValid(userPwd, PWD))
//                                alertDialogPopUp(PWD);
//
//                            else if(!isValid(userPhone, PHONE_NUMBER))
//                            {
//                                alertDialogPopUp(PHONE_NUMBER);
//                            }
//
//                            else if(userIncomeNumber < 300)
//                                alertDialogPopUp(INCOME);
//
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(), "Registered successfully",Toast.LENGTH_SHORT).show();
//
////                                if (databaseHelper.addUser(userFName,
////                                        userLName,
////                                        userPwd,
////                                        userPhone,
////                                        userEmail,
////                                        userIncomeNumber,
////                                        formattedDate
////                                )) {
////                                    Toast.makeText(getApplicationContext(), "Registered successfully",Toast.LENGTH_SHORT).show();
////                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
////                                }
//                            }
//
//                        }
//                        else {
//                            alertDialogPopUp(PWD);
//                        }
//
//                    }
                }
                else {
                    alertDialogPopUp(EMAIL + " or " +PWD  + " or " +GENDER);
                }
            }
        });
    }



    public static boolean isValid(String inputString, String type)
    {
        boolean returnValue = false;

        switch (type)
        {
            case "email":
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(inputString);
                returnValue = matcher.find();
                break;

            case "phone number":
                returnValue = VALID_PHONE_NUMBER_REGEX.matcher(inputString).find();
                break;

            case "first name":
            case "last name":
                returnValue = VALID_NAME_REGEX.matcher(inputString).find();
                break;

            case "password":
                returnValue = VALID_PWD_REGEX.matcher(inputString).find();
                break;
        }

        return returnValue;
    }

    public void alertDialogPopUp(String type)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.ic_launcher_round);
        builder.setMessage("Invalid " + type + " value. Please try again");
        builder.setCancelable(true);

        builder.setNegativeButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
