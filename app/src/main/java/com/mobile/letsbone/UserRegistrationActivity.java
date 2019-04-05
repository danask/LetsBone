package com.mobile.letsbone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mobile.letsbone.Entities.ProfileData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseHelper databaseHelper;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
    private Uri ImageUri;
    private static final int IMAGE_REQUEST = 1;
    int userAge;

    EditText editTextFName;
    EditText editTextLName;
    EditText editTextPwd;
    EditText editTextCPwd;
    EditText editTextPhoneNumber;
    EditText editTextEmail;
    //EditText editTextGender;
    Spinner spinnerGender;
    Spinner spinnerMatchPreference;
    EditText dogName;
    Spinner spinnerDogBreed;
    Spinner spinnerDogGender;
    Spinner spinnerDogAge;
    Button btnBDatePicker;
    EditText editTextDogBreed;
    Button uploadImageButton;

    ProfileData profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editTextFName = (EditText)findViewById(R.id.editTextFName);
        editTextLName = (EditText)findViewById(R.id.editTextLName);
        editTextPwd = (EditText)findViewById(R.id.editTextPwd);
        editTextCPwd = (EditText)findViewById(R.id.editTextCPwd);
        editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        btnBDatePicker = (Button)findViewById(R.id.btnBDatePicker);
        //editTextGender = (EditText)findViewById(R.id.editTextGender);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        spinnerMatchPreference = (Spinner)findViewById(R.id.spinnerMatchPreference);
        dogName = (EditText)findViewById(R.id.editTextDogName);
        spinnerDogBreed = (Spinner)findViewById(R.id.spinnerDogBreed);
        spinnerDogGender = (Spinner)findViewById(R.id.spinnerDogGender);
        spinnerDogAge = (Spinner)findViewById(R.id.spinnerDogAge);
        //editTextDogBreed = (EditText)findViewById(R.id.editTextDogBreed);
        uploadImageButton = findViewById(R.id.btnImage);

        final TextView textViewLastUpdated = (TextView)findViewById(R.id.textViewLastUpdated);
        Button regButton = (Button)findViewById(R.id.userRegButton);
        databaseHelper = new DatabaseHelper(this);
        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("images/" + System.currentTimeMillis());

        firebaseDatabase.getReference("app_title").setValue("Let's Bone");

        firebaseDatabase.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
                Toast.makeText(getApplicationContext(), "App title updated.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        //Image upload button
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageStorage();
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
                String userGender = spinnerGender.getSelectedItem().toString();
                String lookingFor = spinnerMatchPreference.getSelectedItem().toString();
                String dogBreed = spinnerDogBreed.getSelectedItem().toString();
                String dogGender = spinnerDogGender.getSelectedItem().toString();
                String dogAge = spinnerDogAge.getSelectedItem().toString();

                if(!userEmail.isEmpty() && !userPwd.isEmpty() && !userCPwd.isEmpty())
                {

                    submitForm();
                }
                else {
                    alertDialogPopUp(EMAIL + " or " +PWD  + " or " +GENDER);
                }
            }
        });
    }

    private void openImageStorage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            ImageUri = data.getData();

        }
    }

    //method to get extension from file
    private String getFileExtesnion(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFiletoFirebaseStorage(){
        if(ImageUri != null){
            storageReference.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String imageUrl = downloadUri.toString();

                        String userID = auth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                        Map userInfo = new HashMap<>();
                        userInfo.put("ImageUrl", imageUrl);
                        databaseReference.updateChildren(userInfo);
                    }
                }
            });


        }else{
            String userID = auth.getCurrentUser().getUid();
            databaseReference = firebaseDatabase.getInstance().getReference().child("Users").child(userID);
            Map userInfo = new HashMap<>();
            userInfo.put("ImageUrl", "default");
            databaseReference.updateChildren(userInfo);
        }
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

                            String userID = auth.getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            Map userInfo = new HashMap<>();
                            userInfo.put("FirstName", editTextFName.getText().toString());
                            userInfo.put("LastName", editTextLName.getText().toString());
                            userInfo.put("EmailAddress", editTextEmail.getText().toString());
                            userInfo.put("Gender", spinnerGender.getSelectedItem().toString());
                            userInfo.put("LookingFor", spinnerMatchPreference.getSelectedItem().toString());
                            userInfo.put("Age", userAge);
                            userInfo.put("DogName", dogName.getText().toString());
                            userInfo.put("DogBreed", spinnerDogBreed.getSelectedItem().toString());
                            userInfo.put("DogGender", spinnerDogGender.getSelectedItem().toString());
                            userInfo.put("DogAge", spinnerDogAge.getSelectedItem().toString());
                            databaseReference.updateChildren(userInfo);

                            uploadFiletoFirebaseStorage();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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