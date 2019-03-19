package com.mobile.letsbone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    DatabaseHelper databaseHelper;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    String formattedDate;
    private FirebaseAuth auth;

    EditText editTextEmailSignIn;
    EditText editTextPwdSignIn;
    TextView textViewSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        auth = FirebaseAuth.getInstance();

        // to quit command
        if(getIntent().getBooleanExtra("Exit", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        Button buttonSignIn = (Button)findViewById(R.id.buttonSignIn);

        editTextEmailSignIn = (EditText)findViewById(R.id.editTextEmailSignIn);
        editTextPwdSignIn = (EditText)findViewById(R.id.editTextPwdSignIn);
        textViewSignIn = (TextView)findViewById(R.id.textViewSignIn);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        databaseHelper = new DatabaseHelper(this);

//        editTextEmailSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                if (TextUtils.isEmpty(editTextEmailSignIn.getText()))
//                {
//                    editTextEmailSignIn.setError(getString(R.string.error_field_required));
//                }
//            }
//        });
//
//        editTextPwdSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                if (TextUtils.isEmpty(editTextPwdSignIn.getText()))
//                {
//                    editTextPwdSignIn.setError(getString(R.string.error_field_required));
//                }
//            }
//        });


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Auth. Form
                submitForm();

//                if(!userEmail.isEmpty() && !editTextPwdSignIn.getText().toString().isEmpty())
//                {
//
//                    if()
//                    {
//                    }
//                    else
//                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
//                        builder.setMessage("Invalid input. Do you want to create an account?");
//                        builder.setCancelable(true);
//                        builder.setTitle("Error");
//
//                        builder.setPositiveButton(
//                                "Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
//                                        startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
//                                    }
//                                });
//
//                        builder.setNegativeButton(
//                                "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                        AlertDialog alert11 = builder.create();
//                        alert11.show();
//                    }
//
//                }
//                else
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
//                    builder.setMessage("Invalid input. Do you want to create an account?");
//                    builder.setCancelable(true);
//                    builder.setTitle("Error");
//
//                    builder.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
//                                }
//                            });
//
//                    builder.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
            }
        });

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserRegistrationActivity.class));
            }
        });

    }

    /**
     * Validating form
     */
    private void submitForm() {
        final String email = editTextEmailSignIn.getText().toString().trim();
        String password = editTextPwdSignIn.getText().toString().trim();

        if(email.isEmpty()) {
            return;
        }
        if(password.isEmpty()) {
            return;
        }
//        editTextEmailSignIn.setErrorEnabled(false);
//        editTextPwdSignIn.setErrorEnabled(false);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                        } else {
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("currentUser", email);
                            editor.putString("currentUserName", "-");
                            editor.putString("currentUserExtra", "-");
                            editor.commit();

                            new CustomProgressWheel().execute();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                            // noti service
                            startSimpleService();
                        }
                    }
                });
    }

//    private boolean checkEmail() {
//        String email = loginInputEmail.getText().toString().trim();
//        if (email.isEmpty() || !isEmailValid(email)) {
//
//            loginInputLayoutEmail.setErrorEnabled(true);
//            loginInputLayoutEmail.setError(getString(R.string.err_msg_email));
//            loginInputEmail.setError(getString(R.string.err_msg_required));
//            requestFocus(loginInputEmail);
//            return false;
//        }
//        loginInputLayoutEmail.setErrorEnabled(false);
//        return true;
//    }
//
//    private boolean checkPassword() {
//
//        String password = loginInputPassword.getText().toString().trim();
//        if (password.isEmpty() || !isPasswordValid(password)) {
//
//            loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
//            loginInputPassword.setError(getString(R.string.err_msg_required));
//            requestFocus(loginInputPassword);
//            return false;
//        }
//        loginInputLayoutPassword.setErrorEnabled(false);
//        return true;
//    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        progressBar.setVisibility(View.GONE);
//    }




    // AsyncTask
    private class CustomProgressWheel extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void value)
        {
            super.onPostExecute(value);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... values)
        {
            try
            {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void startSimpleService() {
        Intent intent = new Intent(this, NotificationService.class);
        Log.w("NOTI", "----------------NotificationService---------------------");
        startService(intent);  // from super
    }
}
