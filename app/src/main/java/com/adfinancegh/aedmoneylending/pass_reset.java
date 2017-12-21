package com.adfinancegh.aedmoneylending;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class pass_reset extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmailReset;
    private Button mResetBtn;
    private TextInputLayout mEmailResetLayout;
    private ProgressDialog mProgressDialog = null;
//TODO RESOLVE EMAIL
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);
        mEmailReset  = (EditText)findViewById(R.id.emailReset);
        mResetBtn = (Button)findViewById(R.id.resetBtn);
        mEmailResetLayout = (TextInputLayout)findViewById(R.id.emailResetLayout);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences(statLogin.PREF_VARS,MODE_PRIVATE);
        prefs.edit().remove(statLogin.KEY).commit();

        //mEmailReset.setText(mEmail);

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new ResetAsyncTask().execute();
                submitForm();
            }
        });

        //KEYBOARD SIGN IN
        mEmailReset.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.resetPass || id == EditorInfo.IME_NULL) {
                    new ResetAsyncTask().execute();
                    return true;
                }
                return false;
            }
        });
    }



    private void submitForm(){
        if (!validateEmail()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(pass_reset.this,"","Please wait...",true);
        mAuth.sendPasswordResetEmail(mEmailReset.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AeD", "Email sent.");
                            mProgressDialog.dismiss();
                            //TODO CREATE A DIALOG MESSAGE TO ALERT USER OF EMAIL AND ALSO TAKE THEM BACK TO THE APP AFTER RESET
                            showVerificationDialog("An email verification has been sent to  your email.Go to your email and click the link to continue.");
                            mResetBtn.setVisibility(View.GONE);

                        }
                        else {
                            Log.d("AeD",task.getException().toString());
                        }
                    }
                });
    }

    private boolean validateEmail() {
        String email = mEmailReset.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mEmailResetLayout.setError(getString(R.string.err_msg_email));
            requestFocus(mEmailReset);
            return false;
        } else {
            mEmailResetLayout.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0,0,Menu.NONE,"Main Menu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this,FirstScreen.class);
        startActivity(i);
        return true;

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showVerificationDialog(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Verification").setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Do something here when "ok" clicked
                Intent i = new Intent(pass_reset.this,statLogin.class);
                startActivity(i);

            }
        });
        alert.show();
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

                case R.id.emailAddr:
                    validateEmail();
                    break;

            }
        }
    }

    private class ResetAsyncTask extends AsyncTask<Void, Void, Void> {
       ProgressDialog mProgressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(pass_reset.this);
            mProgressDialog.setMessage("Please wait...");
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            try{

                submitForm();
                mProgressDialog.show();

            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
            mProgressDialog.dismiss();

        }
    }
}
