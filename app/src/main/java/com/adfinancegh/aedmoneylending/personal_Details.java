package com.adfinancegh.aedmoneylending;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class personal_Details extends AppCompatActivity {
    private Spinner genderSpinner;
    private String gender;
    private String idType;
    private String mStatus;
    private String workHrs;
    private Spinner idSpinner;
    private Spinner maritalStatus;
    private Spinner workHours;
    private Button dobBtn;
    private TextView dobDisp;
    private Button mNextBtn;
    private TextInputLayout mAltTelNoLayout,mLocationLayout,mPostAddrLayout,mLengthOfResLayout,
    mCompNameLayout,mJobTitleLayout,mSalaryLayout,dateDispLayout,mIdNoLayout;
    private EditText mAltTelNo,mLocation,mPostAddr,mCompName,mJobTitle,mSalary,mLenthOfRes,mIdNo;
    private int month,year,day;
    final static int MIN_LENGTH_OF_RES = 1;
    final static int TELNO_LENGTH = 13;
    final static int TEL_LOCAL_LENGTH = 10;
    final static String COUNTRY_CODE = "+233";
    final static String LOCAL_CODE = "0";
    public static final String PREF_VARS = "Personal_Details";
    public static final String GENDER = null;
    public static final String MARITAL_STAT= null;
    public static int DOB_YEAR = 0;
    public static final String ID = null;
    public static final String KEY = "PD";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatePickerDialog dialog;
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    DatabaseReference mDatabaseReference,mRef,mReference;
    List<String> sample;
    Set<String> set;
    double loanVal = 0.0;
    final double LOAN_PERCENTAGE = 0.4;
    LocalDbHandler mDbHandler;
    Calendar calendar;


    String PREV_TEL_NO;
    String poAddr;
    String telno ;
    String tempTel,pPhone;
    String customerStat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal__details);
        genderSpinner = (Spinner)findViewById(R.id.gender_Spinner);
        idSpinner = (Spinner)findViewById(R.id.id_Spinner);
        maritalStatus = (Spinner)findViewById(R.id.maritalStat_Spinner);
        workHours = (Spinner)findViewById(R.id.workPeriod_Spinner);
        mNextBtn = (Button)findViewById(R.id.nextBtn);
        mAltTelNoLayout = (TextInputLayout)findViewById(R.id.altTelNoLayout);
        mLocationLayout = (TextInputLayout)findViewById(R.id.locationLayout);
        mPostAddrLayout = (TextInputLayout)findViewById(R.id.postAddrLayout);
        mLocationLayout = (TextInputLayout)findViewById(R.id.locationLayout);
        mCompNameLayout = (TextInputLayout) findViewById(R.id.compNameLayout);
        mLengthOfResLayout = (TextInputLayout)findViewById(R.id.lengthOfResLayout);
        mJobTitleLayout = (TextInputLayout)findViewById(R.id.jobTitleLayout);
        mSalaryLayout = (TextInputLayout)findViewById(R.id.salaryLayout);
        dateDispLayout = (TextInputLayout)findViewById(R.id.dateDispLayout);
        mIdNoLayout = (TextInputLayout)findViewById(R.id.idNoLayout);
        mAltTelNo = (EditText)findViewById(R.id.altTelNo);
        mLocation = (EditText)findViewById(R.id.location);
        mPostAddr = (EditText) findViewById(R.id.postAddr);
        mCompName = (EditText)findViewById(R.id.compName);
        mJobTitle = (EditText)findViewById(R.id.jobTitle);
        mSalary = (EditText)findViewById(R.id.salary);
        mIdNo = (EditText)findViewById(R.id.idNo);
        mLenthOfRes = (EditText)findViewById(R.id.lengthOfRes);
        dobDisp = (TextView)findViewById(R.id.dateDisp);
        mDbHandler = new LocalDbHandler(this,null,null,1);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        //KEYBOARD SIGN IN
        mSalary.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.proceed || id == EditorInfo.IME_NULL) {
                    submitForm();
                    return true;
                }
                return false;
            }
        });

//TODO SOME FIREBASE DATA ARE NOT RETRIEVING WHILE OTHERS ARE

        //PASSING THE STRING ARRAY IN THE RES TO SPINNER IN XML
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply Gender array adapter to the spinner
        genderSpinner.setAdapter(adapter);
        //PASSING ID ADAPTER
        adapter = ArrayAdapter.createFromResource(this,R.array.id,R.layout.spinner_dropdown_item);

        //APPLY ID ARRAY TO SPINNER
        idSpinner.setAdapter(adapter);

        //PASSING MARITAL STATUS ADAPTER
        adapter = ArrayAdapter.createFromResource(this,R.array.maritalStatus,R.layout.spinner_dropdown_item);

        //APPLY MARITAL STATUS TO SPINNER
        maritalStatus.setAdapter(adapter);

        //PASSING WORKING TIME ADAPTER
        adapter = ArrayAdapter.createFromResource(this,R.array.workingTime,R.layout.spinner_dropdown_item);

        // Apply WORKING TIME array adapter to the spinner
        workHours.setAdapter(adapter);

        //CHOSEN SPINNER VAL
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 gender = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //CHOSEN SPINNER VAL
        idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idType = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //CHOSEN SPINNER VAL
        maritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStatus = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //TODO WORK ON SETTING THE MINIMUM AGE TO DISPLAY ON THE DATE DIALOG
        //SETTING DATE DIALOG TO MIN DATE
        calendar  = Calendar.getInstance();
        calendar.add(Calendar.YEAR , -18);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dobDisp.setText(year+"/"+month+"/"+day);

        //CHOSEN SPINNER VAL
        workHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workHrs = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showDateDialog();


    }


    @Override
    protected void onStart() {
        super.onStart();
        //HANDLING DATA RECEIVED FROM STEP 2 INTENT AND PREF VARS
        getBundles();
    }

    public void showDateDialog(){
        dobBtn = (Button) findViewById(R.id.dateBtn);
        dobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DatePickerDialog(personal_Details.this,android.R.style.Theme_Holo_Dialog,mDateSetListener
                        ,year,month,day);
                //GETTING DOB YEAR HERE IF USER DOESN'T TRIGGERS DATE LISTENER EVENT
                DOB_YEAR = year;
                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month+=1;
                dobDisp.setText(year + "/" + month + "/" + day);
                //GETTING DOB YEAR HERE IF USER TRIGGERS THIS EVENT
                DOB_YEAR = year;
                Log.d("AeD","DOB Yr is "+DOB_YEAR);
            }
        };
    }

    public void nextBtnClicked(View v){
        submitForm();
    }

    private void submitForm() {
        if (!validateAltTelNo()){
            return;
        }

        if (!validateIdNo()){
            return;
        }

        if (!validateLocation()) {
            return;
        }

        if (!validateLengthOfRes()) {
            return;
        }

        if (!validateCompName()) {
            return;
        }

        if (!validateJobTitle()) {
            return;
        }

        if (!validateSalary()) {
            return;
        }

        if(sample.contains(tempTel)){
            mAltTelNoLayout.setError(getString(R.string.err_msg_tel_similar));
            requestFocus(mAltTelNo);
            Toast.makeText(this,R.string.err_msg_tel_similar,Toast.LENGTH_SHORT).show();

            return ;
        }
        else {
            mAltTelNoLayout.setErrorEnabled(false);
        }

        getPostaddr(poAddr);

        storeLocalData();
        storeCloudData();

        //UPDATING USER INFO WHEN BTN IS CLICKED
        if (customerStat!=null) {
            updateCloudFields();
            return;
        }

        SharedPreferences preferences = getSharedPreferences(PREF_VARS,MODE_PRIVATE);
        Set<String> hset = preferences.getStringSet(KEY,null);
        ArrayList<String> alist = new ArrayList<String>(hset);


        Intent i = new Intent(this,com.adfinancegh.aedmoneylending.loan_purpose.class);
        startActivity(i);
    }

    private void storeLocalData(){
        //STORING DATA LOCALLY
        SharedPreferences prefs = getSharedPreferences(PREF_VARS,0);
        SharedPreferences.Editor editor = prefs.edit();

        String location = mLocation.getText().toString();
        String salary = mSalary.getText().toString();
        String altNo = tempTel;
        String postAddr = mPostAddr.getText().toString();
        String lengthOfRes = mLenthOfRes.getText().toString();
        String compName = mCompName.getText().toString();
        String jobTitle = mJobTitle.getText().toString();
        String dob = dobDisp.getText().toString();
        String id = idType;
        String wkHrs = workHrs;
        String gend = gender;
        String mariStat = MARITAL_STAT;
        String idnumber = mIdNo.getText().toString();

        ArrayList<String>personalDetails = new ArrayList<String>();
        personalDetails.add(0,altNo);
        personalDetails.add(1,gend);
        personalDetails.add(2,dob);
        personalDetails.add(3,id);
        personalDetails.add(4,idnumber);
        personalDetails.add(5,location);
        personalDetails.add(6,postAddr);
        personalDetails.add(7,lengthOfRes);
        personalDetails.add(8,compName);
        personalDetails.add(9,jobTitle);
        personalDetails.add(10,wkHrs);
        personalDetails.add(11,mariStat);
        personalDetails.add(12,salary);


        Set<String>personalDetailsSet = new HashSet<String>();
        personalDetailsSet.addAll(personalDetails);


       // editor.put(KEY,data);

        editor.putStringSet(KEY,personalDetailsSet);


        editor.commit();

        mSalary.addTextChangedListener(new MyTextWatcher(mSalary));
    }

    private boolean validateLocation() {
        if (mLocation.getText().toString().trim().isEmpty()) {
            mLocationLayout.setError(getString(R.string.err_msg_location));
            requestFocus(mLocation);
            return false;
        } else {
            mLocationLayout.setErrorEnabled(false);
        }

        return true;
    }



    private boolean validateSalary() {
        if (mSalary.getText().toString().trim().isEmpty()) {
            mSalaryLayout.setError(getString(R.string.err_msg_salary));
            requestFocus(mSalary);
            return false;
        }
        double salary = Double.parseDouble(mSalary.getText().toString());
        double amtRemain = salary*LOAN_PERCENTAGE;
        Log.d("AeD","40% of Salary is "+amtRemain );
        Log.d("AeD","Loan amt is "+loanVal);
        if(amtRemain<loanVal){
            mSalaryLayout.setError(getString(R.string.err_salary));
            requestFocus(mSalary);
            return false;
        } else {
            mSalaryLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAltTelNo() {

            //DO MANIPULATION ON TELNO TO PRECEDE WITH +233
            telno = mAltTelNo.getText().toString().trim();
            tempTel = null;

            if (!telno.isEmpty()) {


                    if (mAltTelNo.getText().toString().trim().length() == TEL_LOCAL_LENGTH || mAltTelNo.getText().toString().trim().length() == TELNO_LENGTH){
                    if (telno.startsWith(LOCAL_CODE)) {
                        telno = telno.substring(1);
                        tempTel = COUNTRY_CODE + telno;
                        return true;
                    }
                if (telno.startsWith(COUNTRY_CODE)) {
                    tempTel = mAltTelNo.getText().toString();
                    return true;
                }

                if (!telno.startsWith(COUNTRY_CODE)) {
                    mAltTelNoLayout.setError(getString(R.string.err_msg_tellength));
                    requestFocus(mAltTelNo);
                    return false;
                } else {
                    mAltTelNoLayout.setErrorEnabled(false);
                }

             }else {
                    mAltTelNoLayout.setError(getString(R.string.err_msg_tellength));
                    requestFocus(mAltTelNo);
                    return false;
                }

            }


        return true;
    }


    private boolean validateLengthOfRes() {
       if(mLenthOfRes.getText().toString().trim().isEmpty()){
            mLengthOfResLayout.setError(getString(R.string.err_length_Of_Res));
            requestFocus(mLenthOfRes);
            return false;
        }
            //TODO THIS METHOD WOULD USED TO MAKE SURE LENGTH OF RES IS NOT MORE THAN AGE
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            int diff=0;
            diff = year - DOB_YEAR;
            Log.d("AeD","Current yr "+year);
            Log.d("AeD","diff in yrs "+diff);
            int resLength = Integer.parseInt(mLenthOfRes.getText().toString().trim());
            Log.d("AeD","resLength "+resLength);

            if(diff<resLength || resLength<MIN_LENGTH_OF_RES){
                mLengthOfResLayout.setError(getString(R.string.err_invalid_length_Of_Res));
                requestFocus(mLenthOfRes);
                return false;
            }
        else {
            mLengthOfResLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCompName() {
        if(mCompName.getText().toString().trim().isEmpty()){
            mCompNameLayout.setError(getString(R.string.err_compName_msg));
            requestFocus(mCompName);
            return false;
        }
        else {
            mCompNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateJobTitle() {
        if(mJobTitle.getText().toString().trim().isEmpty()){
            mJobTitleLayout.setError(getString(R.string.err_job_msg));
            requestFocus(mJobTitle);
            return false;
        }
        else {
            mJobTitleLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateIdNo() {
        if(mIdNo.getText().toString().trim().isEmpty()){
            mIdNoLayout.setError(getString(R.string.err_id_no_msg));
            requestFocus(mIdNo);
            return false;
        }
        else {
            mIdNoLayout.setErrorEnabled(false);
        }

        return true;
    }


    public String getPostaddr(String postAddr){
        postAddr = mPostAddr.getText().toString().trim();
        if(!postAddr.isEmpty())
        return postAddr;
        else return null;
    }



    private void createFirebaseUser(){
        List<stepOneData>datalist =  mDbHandler.getStepOneData();
        String email="";
        String pword = loan_step1.PASS;
        for (int i = 0; i < datalist.size(); i++) {
            stepOneData sod = datalist.get(i);
            email = sod.getEmail();
        }
        Log.d("AeD","Email is "+email);
        Log.d("AeD","Password is "+pword);
        mAuth.createUserWithEmailAndPassword(email,pword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("AeD","create user onComplete "+ task.isSuccessful());

                if(!task.isSuccessful()){
                    Log.d("AeD","User account failed "+task.getException());
                    return;

                }
                else{
                    Log.d("AeD","User account created");
                }
            }
        });


    }



    private void storeCloudData(){

        if(customerStat==null) {
            new DetailsAsyncTask().execute();
        }

    }

    public void getBundles(){
        Bundle loanData = getIntent().getExtras();
        if(loanData==null){
            return;
        }
        else {
                if(loanData.getString("loanAmount")!=null){
                    loanVal = Double.parseDouble(loanData.getString("loanAmount"));
                }
            customerStat = loanData.getString("UpdatingDetails");
            Log.d("AeD","Personal Bundle "+customerStat);
        }

        if(customerStat!=null){
            //EXISTING USER UPDATING DETAILS


            ChildEventListener mListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("AeD","DataSnapshot "+dataSnapshot);
                    PurposeData pd = dataSnapshot.getValue(PurposeData.class);
                    //STORING ALL PHONE NOs IN ARRAYLIST
                    sample=new ArrayList<String>();
                    sample.add(pd.getPhone());
                    sample.add(pd.getGuarantor_Phone_Number());
                    //SETTING ALL FIELDS TO EXISTING DATA
                    Log.d("AeD","Company name "+pd.getCompany_Name());
                    mAltTelNo.setText(pd.getAlternate_Phone_Number());
                    //TODO SET GENDER SPINNER HERE FOLLOWED BY DOB THEN ID TYPE
                    //SETTING GENDER FROM FB
                    if(pd.getGender().equals("Female")){
                        genderSpinner.setSelection(1);
                    }
                    if(pd.getId_Type().equals("Passport")){
                        idSpinner.setSelection(1);
                    }
                    if(pd.getId_Type().equals("NHIS")){
                        idSpinner.setSelection(2);
                    }
                    if(pd.getId_Type().equals("NIA")){
                        idSpinner.setSelection(3);
                    }
                    dobDisp.setText(pd.getDate_Of_Birth());
                    mIdNo.setText(pd.getId_Number());
                    mLocation.setText(pd.getLocation());
                    mPostAddr.setText(pd.getPost_Address());
                    mLenthOfRes.setText(pd.getLength_of_Res());
                    mCompName.setText(pd.getCompany_Name());
                    mJobTitle.setText(pd.getJobTitle());
                    //TODO SET WORK PERIOD SPINNER  & MARITAL STAT HERE
                    if(pd.getMari_stat().equals("Married")){
                        maritalStatus.setSelection(1);
                    }
                    if(pd.getMari_stat().equals("Divorced")){
                        maritalStatus.setSelection(2);
                    }
                    if(pd.getMari_stat().equals("Widow/Widower")){
                        maritalStatus.setSelection(3);
                    }
                    if(pd.getWrkPeriod().equals("Part time")){
                        workHours.setSelection(1);
                    }
                    if(pd.getWrkPeriod().equals("Contract")){
                        workHours.setSelection(2);
                    }
                    if(pd.getWrkPeriod().equals("Internship")){
                        workHours.setSelection(2);
                    }
                    mSalary.setText(pd.getSalary());

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
            };
            mDatabaseReference = mDatabaseReference.child(MainActivity.userName).child("User_Details");
            mDatabaseReference.addChildEventListener(mListener);


        }

        //HANDLE INITIALIZING OF PREF VARS
        if(customerStat==null){
            prefs = getSharedPreferences(loan_step1.PREF_VARS, MODE_PRIVATE);
            set = prefs.getStringSet(loan_step1.KEY, null);
            sample=new ArrayList<String>(set);
        }

    }

    public void updateCloudFields(){

        try{
            Log.d("AeD","Updating...");
            mRef = FirebaseDatabase.getInstance().getReference(MainActivity.userName);
            ChildEventListener mListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey();
                    Log.d("AeD","Updating Key is "+key);
                    mRef.child("User_Details").child(key).child("alternate_Phone_Number").setValue(mAltTelNo.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("gender").setValue(gender);
                    mRef.child("User_Details").child(key).child("date_Of_Birth").setValue(dobDisp.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("id_Type").setValue(idType);
                    mRef.child("User_Details").child(key).child("id_Number").setValue(mIdNo.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("location").setValue(mLocation.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("post_Address").setValue(mPostAddr.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("length_of_Res").setValue(mLenthOfRes.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("mari_stat").setValue(mStatus);
                    mRef.child("User_Details").child(key).child("company_Name").setValue(mCompName.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("jobTitle").setValue(mJobTitle.getText().toString().trim());
                    mRef.child("User_Details").child(key).child("wrkPeriod").setValue(workHrs);
                    mRef.child("User_Details").child(key).child("salary").setValue(mSalary.getText().toString().trim());


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
            };
            mReference = mReference.child(MainActivity.userName).child("User_Details");
            mReference.addChildEventListener(mListener);

        }catch (Exception e){
            Log.d("AeD","Exception "+e);
        }

        Intent i = new Intent(personal_Details.this,loan_purpose.class);
        i.putExtra("UpdatingDetails","updateInfo");
        startActivity(i);
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
            validateSalary();
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.location:
                    validateLocation();
                    break;
                case R.id.salary:
                    validateSalary();
                    break;
                case R.id.telNo:
                    validateAltTelNo();
                    break;
                case R.id.jobTitle:
                    validateJobTitle();
                    break;
                case R.id.lengthOfRes:
                    validateLengthOfRes();
                    break;
                case R.id.compName:
                    validateCompName();
                    break;
            }
        }
    }
    private class DetailsAsyncTask extends AsyncTask<Void, Void, Void> {

        String location = mLocation.getText().toString();
        String salary = mSalary.getText().toString();
        String altNo = tempTel;
        String postAddr = mPostAddr.getText().toString();
        String lengthOfRes = mLenthOfRes.getText().toString();
        String compName = mCompName.getText().toString();
        String jobTitle = mJobTitle.getText().toString();
        String dob = dobDisp.getText().toString();
        String id = idType;
        String wkHrs = workHrs;
        String gend = gender;
        String mariStatus = mStatus;
        String idnumber = mIdNo.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... args) {
            // do background work here
            try{

//                List<StepTwoData>datalist2 = mDbHandler.getStepTwoData();
//                for (int i = 0; i < datalist2.size(); i++){
//                    StepTwoData std = datalist2.get(i);
//                    Log.d("AeD","Amount new  "+std.getAmount());
//                    Log.d("AeD","Days new "+std.getDays());
//
//
//                }

                PersonalData pd = new PersonalData();
                pd.setAlternate_Phone_Number(altNo);
                pd.setGender(gend);
                pd.setDate_Of_Birth(dob);
                pd.setCompany_Name(compName);
                pd.setId_Type(id);
                pd.setId_Number(idnumber);
                pd.setJobTitle(jobTitle);
                pd.setLength_of_Res(lengthOfRes);
                pd.setLocation(location);
                pd.setPost_Address(postAddr);
                pd.setSalary(salary);
                pd.setWrkPeriod(wkHrs);
                pd.setMari_stat(mariStatus);
                mDbHandler.addDetailsData(pd);

                //TODO CREATE FREBASE USER
                createFirebaseUser();
            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);

        }
    }
}
