package com.adfinancegh.aedmoneylending;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.adfinancegh.aedmoneylending.R.id.loanPurp;
import static com.adfinancegh.aedmoneylending.UserDataAdapter.key;
import static com.adfinancegh.aedmoneylending.loan_step2.ACTUAL_AMOUNT;

public class loan_purpose extends AppCompatActivity {
    private Spinner bankSpinner;
    private Button guarantDobBtn;
    private TextView guarantDobDisp;
    private Button mApplyBtn;
    private RadioButton purpRbtn;
    private RadioGroup mPurpRadioGroup;
    private TextInputLayout mAccNamelayout, mAccNoLayout, mBankBranchLayout, mGuarantFnameLayout,
            mGuarantTelNoLayout, mGuarantDobDispLayout;
    private EditText mAccName, mAccNo, mBankBranch, mGuarantFname, mGuarantTelNo;
    private int month, year, day;
    private final static int MIN_AGE = 18;
    private final static String LOCAL_CODE = "0";
    private final static String COUNTRY_CODE = "+233";
    private final static int TEL_LOCAL_LENGTH = 10;
    private final static int TELNO_LENGTH = 13;
    private final static int ACCNOLENGTH = 8;
    static final int DIALOG_ID = 0;
    public static final String KEY = "LP";
    public static String PREF_VARS = "loan_purpose";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference,mDbRef,mRef,mRefHist;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatePickerDialog dialog;
    Session session = null;

    SharedPreferences prefsStep1,prefsStep2,prefsPersonal;
    List<String> sampleStep1,sampleStep2,samplePersonal;
    Set<String> setStep1,setStep2,setPersonal;
    String telno, tempTel,bankName,emailId;
    LocalDbHandler mDbHandler;
    String returningCust = null;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_purpose);
        bankSpinner = (Spinner) findViewById(R.id.bankSpinner);
        guarantDobBtn = (Button) findViewById(R.id.guarantDobBtn);
        guarantDobDisp = (TextView) findViewById(R.id.guarantDobDisp);
        mApplyBtn = (Button) findViewById(R.id.applyBtn);
        mAccNamelayout = (TextInputLayout) findViewById(R.id.accNameLayout);
        mAccNoLayout = (TextInputLayout) findViewById(R.id.accNoLayout);
        mBankBranchLayout = (TextInputLayout) findViewById(R.id.bankBranchLayout);
        mGuarantFnameLayout = (TextInputLayout) findViewById(R.id.guarantFnameLayout);
        mGuarantDobDispLayout = (TextInputLayout) findViewById(R.id.guarantDobDispLayout);
        mGuarantTelNoLayout = (TextInputLayout) findViewById(R.id.guarantTelNoLayout);
        mAccName = (EditText) findViewById(R.id.accName);
        mAccNo = (EditText) findViewById(R.id.accNo);
        mBankBranch = (EditText) findViewById(R.id.bankBranch);
        mGuarantFname = (EditText) findViewById(R.id.guarantFname);
        mGuarantTelNo = (EditText) findViewById(R.id.guarantTelNo);
        mPurpRadioGroup = (RadioGroup) findViewById(R.id.purpGroup);
        purpRbtn = (RadioButton) findViewById(R.id.personalRbtn);
        mDbHandler = new LocalDbHandler(this,null,null,1);

        //GETTING TEXT ON RADIO BUTTON
        mPurpRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                purpRbtn = (RadioButton) findViewById(checkedId);

            }
        });

        //PASSING THE STRING ARRAY IN THE RES TO SPINNER IN XML
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.banks, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply Gender array adapter to the spinner
        bankSpinner.setAdapter(adapter);

        //CHOSEN SPINNER VAL
        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankName = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //SETTING DATE DIALOG TO CURRENT DATE
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR , -18);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        guarantDobDisp.setText(year + "/" + month + "/"+ day);


        showDateDialog();

        //RETRIEVING EMAIL
        SharedPreferences preferences =  getSharedPreferences(loan_step1.PREF_MAIL, MODE_PRIVATE);
        emailId = preferences.getString(loan_step1.MAIL_KEY, null);

        // DATABASE VARIABLE INIT

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mRefHist = FirebaseDatabase.getInstance().getReference();

        //ENTER KEY PRESSED ON KEYBOARD
        mGuarantTelNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                submitForm();
                return true;
            }
        });

        Log.d("AeD","Loan Amount without interest is at Loan Purpose "+loan_step2.ACTUAL_AMOUNT);


    }




    @Override
    protected void onStop() {
        super.onStop();
        storeLocalData();
    }



    public void showDateDialog() {
        guarantDobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DATE PICKER DIALOG
                dialog = new DatePickerDialog(loan_purpose.this,android.R.style.Theme_Holo_Dialog,mDateSetListener
                        ,year,month,day);
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
                guarantDobDisp.setText(year + "/" + month + "/" + day);

            }
        };
    }



    public void nextBtnClicked(View v) {
       submitForm();
    }

    private void submitForm() {
        if (!validateAccName()) {
            return;
        }
        if (!validateAccNo()) {
            return;
        }

        if (!validateBankBranch()) {
            return;
        }

        if (!validateGuarantName()) {
            return;
        }


        if (!validateGuarantTelNo()) {
            return;
        }

        if(returningCust!=null){
            updateDetails();
            return;
        }
        //hasNumberBeenUsed();

        prefsStep1 = getSharedPreferences(loan_step1.PREF_VARS,MODE_PRIVATE);
        setStep1 = prefsStep1.getStringSet(loan_step1.KEY,null);
        sampleStep1  = new ArrayList<String>(setStep1);

        //CHECKING FOR EXISTING PHONE NUMBER AT LOAN STEP ONE
        if(sampleStep1.contains(tempTel)){
            mGuarantTelNoLayout.setError(getString(R.string.err_msg_tel_similar));
            requestFocus(mGuarantTelNo);
             return;
        }


        // CHECKING FOR EXISTING TELEPHONE NUMBERS AT PERSONAL DETAILS
        prefsPersonal = getSharedPreferences(personal_Details.PREF_VARS,MODE_PRIVATE);
        setPersonal = prefsPersonal.getStringSet(personal_Details.KEY,null);
        samplePersonal  = new ArrayList<String>(setPersonal);

        if(samplePersonal.contains(tempTel)){
            mGuarantTelNoLayout.setError(getString(R.string.err_msg_tel_similar));
            requestFocus(mGuarantTelNo);
            return;
        }
        else {
            mGuarantTelNoLayout.setErrorEnabled(false);
        }



        new PurposeAsyncTask().execute();

        Intent i = new Intent(this, exit.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBundles();

    }

    private boolean validateAccNo() {
        if (mAccNo.getText().toString().trim().isEmpty()) {
            mAccNoLayout.setError(getString(R.string.err_msg_acc_no));
            requestFocus(mAccNo);
            return false;
        }
        if (mAccNo.getText().toString().trim().length()<ACCNOLENGTH) {
            mAccNoLayout.setError(getString(R.string.err_msg_acc_no_valid));
            requestFocus(mAccNo);
            return false;
        } else {
            mAccNoLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAccName() {
        if (mAccName.getText().toString().trim().isEmpty()) {
            mAccNamelayout.setError(getString(R.string.err_msg_acc_name));
            requestFocus(mAccName);
            return false;
        } else {
            mAccNamelayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateBankBranch() {
        if (mBankBranch.getText().toString().trim().isEmpty()) {
            mBankBranchLayout.setError(getString(R.string.err_msg_bank_branch));
            requestFocus(mBankBranch);
            return false;
        } else {
            mBankBranchLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateGuarantName() {
        if (mGuarantFname.getText().toString().trim().isEmpty()) {
            mGuarantFnameLayout.setError(getString(R.string.err_msg_guarant_name));
            requestFocus(mGuarantFname);
            return false;
        } else {
            mGuarantFnameLayout.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateGuarantTelNo() {
        //DO MANIPULATION ON TELNO TO PRECEDE WITH +233
        if (mGuarantTelNo.getText().toString().trim().length() == TEL_LOCAL_LENGTH || mGuarantTelNo.getText().toString().trim().length() == TELNO_LENGTH) {
            telno = mGuarantTelNo.getText().toString().trim();
            tempTel = null;
            if (telno.startsWith(LOCAL_CODE)) {
                telno = telno.substring(1);
                tempTel = COUNTRY_CODE + telno;
                return true;
            }
            if (telno.startsWith(COUNTRY_CODE)) {
                tempTel = mGuarantTelNo.getText().toString();
                return true;
            }

            if (!telno.startsWith(COUNTRY_CODE)) {
                mGuarantTelNo.setError(getString(R.string.err_msg_tellength));
                requestFocus(mGuarantTelNo);
                return false;
            }

             else {
                mGuarantTelNoLayout.setErrorEnabled(false);

            }
        }
        else {
            mGuarantTelNo.setError(getString(R.string.err_msg_tellength));
            requestFocus(mGuarantTelNo);
            return false;
        }
        return true;
    }


    private void storeLocalData(){
        SharedPreferences prefs = getSharedPreferences(PREF_VARS,0);
        SharedPreferences.Editor editor = prefs.edit();

        String loanPurp = purpRbtn.getText().toString();
        String accountName = mAccName.getText().toString().trim();
        String accountNumber = mAccNo.getText().toString().trim();
        String bankbranch = mBankBranch.getText().toString().trim();
        String guarantorName = mGuarantFname.getText().toString().trim();
        String dob = guarantDobDisp.getText().toString().trim();
        String guarantorNumber = tempTel;

        ArrayList<String>loanPurposeList = new ArrayList<String>();
        loanPurposeList.add(loanPurp);
        loanPurposeList.add(accountName);
        loanPurposeList.add(accountNumber);
        loanPurposeList.add(bankbranch);
        loanPurposeList.add(guarantorName);
        loanPurposeList.add(dob);
        loanPurposeList.add(guarantorNumber);

        Set<String>loanPurposeSet = new HashSet<String>();
        loanPurposeSet.addAll(loanPurposeList);

        editor.putStringSet(KEY,loanPurposeSet);
        editor.commit();
    }



    private void cloudStorage(){

        prefsStep2 = getSharedPreferences(loan_step2.PREF_VARS,MODE_PRIVATE);
        prefsPersonal = getSharedPreferences(personal_Details.PREF_VARS,MODE_PRIVATE);


        setStep2 = prefsStep2.getStringSet(loan_step2.KEY,null);
        sampleStep2  = new ArrayList<String>(setStep2);
        setPersonal = prefsPersonal.getStringSet(personal_Details.KEY,null);
        samplePersonal = new ArrayList<String>(setPersonal);



    }

    public void sendMail(){


        //pDialog = ProgressDialog.show(loan_purpose.this,"","Sending Mail",true);
//        SendMail sendMail = new SendMail();
//        sendMail.execute();

    }

    private void showErrorDialog(String message){

        new AlertDialog.Builder(this).setTitle("Ooops").setMessage(message).setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void getBundles(){
        Bundle updateRequest = getIntent().getExtras();
        if(updateRequest==null){
            return;
        }
        returningCust = updateRequest.getString("UpdatingDetails");
        if(returningCust!=null){
            mApplyBtn.setText(R.string.update_data);
            mReference = FirebaseDatabase.getInstance().getReference();
            mReference = mReference.child(MainActivity.userName).child("User_Details");
            mReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PurposeData pd = dataSnapshot.getValue(PurposeData.class);
                    //ADDING PHONE NUMBERS TO PREVENT REPETITION
                    samplePersonal  = new ArrayList<String>();
                    samplePersonal.add(pd.getAlternate_Phone_Number());
                    samplePersonal.add(pd.getPhone());
                    Log.d("AeD"," Alt Phone number is "+pd.getAlternate_Phone_Number());
                    Log.d("AeD"," Phone number is "+pd.getPhone());


                    //TODO SET LOAN PURPOSE HERE FOLLOWED BY BANK
                    if(pd.getLoan_Purpose().equals("Business")){
                        purpRbtn = (RadioButton)findViewById(R.id.busRbtn);
                        purpRbtn.setChecked(true);
                    }

                    if(pd.getLoan_Purpose().equals("Fees")){
                        purpRbtn = (RadioButton)findViewById(R.id.feesRbtn);
                        purpRbtn.setChecked(true);
                    }

                    if(pd.getLoan_Purpose().equals("Medical")){
                        purpRbtn = (RadioButton)findViewById(R.id.medsRbtn);
                        purpRbtn.setChecked(true);
                    }

                    if(pd.getLoan_Purpose().equals("Purchase Goods")){
                        purpRbtn = (RadioButton)findViewById(R.id.purchaseRbtn);
                        purpRbtn.setChecked(true);
                    }

                    if(pd.getLoan_Purpose().equals("Emergency")){
                        purpRbtn = (RadioButton)findViewById(R.id.emergencyRbtn);
                        purpRbtn.setChecked(true);
                    }

                    //SETTING BANK SPINNER TO SERVER BANK
                    List<String> BanksList = Arrays.asList(getResources().getStringArray(R.array.banks));
                    for(int x = 0; x<BanksList.size();x++){
                        if(pd.getBank().equals(BanksList.get(x))){
                            bankSpinner.setSelection(x);
                            break;
                        }
                    }
                    //SETTING THE REST O SERVER DETAILS
                    mAccName.setText(pd.getAccount_Name());
                    mAccNo.setText(pd.getAccount_Number());
                    mBankBranch.setText(pd.getBankBranch());
                    mGuarantFname.setText(pd.getGuarantor_Full_Name());
                    guarantDobDisp.setText(pd.getGuarantor_Date_Of_Birth());
                    mGuarantTelNo.setText(pd.getGuarantor_Phone_Number());
                    Log.d("AeD","Guarantor date "+pd.getGuarantor_Date_Of_Birth());

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

//        if(returningCust==null){
//            // CHECKING FOR EXISTING TELEPHONE NUMBERS AT PERSONAL DETAILS
//            prefsPersonal = getSharedPreferences(personal_Details.PREF_VARS,MODE_PRIVATE);
//            setPersonal = prefsPersonal.getStringSet(personal_Details.KEY,null);
//            samplePersonal  = new ArrayList<String>(setPersonal);
//        }
    }

    public void updateDetails(){
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference(MainActivity.userName);
        mDbRef = mDbRef.child(MainActivity.userName).child("User_Details");

        //CHECKING FOR EXISTING PHONE NUMBER IN FB

        if(samplePersonal.contains(tempTel)){
            mGuarantTelNoLayout.setError(getString(R.string.err_msg_tel_similar));
            requestFocus(mGuarantTelNo);
            return;
        }
        else {
            mGuarantTelNoLayout.setErrorEnabled(false);
        }

        mDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Log.d("AeD","Updating Purpose Key is "+key);
                mRef.child("User_Details").child(key).child("loan_Purpose").setValue(purpRbtn.getText().toString());
                mRef.child("User_Details").child(key).child("bank").setValue(bankName);
                mRef.child("User_Details").child(key).child("account_Name").setValue(mAccName.getText().toString());
                mRef.child("User_Details").child(key).child("account_Number").setValue(mAccNo.getText().toString());
                mRef.child("User_Details").child(key).child("bankBranch").setValue(mBankBranch.getText().toString());
                mRef.child("User_Details").child(key).child("guarantor_Full_Name").setValue(mGuarantFname.getText().toString());
                mRef.child("User_Details").child(key).child("guarantor_Date_Of_Birth").setValue(guarantDobDisp.getText().toString());
                mRef.child("User_Details").child(key).child("guarantor_Phone_Number").setValue(mGuarantTelNo.getText().toString());
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

        Intent i = new Intent(loan_purpose.this,statusMainTab.class);
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
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.accName:
                    validateAccName();
                    break;
                case R.id.accNo:
                    validateAccNo();
                    break;
                case R.id.bankBranch:
                    validateBankBranch();
                    break;
                case R.id.guarantFname:
                    validateGuarantName();
                    break;
                case R.id.guarantTelNo:
                    validateGuarantTelNo();
                    break;
            }
        }
    }

    private class PurposeAsyncTask extends AsyncTask<Void, Void, Void> {

        //VARIABLES OF THIS CLASS
        String loanPurp = purpRbtn.getText().toString();
        String accountName = mAccName.getText().toString().trim();
        String accountNumber = mAccNo.getText().toString().trim();
        String bankbranch = mBankBranch.getText().toString().trim();
        String guarantorName = mGuarantFname.getText().toString().trim();
        String dob = guarantDobDisp.getText().toString().trim();
        String guarantorNumber = tempTel;
        ProgressDialog mProgressDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(loan_purpose.this,"","Please wait...",true);

        }

        protected Void doInBackground(Void... args) {
            // do background work here
            String content ="Find the below the details of " ;

            try{
                List<stepOneData>datalist =  mDbHandler.getStepOneData();
                String fnameFire = "";
                String mnameFire = "";
                String lnameFire = "";
                String phoneFire = "";
                String emailFire = "";
                for (int i = 0; i < datalist.size(); i++) {
                    stepOneData sod = datalist.get(i);
                    fnameFire = sod.getFname();
                    lnameFire = sod.getLname();
                    mnameFire = sod.getMname();
                    phoneFire = sod.getPhone();
                    emailFire = sod.getEmail();

                }
                content+=fnameFire+" "+mnameFire+" "+lnameFire+"'s loan application form"+"<br />";
                content+="Full Name : "+fnameFire+" "+mnameFire+" "+lnameFire+"<br />";
                content+="Phone Number : "+phoneFire+"<br />";
                content+="Email : "+emailFire+"<br />";

                //NOT RETRIEVING LOAN AMOUNT AND DAYS FROM SQLITE
                String amtFire = loan_step2.amountInt;
                String daysFire =loan_step2.numberOfDays;


                content+="Amount Requested without interest (GHS) : "+ ACTUAL_AMOUNT+".00"+"<br />";
                content+="Amount Requested with interest (GHS) : "+amtFire+"0"+"<br />";
                content+="No Of Days : "+daysFire+"<br />";

                //TODO CONTINUE TO APPEND THE STRING BUILDER

                List<PersonalData>datalist3 =  mDbHandler.getTableDetailsData();
                String altnoFire = "";
                String genderFire = "";
                String dobFire = "";
                String idNoFire = "";
                String idTypeFire = "";
                String locationFire = "";
                String postAddrFire = "";
                String lengthOfResFire = "";
                String mariStatFire = "";
                String compNameFire = "";
                String jobTitleFire = "";
                String workPeriodFire = "";
                String salaryFire = "";

                for (int i = 0; i < datalist3.size(); i++) {
                    PersonalData pd = datalist3.get(i);
                    altnoFire = pd.getAlternate_Phone_Number();
                    genderFire = pd.getGender();
                    dobFire = pd.getDate_Of_Birth();
                    idNoFire = pd.getId_Number();
                    idTypeFire = pd.getId_Type();
                    locationFire = pd.getLocation();
                    postAddrFire = pd.getPost_Address();
                    lengthOfResFire = pd.getLength_of_Res();
                    mariStatFire = pd.getMari_stat();
                    compNameFire = pd.getCompany_Name();
                    jobTitleFire = pd.getJobTitle();
                    workPeriodFire = pd.getWrkPeriod();
                    salaryFire = pd.getSalary();
                }
                content+="Alternate Phone Number : "+altnoFire+"<br />";
                content+="Gender : "+genderFire+"<br />";
                content+="Date Of Birth : "+dobFire+"<br />";
                content+="ID Type : "+idTypeFire+"<br />";
                content+="ID Number : "+idNoFire+"<br />";
                content+="Location : "+locationFire+"<br />";
                content+="How long he/she has lived in the current residence : "+lengthOfResFire+" year(s)"+"<br />";
                content+="Postal Address : "+postAddrFire+"<br />";
                content+="Marital Status : "+mariStatFire+"<br />";
                content+="Current Company Name : "+compNameFire+"<br />";
                content+="Job Title : "+jobTitleFire+"<br />";
                content+="How long does he/she work : "+workPeriodFire+"<br />";
                content+="Salary : "+salaryFire+"<br />";
                content+="Loan Purpose : "+loanPurp+"<br />";
                content+="Bank Name : "+bankName+"<br />";
                content+="Account Name : "+accountName+"<br />";
                content+="Account Number : "+accountNumber+"<br />";
                content+="Bank Branch : "+bankbranch+"<br />";
                content+="Guarantor's Name : "+guarantorName+"<br />";
                content+="Guarantor's Date Of Birth : "+dob+"<br />";
                content+="Guarantor's Phone Number : "+guarantorNumber+". <br /><br />";
                content+="Thank you."+"<br /><br />"+"AeD Team.";

                cloudStorage();

                String token = FirebaseInstanceId.getInstance().getToken();
                String status = "Pending";
                String due_date = "Unspecified";

                PurposeData pd = new PurposeData(fnameFire,mnameFire,lnameFire,phoneFire,emailFire,amtFire, ACTUAL_AMOUNT,daysFire,altnoFire,genderFire
                        ,dobFire,idTypeFire,idNoFire,locationFire,postAddrFire,lengthOfResFire,mariStatFire,compNameFire,jobTitleFire,
                        workPeriodFire,salaryFire,loanPurp,bankName,accountName,accountNumber,bankbranch,guarantorName,
                        dob,guarantorNumber,token,status,due_date,"true");

                emailId = emailId.replace("@","at");
                emailId = emailId.replace(".","dot");
                mDatabaseReference.child(emailId).child("User_Details").push().setValue(pd);
                Log.d("AeD","User Data storage successful");

                //INSERTING FIRST LOAN HISTORY
//                LoanHistory lh = new LoanHistory(amtFire,due_date,status);
//                mRefHist.child(emailId).child("Loan_History").push().setValue(lh);

                //TODO SEND EMAIL TO ADMIN
                Properties props = new Properties();
                props.put("mail.smtp.host","smtp.gmail.com");
                props.put("mail.smtp.port","465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.port", "465");

                session  = Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("aedcapital@gmail.com","Password@1");
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("aedcapital@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("aedcapital@gmail.com"));
                message.setSubject("Online Loan Application");
                message.setContent(content,"text/html; charset=utf-8");
                Transport.send(message);
                Log.d("AeD","Email sent");


            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
            //DELAY PROGRESS BAR FOR SOME TIME
            long delayInMillis = 8000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            }, delayInMillis);
            //mProgressDialog.dismiss();

        }
    }

}
