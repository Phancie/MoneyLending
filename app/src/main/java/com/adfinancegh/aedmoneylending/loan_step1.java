package com.adfinancegh.aedmoneylending;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class loan_step1 extends AppCompatActivity {
    private Button mNextBtn;
    private EditText mFname,mMname,mLname,mTelNo,mEmailAddr,mPword;
    private TextInputLayout mFnameLayout,mMnameLayout,mLnameLayout,mTelNoLayout,mEmailAddrLayout,
    mPwordLayout;
    final static int PASSWORD_LENGTH = 8;
    final static int TELNO_LENGTH = 13;
    final static int TEL_LOCAL_LENGTH = 10;
    final static String COUNTRY_CODE = "+233";
    final static String LOCAL_CODE = "0";
    public static final String PREF_VARS = "loan_step_1";
    public static final String KEY = "LSO";
    public static final String FNAME = null;
    public static String MAIL_KEY = "mailKey";
    public static final String PREF_MAIL = "email";
    public static String PASS = "";
    DatabaseReference mDatabaseReference,mReference,mRef;
    LocalDbHandler mDbHandler;
    String telno;
    String tempTel;
    String returningCust = null;
    ArrayList<String>existingPhonNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_step1);
        mNextBtn = (Button)findViewById(R.id.nextBtn);
        mFname = (EditText)findViewById(R.id.fName);
        mMname = (EditText)findViewById(R.id.mName);
        mLname = (EditText)findViewById(R.id.lName);
        mEmailAddr = (EditText)findViewById(R.id.emailAddr);
        mPword = (EditText)findViewById(R.id.pWord);
        mTelNo = (EditText)findViewById(R.id.telNo);
        mFnameLayout = (TextInputLayout) findViewById(R.id.fNameLayout);
        mLnameLayout = (TextInputLayout) findViewById(R.id.lNameLayout);
        mMnameLayout = (TextInputLayout) findViewById(R.id.mNameLayout);
        mTelNoLayout = (TextInputLayout) findViewById(R.id.telNoLayout);
        mEmailAddrLayout = (TextInputLayout) findViewById(R.id.emailAddrLayout);
        mPwordLayout = (TextInputLayout) findViewById(R.id.pWordLayout);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDbHandler = new LocalDbHandler(this,null,null,1);
        existingPhonNums = new ArrayList<String>();
        //mDbHandler.getStepOneData();

        //KEYBOARD SIGN IN
        mPword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.loanStep1_form_finished || id == EditorInfo.IME_NULL) {
                    submitForm();
                    return true;
                }
                return false;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        getBundles();
    }

    public void nextBtnClicked(View v){
        submitForm();
    }

    private void submitForm() {
        if (!validateFname()) {
            return;
        }

        if (!validateLname()) {
            return;
        }

        if (!validateTelNo()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        getMiddleName();

        //EXECUTION WOULD END HERE IF IT'S AN UPDATE OF DETAILS
        if(returningCust!=null){
            updateDetails();
            return;
        }

        if (!validatePassword()) {
            return;
        }

        saveLocalData();
        storeCloudData();



        //STORING EMAIL ALONE TO BE USED AS THE SEARCH INDEX IN THE DATABASE
        SharedPreferences prefs = getSharedPreferences(PREF_MAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String email = mEmailAddr.getText().toString().trim();
        editor.putString(MAIL_KEY,email);
        editor.commit();

        new StepOneAsyncTask().execute();

        Intent i = new Intent(this,loan_step2.class);
        startActivity(i);
    }
    private boolean validateFname() {
        if (mFname.getText().toString().trim().isEmpty()) {
            mFnameLayout.setError(getString(R.string.err_msg_fname));
            requestFocus(mFname);
            return false;
        } else {
            mFnameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private String getMiddleName(){
        String mname = mMname.getText().toString();
        if(!mname.isEmpty())
        return mname;
        else
            return null;
    }

    private boolean validateLname() {
        if (mLname.getText().toString().trim().isEmpty()) {
            mLnameLayout.setError(getString(R.string.err_msg_lname));
            requestFocus(mFname);
            return false;
        } else {
            mLnameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTelNo() {
        //DO MANIPULATION ON TELNO TO PRECEDE WITH +233
        if(mTelNo.getText().toString().trim().length()==TEL_LOCAL_LENGTH || mTelNo.getText().toString().trim().length()==TELNO_LENGTH) {
            telno = mTelNo.getText().toString().trim();
            tempTel = null;
            if (telno.startsWith(LOCAL_CODE)) {
                telno = telno.substring(1);
                tempTel = COUNTRY_CODE + telno;
                return true;
            }
            if (telno.startsWith(COUNTRY_CODE)) {
                tempTel = mTelNo.getText().toString();
                return true;
            }

            if (!telno.startsWith(COUNTRY_CODE)) {
                mTelNoLayout.setError(getString(R.string.err_msg_tellength));
                requestFocus(mTelNo);
                return false;
            }

            if (mTelNo.getText().toString().trim().isEmpty()) {
                mTelNoLayout.setError(getString(R.string.err_msg_telno));
                requestFocus(mTelNo);
                return false;
            } else {
                mTelNoLayout.setErrorEnabled(false);

            }
        }
        else {
            mTelNoLayout.setError(getString(R.string.err_msg_tellength));
            requestFocus(mTelNo);
            return false;
        }
         return true;
    }


    private boolean validateEmail() {
        String email = mEmailAddr.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mEmailAddrLayout.setError(getString(R.string.err_msg_email));
            requestFocus(mEmailAddr);
            return false;
        } else {
            mEmailAddrLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mPword.getText().toString().trim().isEmpty()) {
            mPwordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(mPword);
            return false;
        }
        else if(mPword.getText().toString().trim().length()<PASSWORD_LENGTH){
            mPwordLayout.setError(getString(R.string.err_length_msg_password));
            requestFocus(mPword);
            return false;
        }
        else {
            mPwordLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void saveLocalData(){
        //STORING DATA LOCALLY
        SharedPreferences prefs = getSharedPreferences(loan_step1.PREF_VARS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String userTelNo = tempTel;
        String fname = mFname.getText().toString();
        String lname = mLname.getText().toString();
        String mname = mMname.getText().toString();
        String email = mEmailAddr.getText().toString();
        PASS = mPword.getText().toString();

        ArrayList<String>loanStep1Det = new ArrayList<String>();
        loanStep1Det.add(0,fname);
        loanStep1Det.add(1,lname);
        loanStep1Det.add(2,mname);
        loanStep1Det.add(3,userTelNo);
        loanStep1Det.add(4,email);

        Set<String>loanStep1Set = new HashSet<String>();
        loanStep1Set.addAll(loanStep1Det);

        editor.putStringSet(KEY,loanStep1Set);

        editor.commit();

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void storeCloudData(){

       new StepOneAsyncTask().execute();

    }

    public void getBundles(){
        Bundle updateRequest = getIntent().getExtras();
        if(updateRequest==null){
            return;
        }
        returningCust = updateRequest.getString("UpdatingDetails");
        if(returningCust!=null){
            //HIDING PASSWORD FIELD
            mPword.setVisibility(View.GONE);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mDatabaseReference = mDatabaseReference.child(MainActivity.userName).child("User_Details");
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PurposeData pd = dataSnapshot.getValue(PurposeData.class);
                    
                    //ADDING OTHER PHONE NO TO ARRAY LIST
                    existingPhonNums.add(pd.getAlternate_Phone_Number());
                    existingPhonNums.add(pd.getGuarantor_Phone_Number());
                    
                    mFname.setText(pd.getFname());
                    mMname.setText(pd.getMname());
                    mLname.setText(pd.getLname());
                    mTelNo.setText(pd.getPhone());
                    mEmailAddr.setText(pd.getEmail());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void updateDetails(){
        mRef = FirebaseDatabase.getInstance().getReference(MainActivity.userName);
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference = mReference.child(MainActivity.userName).child("User_Details");

        //TODO CHECK FOR REPETITION OF PHONE NUMBER
        if(existingPhonNums.contains(tempTel)){
            mTelNoLayout.setError(getString(R.string.err_msg_tel_similar));
            requestFocus(mTelNo);
            return;
        }
        else {
            mTelNoLayout.setErrorEnabled(false);
        }
        
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                mRef.child("User_Details").child(key).child("fname").setValue(mFname.getText().toString());
                mRef.child("User_Details").child(key).child("mname").setValue(mMname.getText().toString());
                mRef.child("User_Details").child(key).child("lname").setValue(mLname.getText().toString());
                mRef.child("User_Details").child(key).child("phone").setValue(mTelNo.getText().toString());
                mRef.child("User_Details").child(key).child("email").setValue(mEmailAddr.getText().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //UPDATE AUTH EMAIL IN FIREBASE USERS
        //updateEmail(mEmailAddr.getText().toString());
        
        Intent i = new Intent(loan_step1.this,personal_Details.class);
        i.putExtra("UpdatingDetails","updateInfo");
        startActivity(i);
    }

//    public void updateEmail(String email){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d("AeD", "User email address updated.");
//                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                    Log.d("AeD","UID "+currentFirebaseUser);
//                }
//             }
//        });
//    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLocalData();
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
                case R.id.fName:
                    validateFname();
                    break;
                case R.id.lName:
                    validateLname();
                    break;
                case R.id.telNo:
                    validateTelNo();
                    break;
                case R.id.emailAddr:
                    validateEmail();
                    break;
                case R.id.pWord:
                    validatePassword();
                    break;
            }
        }
    }

    private class StepOneAsyncTask extends AsyncTask<Void, Void, Void> {

        String fname = mFname.getText().toString();
        String lname = mLname.getText().toString();
        String mname = mMname.getText().toString();
        String phone = tempTel;
        String email = mEmailAddr.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... args) {
            // do background work here
            try{

                stepOneData sod = new stepOneData();
                sod.setFname(fname);
                sod.setMname(mname);
                sod.setLname(lname);
                sod.setPhone(phone);
                sod.setEmail(email);
                mDbHandler.addStepOneData(sod);
            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);

        }
    }
}
