package com.adfinancegh.aedmoneylending;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.R.id.message;

public class help extends AppCompatActivity {
    private TextView mEmailHelp;
    private TextView mSubject;
    private TextView mBody;
    private TextView mName;
    private TextInputLayout mEmailHelpLayout;
    private TextInputLayout mSubjectLayout;
    private TextInputLayout mBodyLayout;
    private TextInputLayout mNameLayout;
    private Button mResetBtn;
    Session session = null;
    String subj;
    String email;
    String content;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mEmailHelp = (TextView)findViewById(R.id.emailHelp);
        mSubject = (TextView)findViewById(R.id.emailSubj);
        mBody = (TextView)findViewById(R.id.emailBody);
        mName = (TextView) findViewById(R.id.nameHelp);
        mEmailHelpLayout = (TextInputLayout)findViewById(R.id.email_help_layout);
        mSubjectLayout = (TextInputLayout)findViewById(R.id.subject_layout);
        mBodyLayout = (TextInputLayout)findViewById(R.id.emailBody_layout);
        mResetBtn = (Button)findViewById(R.id.sendBtn);
        mNameLayout = (TextInputLayout)findViewById(R.id.name_help_layout);


        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        //KEYBOARD SIGN IN
        mBody.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    public void submitForm(){
        if (!validateEmail()) {
            return;
        }
        new HelpAsyncTask().execute();
    }

    private void showResponseDialog(String message){
        new AlertDialog.Builder(this).setTitle("Email Sent").setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(help.this,MainActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    //TODO VALIDATE THE REST OF THE FIELDS ...

    private boolean validateEmail() {
        String emailHelp = mEmailHelp.getText().toString().trim();

        if (emailHelp.isEmpty() || !isValidEmail(emailHelp)) {
            mEmailHelpLayout.setError(getString(R.string.err_msg_email));
            requestFocus(mEmailHelp);
            return false;
        } else {
            mEmailHelpLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateName() {
        if (mName.getText().toString().trim().isEmpty()) {
            mNameLayout.setError(getString(R.string.err_msg_fname));
            requestFocus(mName);
            return false;
        } else {
            mNameLayout.setErrorEnabled(false);
        }

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

                case R.id.emailReset:
                    validateEmail();
                    break;
//                case R.id.pWord:
//                    validatePassword();
//                    break;
            }
        }
    }

    private class HelpAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog mProgressDialog = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(help.this);
            mProgressDialog.setMax(100);
            mProgressDialog.setMessage("Sending email.Please wait...");
            name = mName.getText().toString();
            email = mEmailHelp.getText().toString();
            subj = mSubject.getText().toString();
            content = mBody.getText().toString()+"<br />";
            content+=name +"<br /><br />";
            content +=email;
            Log.d("AeD","Email is in pre "+mEmailHelp.getText().toString());
            //mProgressDialog.show(help.this,"","Sending email.Please wait...",true);

        }

        protected String doInBackground(String... args) {
            // do background work here
            try{

                //mProgressDialog.dismiss();
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


                Log.d("AeD","Email is "+email);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("aedcapital@gmail.com"));
                message.setSubject(subj);
                message.setContent(content,"text/html; charset=utf-8");
                Transport.send(message);
                Log.d("AeD","Email sent");
                mProgressDialog.show();




            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(String result) {
            // do UI work here
            //super.onPostExecute(result);
            Log.d("AeD","In post execute");
            mProgressDialog.dismiss();
            showResponseDialog("Thanks for contacting us.We appreciate our customers' feedback and we would do our best to assist and respond to your requests");



        }
    }
}
