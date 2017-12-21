package com.adfinancegh.aedmoneylending;

import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.adfinancegh.aedmoneylending.loan_step1.PASSWORD_LENGTH;

public class CustHist extends AppCompatActivity {

    private Button histNextBtn;
    private EditText histEmail;
    private EditText histPword;
    private TextInputLayout histEmailLayout,histPwordLayout;
    private RadioGroup custHistGroup;
    private RadioButton custHistRbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_hist);
        histNextBtn = (Button)findViewById(R.id.histnextBtn);
        histEmail = (EditText)findViewById(R.id.histemailAddr);
        histPword = (EditText)findViewById(R.id.histpWord);
        histEmailLayout = (TextInputLayout)findViewById(R.id.histEmailAddrLayout);
        histPwordLayout = (TextInputLayout)findViewById(R.id.histpWordLayout);
        custHistGroup = (RadioGroup)findViewById(R.id.histGroup);
        custHistRbtn = (RadioButton)findViewById(R.id.newCustRbtn);
        //KEYBOARD SIGN IN
        histPword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.hist_form_finished || id == EditorInfo.IME_NULL) {
                    submitForm();
                    return true;
                }
                return false;
            }
        });

        histEmail.addTextChangedListener(new MyTextWatcher(histEmail));
        histPword.addTextChangedListener(new MyTextWatcher(histPword));

        //GETTING TEXT ON RADIO BUTTON
        custHistGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                custHistRbtn = (RadioButton) findViewById(checkedId);

            }
        });
    }

    private void submitForm(){
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if(custHistRbtn.getText().toString().equals("New Customer")){
            Toast.makeText(this,"New Customer",Toast.LENGTH_SHORT).show();
        }
        if(custHistRbtn.getText().toString().equals("Returning Customer")){
            Toast.makeText(this,"Returning Customer",Toast.LENGTH_SHORT).show();
        }
    }

    public void histNextBtnClicked(View v){
        submitForm();

    }

    public void newCustClicked(View v){
        histEmailLayout.setVisibility(View.GONE);
        histPwordLayout.setVisibility(View.GONE);

    }

    public void retCustClicked(View v){
        histEmailLayout.setVisibility(View.VISIBLE);
        histPwordLayout.setVisibility(View.VISIBLE);
    }


    private boolean validateEmail() {
        String email = histEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            histEmailLayout.setError(getString(R.string.err_msg_email));
            requestFocus(histEmail);
            return false;
        } else {
            histEmailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (histPword.getText().toString().trim().isEmpty()) {
            histPwordLayout.setError(getString(R.string.err_msg_password));
            requestFocus(histPword);
            return false;
        }
        else if(histPword.getText().toString().trim().length()<PASSWORD_LENGTH){
            histPwordLayout.setError(getString(R.string.err_length_msg_password));
            requestFocus(histPword);
            return false;
        }
        else {
            histPwordLayout.setErrorEnabled(false);
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
                case R.id.histemailAddr:
                    validateEmail();
                    break;
                case R.id.histpWord:
                    validatePassword();
                    break;

            }
        }
    }
}
