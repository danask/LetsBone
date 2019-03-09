package com.mobile.letsbone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
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
    int userAge;

    EditText editTextFName;
    EditText editTextLName;
    EditText editTextPwd;
    EditText editTextCPwd;
    EditText editTextPhoneNumber;
    EditText editTextEmail;
    //EditText editTextGender;
    Spinner spinnerGender;
    Spinner spinnerDogBreed;
    Spinner spinnerDogGender;
    Spinner spinnerDogAge;
    Button btnBDatePicker;
    EditText editTextDogBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2476a6")));

        editTextFName = (EditText)findViewById(R.id.editTextFName);
        editTextLName = (EditText)findViewById(R.id.editTextLName);
        editTextPwd = (EditText)findViewById(R.id.editTextPwd);
        editTextCPwd = (EditText)findViewById(R.id.editTextCPwd);
        editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        btnBDatePicker = (Button)findViewById(R.id.btnBDatePicker);
        //editTextGender = (EditText)findViewById(R.id.editTextGender);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        spinnerDogBreed = (Spinner)findViewById(R.id.spinnerDogBreed);
        spinnerDogGender = (Spinner)findViewById(R.id.spinnerDogGender);
        spinnerDogAge = (Spinner)findViewById(R.id.spinnerDogAge);
        //editTextDogBreed = (EditText)findViewById(R.id.editTextDogBreed);

        final TextView textViewLastUpdated = (TextView)findViewById(R.id.textViewLastUpdated);
        Button regButton = (Button)findViewById(R.id.userRegButton);
        databaseHelper = new DatabaseHelper(this);
        auth = FirebaseAuth.getInstance();

        // initialize
        final String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        textViewLastUpdated.setText("Created on "+ formattedDate);
        //editTextGender.setHint("M, F, or H");

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

        /*editTextPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextPhoneNumber.getText()))
                {
                    editTextPhoneNumber.setError(getString(R.string.error_field_required));
                }
            }
        });*/

        btnBDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int bDateYear = calendar.get(Calendar.YEAR);
                int bDateMonth = calendar.get(Calendar.MONTH);
                int bDateDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), datePickerListener,
                        bDateYear, bDateMonth, bDateDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
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

        /*editTextGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        });*/

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = editTextEmail.getText().toString();
                String userFName = editTextFName.getText().toString();
                String userLName = editTextLName.getText().toString();
                String userPwd = editTextPwd.getText().toString();
                String userCPwd = editTextCPwd.getText().toString();
                String userPhone = editTextPhoneNumber.getText().toString();
                String userGender = spinnerGender.getSelectedItem().toString();
                String dogBreed = spinnerDogBreed.getSelectedItem().toString();
                String dogGender = spinnerDogGender.getSelectedItem().toString();
                String dogAge = spinnerDogAge.getSelectedItem().toString();

                if(!userEmail.isEmpty() && !userPwd.isEmpty() && !userCPwd.isEmpty())
                {
                    submitForm();
//                    Cursor cursorUser = databaseHelper.getUserProfileByEmail(userEmail);
//
//                    if (cursorUser.getCount() > 0)
//                    {
//                        alertDialogPopUp("That e-mail address already exists. Please try again.");
//                    }
//                    else {
//
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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String dateFormat = new SimpleDateFormat("MMM dd YYYY").format(calendar.getTime());
            Toast.makeText(getApplicationContext(), "Age: " + Integer.toString(calcAge(calendar.getTimeInMillis())), Toast.LENGTH_SHORT).show();
        }
    };

    int calcAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar dateToday = Calendar.getInstance();
        userAge = dateToday.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(dateToday.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            userAge--;
        }

        return userAge;
    }




/**
 * Validating form
 */
    private void submitForm()
    {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPwd.getText().toString().trim();

        if(email.isEmpty()) {
            return;
        }
        if(password.isEmpty()) {
            return;
        }
//        editTextEmailSignIn.setErrorEnabled(false);
//        editTextPwdSignIn.setErrorEnabled(false);

        //authenticate user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(UserRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(UserRegistrationActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                        }
                        else {
                            // TODO: Go to login again or direct to home
//                            SharedPreferences.Editor editor = sharedPref.edit();
//
//                            editor.putString("currentUser", email);
//                            editor.putString("currentUserName", "-");
//                            editor.putString("currentUserExtra", "-");
//                            editor.commit();


                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            finish();

                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();

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
//        builder.setIcon(R.drawable.ic_launcher_round);
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
