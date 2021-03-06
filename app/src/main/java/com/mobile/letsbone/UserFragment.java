package com.mobile.letsbone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class UserFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = auth.getCurrentUser().getUid();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
    final Map<String, Object> userUpdates = new HashMap<>();

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}");
    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[\\p{L} .'-]+$");
    public static final Pattern VALID_PWD_REGEX =
            Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}");  //(?=.*[@#$%^&+=]) for easy input

    private Spinner spinnerMatchPreference;
    private EditText editTextNewPwd;
    private EditText editTextCNewPwd;
    private EditText editTextDogName;
    private Spinner spinnerDogBreed;
    private Spinner spinnerDogGender;
    private Spinner spinnerDogAge;
    private Button updateButton;

    private ArrayList<String> matchPrefArray = new ArrayList<>(Arrays.asList("Male", "Female"));
    private ArrayList<String> dogBreedArray = new ArrayList<>(Arrays.asList("Labrador Retriever", "German Shepherd", "Poodle", "Chihuahua", "Golden Retriever",
            "Yorkshire Terrier", "Dachshund", "Beagle", "Boxer", "Miniature Schnauzer", "Others"));
    private ArrayList<String> dogGenderArray = new ArrayList<>(Arrays.asList("Male", "Female"));
    private ArrayList<String> dogAgeArray = new ArrayList<>(Arrays.asList("Less than 1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "More than 10"));

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Edit User Profile");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_menu_registration, container, false);

        spinnerMatchPreference = (Spinner)view.findViewById(R.id.spinnerMatchPreference);
        editTextNewPwd = (EditText)view.findViewById(R.id.editTextNewPwd);
        editTextCNewPwd = (EditText)view.findViewById(R.id.editTextCNewPwd);
        editTextDogName = (EditText)view.findViewById(R.id.editTextDogName);
        spinnerDogBreed = (Spinner)view.findViewById((R.id.spinnerDogBreed));
        spinnerDogGender = (Spinner)view.findViewById((R.id.spinnerDogGender));
        spinnerDogAge = (Spinner)view.findViewById(R.id.spinnerDogAge);
        updateButton = (Button)view.findViewById(R.id.userUpdateButton);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String currentUser = sharedPref.getString("currentUser", "null");

        //For loading the values currently in Firebase for the current user to the spinners and edittexts
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile users = null;

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    users = child.getValue(UserProfile.class);
                    userID = child.getKey();

                    if(users.getEmailAddress().equalsIgnoreCase(currentUser)) {
                        String matchPrefVal = users.getLookingFor();
                        String dogNameVal = users.getDogName();
                        String dogBreedVal = users.getDogBreed();
                        String dogGenderVal = users.getDogGender();
                        String dogAgeVal = users.getDogAge();

                        for(int i = 0; i < matchPrefArray.size(); i++) {
                            if(matchPrefVal.equals(matchPrefArray.get(i))) {
                                spinnerMatchPreference.setSelection(i);
                            }
                        }

                        editTextDogName.setText(dogNameVal);

                        for(int i = 0; i < dogBreedArray.size(); i++) {
                            if(dogBreedVal.equals(dogBreedArray.get(i))) {
                                spinnerDogBreed.setSelection(i);
                            }
                        }

                        for(int i = 0; i < dogGenderArray.size(); i++) {
                            if(dogGenderVal.equals(dogGenderArray.get(i))) {
                                spinnerDogGender.setSelection(i);
                            }
                        }

                        for(int i = 0; i < dogAgeArray.size(); i++) {
                            if(dogAgeVal.equals(dogAgeArray.get(i))) {
                                spinnerDogAge.setSelection(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!TextUtils.isEmpty(editTextNewPwd.getText())) && (!TextUtils.isEmpty(editTextCNewPwd.getText()))) {
                    if(!editTextNewPwd.getText().toString().equals(editTextCNewPwd.getText().toString())) {
                        alertDialogPopup("Please make sure that the New Password and Confirm New Password are the same.");
                    } else {
                        user.updatePassword(editTextNewPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    alertDialogPopup("There was a problem with the password change. Please try again.");
                                } else {
                                    alertDialogPopup("Your password has been changed.");
                                }
                            }
                        });
                    }
                }
                
                userUpdates.put("DogName", editTextDogName.getText().toString());
                userUpdates.put("LookingFor", spinnerMatchPreference.getSelectedItem().toString());
                userUpdates.put("DogBreed", spinnerDogBreed.getSelectedItem().toString());
                userUpdates.put("DogGender", spinnerDogGender.getSelectedItem().toString());
                userUpdates.put("DogAge", spinnerDogAge.getSelectedItem().toString());
                databaseReference2.updateChildren(userUpdates);
                //alertDialogPopup("Your profile has been updated.");
            }
        });

//         final String dogName = editTextDogName.getText().toString();

//         editTextDogName.addTextChangedListener(new TextWatcher() {


//             @Override
//             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//             }

//             @Override
//             public void onTextChanged(CharSequence s, int start, int before, int count) {
//             }

//             @Override
//             public void afterTextChanged(Editable s) {
//                 if(!dogName.equals(String.valueOf(s))) {
//                     editTextDogName.setSelection(editTextDogName.length());
//                     userUpdates.put("DogName", editTextDogName.getText().toString());
//                     databaseReference2.updateChildren(userUpdates);
//                 }
//             }
//         });
        return view;
    }

    private void alertDialogPopup(String alertText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage(alertText);
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
