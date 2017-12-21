package com.adfinancegh.aedmoneylending;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.adfinancegh.aedmoneylending.R.string.personalDetails;
import static com.adfinancegh.aedmoneylending.loan_step1.MAIL_KEY;
import static com.adfinancegh.aedmoneylending.loan_step1.PREF_MAIL;


public class statLogin extends AppCompatActivity {
    private TextInputLayout mStatEmailLayout,mStatPasswordLayout;
    private EditText mStatPassword;
    private AutoCompleteTextView  mStatEmail;
    private FirebaseAuth mAuth;
    private Button mSignInBtn;
    static String KEY = "EMAIL";
    static String PREF_VARS = "loginSession";
    private String email,password;
    private ProgressDialog mProgressDialog = null;
    private DatabaseReference mDatabaseReference;
    String userName;
    String db_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_login);
        mStatEmailLayout = (TextInputLayout)findViewById(R.id.stat_email_layout);
        mStatPasswordLayout = (TextInputLayout) findViewById(R.id.stat_password_layout);
        mStatEmail = (AutoCompleteTextView)findViewById(R.id.stat_email);
        mStatPassword = (EditText)findViewById(R.id.stat_password);
        mSignInBtn = (Button) findViewById(R.id.login_sign_in_button);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("AeD","db_key "+MainActivity.db_key);

        //KEYBOARD SIGN IN
        mStatPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signIn_form_finished || id == EditorInfo.IME_NULL) {
                    mProgressDialog = ProgressDialog.show(statLogin.this,"","Please wait...",true);
                    LoginAsyncTask task = new LoginAsyncTask();
                    task.execute();
                    return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences =  getSharedPreferences(PREF_MAIL, MODE_PRIVATE);
        userName = preferences.getString(MAIL_KEY, null);
        Log.d("AeD","Username "+userName);

    }

    public void resetPassClicked(View v){
        Intent i = new Intent(this,pass_reset.class);
        startActivity(i);
    }

    public void signInExistingUser(View v){
        mProgressDialog = ProgressDialog.show(this,"","Please wait...",true);
        LoginAsyncTask task = new LoginAsyncTask();
        task.execute();


    }

    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("AeD","sign in onComplete "+ task.isSuccessful());

                if(!task.isSuccessful()){
                    Log.d("AeD","sign in failed "+task.getException());
                    mProgressDialog.dismiss();
                    showErrorDialog("Login failed please check your email/password.");
                    return;

                }
                else{
                    storeSession();
                    Intent i = new Intent(statLogin.this,MainActivity.class);
                    startActivity(i);
                    mProgressDialog.dismiss();


                }
            }
        });


    }



    private boolean validateEmail() {
        email = mStatEmail.getText().toString().trim();

        if (email.isEmpty()) {
            mStatEmailLayout.setError(getString(R.string.err_msg_email));
            requestFocus(mStatEmail);
            return false;
        } else {
            mStatEmailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
       password = mStatPassword.getText().toString().trim();
        if (password.isEmpty()) {
            mStatPasswordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(mStatPassword);
            return false;
        }

          else {
            mStatPasswordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void storeSession(){
        //UPDATING THE EMAIL SHARED PREF FOR DATABASE REFERENCE
        SharedPreferences prefs = getSharedPreferences(loan_step1.PREF_MAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(loan_step1.MAIL_KEY,email);
        editor.commit();
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this).setTitle("Ooops").setMessage(message).setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        storeSession();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.stat_email:
                    validateEmail();
                    break;
                case R.id.stat_password:
                    validatePassword();
                    break;
            }

        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... args) {
            // do background work here
            try{
                submitForm();
            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);


        }
    }
}
