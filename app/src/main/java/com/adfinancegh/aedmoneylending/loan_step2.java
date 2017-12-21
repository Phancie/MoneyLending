package com.adfinancegh.aedmoneylending;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.os.Build.VERSION_CODES.M;
import static com.adfinancegh.aedmoneylending.R.id.amtNeeded;
import static com.adfinancegh.aedmoneylending.R.id.phoneNo;

public class loan_step2 extends AppCompatActivity {
    private Button mNextBtn;
    private TextInputLayout mAmountNeededLayout,mNoOfDayslayout;
    private EditText mAmountNeeded,mNoOfDays;
    private TextView mIntrVal,mAmountVal,mRateVal;
    private final static double RATE_14 = 0.08;
    private final static double RATE_21 = 0.09;
    private final static double RATE_30 = 0.10;
    private final static double MAX_AMOUNT = 300.00;
    private final static double MAX_AMOUNT_ON_RETURN = 500.00;
    private final static int MAX_DAYS = 30;
    private final static double MIN_AMOUNT = 50;
    private final static int MIN_DAYS = 1;
    private final static int AMOUNT_MULTIPLE = 50;
    public static final String PREF_VARS = "loan_step_2";
    public static String KEY = "LS2";
    DatabaseReference mDatabaseReference,mReference;
    int DAYS = 0;
    static String numberOfDays = null;
    double AMOUNT =0.0;
    static String amountInt=null;
    LocalDbHandler mDbHandler;
    String returningCust=null;
    String paymentStat = null;
    Session session = null;
    String applicant;
    String salary;
    static String ACTUAL_AMOUNT = null;
    int flag;

//TODO DO NOT USE LOAN AMOUNT FROM SQL LITE BCOS IT WOULD RATHER USE LOAN FROM PREVIOUS REQUESTS
//TODO JUST MAKE THE SUM OF LOAN AND INTEREST VARIABLE STATIC TO BE ACCESSED LATER FOR FIREBASE MANIPULATIONS
//TODO DO THE SAME FOR NUMBER OF DAYS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_step2);

        mNextBtn = (Button)findViewById(R.id.nextBtn);
        mAmountNeeded = (EditText)findViewById(amtNeeded);
        mNoOfDays = (EditText)findViewById(R.id.noOfDays);
        mRateVal = (TextView)findViewById(R.id.rateVal);
        mAmountNeededLayout = (TextInputLayout)findViewById(R.id.amtNeededLayout);
        mNoOfDayslayout = (TextInputLayout)findViewById(R.id.noOfDaysLayout);
        mIntrVal = (TextView)findViewById(R.id.intrVal);
        mAmountVal = (TextView)findViewById(R.id.amtVal);
        mDbHandler = new LocalDbHandler(this,null,null,1);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        
        //HANDLING EXTRA BUNDLES
        getBundles();
        Log.d("AeD","payment stat is "+paymentStat);
        Log.d("AeD","returning customer value is "+returningCust);

               //KEYBOARD SIGN IN
        mNoOfDays.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.loanStep2_form_finished || id == EditorInfo.IME_NULL) {
                    submitForm();
                    return true;
                }
                return false;
            }
        });

        mAmountNeeded.addTextChangedListener(new MyTextWatcher(mAmountNeeded));
        mNoOfDays.addTextChangedListener(new MyTextWatcher(mNoOfDays));

//        SharedPreferences preferences =  getSharedPreferences(loan_step1.PREF_PHONE, MODE_PRIVATE);
//        pPhone = preferences.getString(loan_step1.PHONE_KEY, null);


    }




    public void nextBtnClicked(View v){
        submitForm();
    }

    private void calcLoan(){


            try{
                AMOUNT = Double.parseDouble(mAmountNeeded.getText().toString());
                DAYS = Integer.parseInt(mNoOfDays.getText().toString());
                double tempAmt =AMOUNT;
                double interest = 0.0;
                double sum = 0.0;
                if (DAYS <= 14) {
                    interest = tempAmt * RATE_14;
                    mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
                    sum = interest + tempAmt;
                    mRateVal.setText(String.valueOf(RATE_14));
                    mAmountVal.setText("GHS" + String.valueOf(sum) + "0");
                }


                if (DAYS > 14 && DAYS <= 21) {
                    interest = tempAmt * RATE_21;
                    mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
                    sum = interest + tempAmt;
                    mAmountVal.setText("GHS" + String.valueOf(sum) + "0");
                    mRateVal.setText(String.valueOf(RATE_21));
                }

                if (DAYS > 21) {
                    interest = tempAmt * RATE_30;
                    mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
                    sum = interest + tempAmt;
                    mAmountVal.setText("GHS" + String.valueOf(sum) + "0");
                    mRateVal.setText(String.valueOf(RATE_30));
                }
                amountInt = String.valueOf(sum);
                Log.d("AeD","Value of amount int is "+amountInt);
                //TODO STORE THIS VALUE IN FB AS ACT_AMOUNT
                ACTUAL_AMOUNT = mAmountNeeded.getText().toString();
                Log.d("AeD","Loan Amount without interest is "+ACTUAL_AMOUNT);

            }catch (Exception e){}


    }

    public void nullCrushControl(){
        if(!mAmountNeeded.getText().toString().isEmpty() && !mNoOfDays.getText().toString().isEmpty()){
            calcLoan();
        }

    }

    public void calcLoan(View v){
        String amtNeeded = mAmountNeeded.getText().toString();

        DAYS = Integer.parseInt(mNoOfDays.getText().toString());

        double tempAmt = Double.parseDouble(amtNeeded);
        double interest = 0.0;
        double sum = 0.0;
        if (DAYS <= 14) {
            interest = tempAmt * RATE_14;
            mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
            sum = interest + tempAmt;
            mRateVal.setText(String.valueOf(RATE_14));
            mAmountVal.setText("GHS" + String.valueOf(sum) + "0");

        }


        if (DAYS > 14 && DAYS <= 21) {
            interest = tempAmt * RATE_21;
            mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
            sum = interest + tempAmt;
            mAmountVal.setText("GHS" + String.valueOf(sum) + "0");
            mRateVal.setText(String.valueOf(RATE_21));

        }

        if (DAYS > 21) {
            interest = tempAmt * RATE_30;
            mIntrVal.setText("GHS" + String.valueOf(interest) + "0");
            sum = interest + tempAmt;
            mAmountVal.setText("GHS" + String.valueOf(sum) + "0");
            mRateVal.setText(String.valueOf(RATE_30));

        }

        amountInt = String.valueOf(sum);
        Log.d("AeD","Value of amount int is "+amountInt);
        //TODO STORE THIS VALUE IN FB AS ACT_AMOUNT
        ACTUAL_AMOUNT = mAmountNeeded.getText().toString();

    }

    private void submitForm() {
        if (!validateAmount()) {
            return;
        }

        if (!validateDays()) {
            return;
        }

        if(returningCust!=null){
            updateLoan();
//            if(flag==1){
//                showAlertDialog("Would you like to update your details?");
//            }

            return;
        }
        //STORING  LOCALLY
        storeLocalData();


        Intent i = new Intent(this,personal_Details.class);
        i.putExtra("loanAmount",ACTUAL_AMOUNT);
        startActivity(i);
    }
    private boolean validateAmount() {
        if (!mAmountNeeded.getText().toString().trim().isEmpty()){
            isAmountValid();
            if(!isAmountValid()){
                return false;
            }

           else {
                mAmountNeededLayout.setErrorEnabled(false);
            }

            return true;
        }
       else {
            mAmountNeededLayout.setError(getString(R.string.err_msg_amount));
            requestFocus(mAmountNeeded);
            return false;
        }

    }


    private boolean validateDays() {
        numberOfDays = mNoOfDays.getText().toString();
        if(!mNoOfDays.getText().toString().trim().isEmpty()){
            if(!isDayValid()){
                return false;
            }
            else {
                mNoOfDayslayout.setErrorEnabled(false);
            }

            return true;

        }else {
            mNoOfDayslayout.setError(getString(R.string.err_msg_days));
            requestFocus(mNoOfDays);
            return false;
        }

    }


    private boolean isAmountValid(){
        String amount = mAmountNeeded.getText().toString().trim();
        double tempAmount = Double.parseDouble(amount);

        //THIS CHECK WOULD BE DONE WHEN USER APPLIES FOR THE VERY FIRST TIME
        if(returningCust==null ){
            if(tempAmount%AMOUNT_MULTIPLE!=0){
                mAmountNeededLayout.setError(getString(R.string.err_msg_multiple_of_fifty));
                requestFocus(mAmountNeeded);
                return false;
            }

            if(tempAmount>=MIN_AMOUNT && tempAmount<=MAX_AMOUNT ){
                mAmountNeededLayout.setErrorEnabled(false);
                return true;
            }
            else if(tempAmount<MIN_AMOUNT) {
                mAmountNeededLayout.setError(getString(R.string.err_msg_amount_min));
                requestFocus(mAmountNeeded);
                return false;
            }
            else if(tempAmount>MAX_AMOUNT) {

                mAmountNeededLayout.setError(getString(R.string.err_msg_amount_max));
                requestFocus(mAmountNeeded);
                return false;
            }
        }

    //THIS CHECK WOULD BE DONE ONLY WHEN USER HAS APPLIED BEFORE
        if(returningCust!=null){
            //CALCULATING TO PREVENT TAKING MORE 40% OF SALARY AS LOAN ON RETURN
                double salRemained = Double.parseDouble(salary)*0.4;
                if(tempAmount%AMOUNT_MULTIPLE!=0){
                    mAmountNeededLayout.setError(getString(R.string.err_msg_multiple_of_fifty));
                    requestFocus(mAmountNeeded);
                    return false;
                }

                if(paymentStat!=null){
                    if(tempAmount>MAX_AMOUNT) {

                        mAmountNeededLayout.setError(getString(R.string.err_msg_amount_max));
                        requestFocus(mAmountNeeded);
                        return false;
                    }
                }

                if(salRemained<tempAmount){
                    mAmountNeededLayout.setError(getString(R.string.err_salary));
                    requestFocus(mAmountNeeded);
                    return false;
                }

                if(tempAmount>MAX_AMOUNT_ON_RETURN){
                mAmountNeededLayout.setError(getString(R.string.err_msg_amount_max_on_return));
                requestFocus(mAmountNeeded);
                return false;
             }

        }

        return true;

    }

    private boolean isDayValid(){
        String days = mNoOfDays.getText().toString().trim();
        int tempDays = Integer.parseInt(days);
        if(tempDays>=MIN_DAYS && tempDays<=MAX_DAYS){
            mNoOfDayslayout.setErrorEnabled(false);
            return true;
        }
        else if(tempDays<MIN_DAYS) {
            mNoOfDayslayout.setError(getString(R.string.err_msg_days_min));
            requestFocus(mNoOfDays);
            return false;
        }
        else {
            mNoOfDayslayout.setError(getString(R.string.err_msg_days_max));
            requestFocus(mNoOfDays);
            return false;
        }

    }

    private void storeLocalData(){
        SharedPreferences prefs = getSharedPreferences(PREF_VARS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String amount = mAmountNeeded.getText().toString();
        String days = mNoOfDays.getText().toString();

        ArrayList<String>loanStep2Det = new ArrayList<String>();
        loanStep2Det.add(0,amount);
        loanStep2Det.add(1,days);

        Set<String>loanStep2Set = new HashSet<String>();
        loanStep2Set.addAll(loanStep2Det);

        editor.putStringSet(KEY,loanStep2Set);

        editor.commit();
    }

    public void checkSalaryOnReturn(){
        //int salary =
    }

    public void getBundles(){

        Intent i = getIntent();
        returningCust = i.getStringExtra("CustomerStat");
        paymentStat = i.getStringExtra("paymentStat");
        salary = i.getStringExtra("salary");
        Log.d("AeD","Payment status "+paymentStat);
        if(returningCust!=null){
            mReference = FirebaseDatabase.getInstance().getReference(MainActivity.userName);
        }
        Log.d("AeD","Bundle is "+returningCust);
    }

    public void updateLoan(){
        mDatabaseReference = mDatabaseReference.child(MainActivity.userName).child("User_Details");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PurposeData pd = dataSnapshot.getValue(PurposeData.class);
                String key = dataSnapshot.getKey();
                String status = "Pending";
                String due_date = "Unspecified";
                applicant = pd.getFname()+" "+pd.getMname()+" "+pd.getLname();
                String currentStatus = pd.getstatus();
                Log.d("AeD","Applicant name is "+applicant);
//TODO PREVENT DETAILS FROM UPDATING IF STATUS IS NOT APPROPRIATE
                if(currentStatus.equalsIgnoreCase("Outstanding") || currentStatus.equalsIgnoreCase("Disbursed") || currentStatus.equalsIgnoreCase("Delayed-Repayment")){
                    showErrorDialog("Your current AeD account status does not permit for a new loan. Please contact AeD" +
                            " office for further information.");
                    flag = 0;
                    Log.d("AeD","Status is "+pd.getstatus());
                    Log.d("AeD","Flag is "+flag);
                    return;
                }
                flag = 1;
                //UPDATING USER DETAILS
                mReference.child("User_Details").child(key).child("amount").setValue(amountInt);
                mReference.child("User_Details").child(key).child("actual_amount").setValue(mAmountNeeded.getText().toString().trim());
                mReference.child("User_Details").child(key).child("due_date").setValue(due_date);
                mReference.child("User_Details").child(key).child("status").setValue(status);
                mReference.child("User_Details").child(key).child("days").setValue(mNoOfDays.getText().toString().trim());
                //THIS WOULD WORK IF USER APPLIES LESS THAN GHC300 ON SECOND INSTANCE
                mReference.child("User_Details").child(key).child("first_timer").removeValue();

                if(flag ==1){
                    Log.d("AeD","Flag before aync "+flag);
                    new StepTwoAsyncTask().execute();
                    showAlertDialog("Would you like to update your details?");
                    //INSERTING INTO LOAN HISTORY
        //            String status = "Approved";
        //            String due_date = "Unspecified";
        //            LoanHistory lh = new LoanHistory(amountInt,due_date,status);
        //            mReferenceHistory.child(MainActivity.userName).child("Loan_History").push().setValue(lh);
                }

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

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this).setTitle("Loan Re-Application Failed").setMessage(message).setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }


    public void showAlertDialog(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("UPDATE INFO");
        alert.setMessage(message);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Do something here when "ok" clicked
                Intent i = new Intent(loan_step2.this,personal_Details.class);
                i.putExtra("UpdatingDetails","updateInfo");
                startActivity(i);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //So sth here when "cancel" clicked.
                Intent i = new Intent(loan_step2.this,statusMainTab.class);
                startActivity(i);
            }
        });
        alert.show();
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
            //if(mAmountNeeded.getText().toString().length()!=0 && mNoOfDays.getText().toString().length()!=0) {
                nullCrushControl();

           //else return;
        }


        public void afterTextChanged(Editable editable) {
            if (mAmountNeeded.getText().toString().length() != 0 && mNoOfDays.getText().toString().length() != 0) {
                switch (view.getId()) {
                    case amtNeeded:
                        validateAmount();
                        break;
                    case R.id.noOfDays:
                        validateDays();
                        break;
                }
            }
            else if(mAmountNeeded.getText().toString().length() != 0 && mNoOfDays.getText().toString().length() == 0){
                mAmountVal.setText("GHS"+mAmountNeeded.getText().toString());
                mIntrVal.setText("GHS0.00");
                mRateVal.setText("0.00");
            }
            else {
                mAmountVal.setText("GHS0.00");
                mIntrVal.setText("GHS0.00");
                mRateVal.setText("0.00");
            }
        }
    }

    private class StepTwoAsyncTask extends AsyncTask<Void, Void, Void> {
        String amount = amountInt;
        String days = mNoOfDays.getText().toString();
        String act_amount = mAmountNeeded.getText().toString().trim();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... args) {
            // do background work here
            //TODO DO NOT SEND MAIL IF LOAN IF NOT GRANTED
            //THESE INFORMATION WOULD BE RELEVANT IN CASE OF LOAN RE-APPLICATION
            Log.d("AeD","Background applicant "+applicant);
            String content = "Find below the loan re-application details of "+applicant+"<br />";
            content +="Amount(without interest) "+act_amount+"<br />";
            content +="Amount(with interest) "+amountInt+"<br />";
            content +="Number of days "+numberOfDays+"<br />";
            content +="Contact your IT department for the full details of loan applicant"+"<br /><br />";
            content+="AeD Team";
            try{
                //TODO HOW TO READ SQLITE DATA
//                List<stepOneData>datalist =  mDbHandler.getStepOneData();
//
//                for (int i = 0; i < datalist.size(); i++) {
//                    stepOneData sod = datalist.get(i);
//
//                    Log.d("AeD","Fname "+sod.getFname());
//                    Log.d("AeD","Lname "+sod.getLname());
//                    Log.d("AeD","Mname "+sod.getMname());
//                    Log.d("AeD","Phone "+sod.getPhone());
//                    Log.d("AeD","Email "+sod.getEmail());
//
//                }

                StepTwoData std = new StepTwoData();
                std.setAmount(amount);
                std.setDays(days);
                mDbHandler.addStepTwoData(std);

                if(returningCust!=null){
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
                    message.setSubject("Online Loan Re-Application");
                    message.setContent(content,"text/html; charset=utf-8");
                    Transport.send(message);
                    Log.d("AeD","Email sent");
                }


            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);


        }
    }
}
