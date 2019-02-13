package com.mobile.letsbone;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class UserFragment extends Fragment {

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

    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextPwd;
    private EditText editTextCPwd;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private EditText editTextGender;
    private TextView textViewLastUpdated;
    private Button regButton;
    String orgGender ="";
    String orgMonth = "";

    final String formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Edit User Profile");

        editTextFName = (EditText)view.findViewById(R.id.editTextFName);
        editTextLName = (EditText)view.findViewById(R.id.editTextLName);
        editTextPwd = (EditText)view.findViewById(R.id.editTextPwd);
        editTextCPwd = (EditText)view.findViewById(R.id.editTextCPwd);
        editTextPhoneNumber = (EditText)view.findViewById(R.id.editTextPhoneNumber);
        editTextEmail = (EditText)view.findViewById(R.id.editTextEmail);
        editTextGender = (EditText)view.findViewById(R.id.editTextGender);
        textViewLastUpdated = (TextView)view.findViewById(R.id.textViewLastUpdated);

        final TextView textViewLastUpdated = (TextView)view.findViewById(R.id.textViewLastUpdated);


        databaseHelper = new DatabaseHelper(getActivity());

        loadUserRecord();
    }


    public void loadUserRecord()
    {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentUser = sharedPref.getString("currentUser", "-");
        String currentAmount = sharedPref.getString("currentUserAmount", "-");
        orgGender = currentAmount;
        String firstName = "";
        String lastName = "";
        String phoneNumber = "";
        String updatedDate = "";

//        Cursor cursor = databaseHelper.getUserProfileByEmail(currentUser);
//        textViewLastUpdated.setText("Last update on "+ formattedDate);

//        if(cursor.getCount() > 0 ) {
//            while (cursor.moveToNext()) {
//                firstName = cursor.getString(cursor.getColumnIndex("firstName"));
//                lastName = cursor.getString(cursor.getColumnIndex("lastName"));
//                phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
//                updatedDate = cursor.getString(cursor.getColumnIndex("updatedDate"));
//            }

//            editTextFName.setText(firstName);
//            editTextLName.setText(lastName);
//            editTextPhoneNumber.setText(phoneNumber);
//            editTextEmail.setText(currentUser);
//            editTextIncome.setText(currentAmount);
//            textViewLastUpdated.setText("Last update on "+ updatedDate);
//            orgMonth = updatedDate.substring(0,2);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_menu_registration, container, false);

        editTextFName = (EditText)view.findViewById(R.id.editTextFName);
        editTextLName = (EditText)view.findViewById(R.id.editTextLName);
        editTextPwd = (EditText)view.findViewById(R.id.editTextPwd);
        editTextCPwd = (EditText)view.findViewById(R.id.editTextCPwd);
        editTextPhoneNumber = (EditText)view.findViewById(R.id.editTextPhoneNumber);
        editTextEmail = (EditText)view.findViewById(R.id.editTextEmail);
        editTextGender = (EditText)view.findViewById(R.id.editTextGender);
        textViewLastUpdated = (TextView)view.findViewById(R.id.textViewLastUpdated);
        regButton = (Button)view.findViewById(R.id.userRegButton);

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
                if (TextUtils.isEmpty(editTextFName.getText()))
                {
                    editTextFName.setError(getString(R.string.error_field_required));
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

                if(!userEmail.isEmpty() && !userCPwd.isEmpty() && !userGender.isEmpty()) {

                    double userIncomeNumber = 0.0;
//                    userIncomeNumber = Double.parseDouble(userIncome);

//                    Log.d(TAG, userIncomeNumber + ", " + Double.parseDouble(orgIncome) + ", " +
//                            formattedDate.substring(0, 2) + ", " + orgMonth);

                    if (userPwd.equalsIgnoreCase(userCPwd))
                    {
                        if(!isValid(userEmail, EMAIL))
                            alertDialogPopUp(EMAIL);

                        else if(!isValid(userFName, FNAME))
                            alertDialogPopUp( FNAME);

                        else if(!isValid(userFName, LNAME))
                            alertDialogPopUp(LNAME);

                        else if(!isValid(userPwd, PWD))
                            alertDialogPopUp(PWD);

                        else if(!isValid(userPhone, PHONE_NUMBER))
                        {
                            alertDialogPopUp(PHONE_NUMBER);
                        }


                        else
                        {
//                            if (databaseHelper.updateUser(userFName,
//                                    userLName,
//                                    userPwd,
//                                    userPhone,
//                                    userEmail,
//                                    userIncomeNumber,
//                                    formattedDate
//                            )) {
//                                Toast.makeText(getActivity(), "Applied successfully",Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getActivity(), SignInActivity.class));
//                            }
                        }
                    }
                    else {
                        alertDialogPopUp(PWD);
                    }
                }
                else {
                    alertDialogPopUp(EMAIL + " or " +PWD  + " or " );
                }
            }
        });

        return view;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error");
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
