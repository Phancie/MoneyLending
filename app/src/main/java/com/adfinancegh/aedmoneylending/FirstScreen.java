package com.adfinancegh.aedmoneylending;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirstScreen extends AppCompatActivity {
    private Button mLogIn,mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO CALLING FIREBASE PERSISTANCE HERE
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_first_screen);
        mLogIn = (Button)findViewById(R.id.log_in_Btn);
        mSignUp = (Button)findViewById(R.id.sign_up_Btn);
    }

    public void signUpClicked(View v){
        Intent i = new Intent(this,com.adfinancegh.aedmoneylending.loan_step1.class);
        startActivity(i);

    }

    public void logInClicked(View v){
        checkSession();
    }

    private void checkSession(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //TODO DECLARE A PROGRESS DIALOG HERE
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        } else {

            Intent i = new Intent(this,statLogin.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.faqmItem:
                showFaq();
                return true;
            case R.id.helpmItem:
                showHelp();
                return true;
            case R.id.aboutmItem:
                showAbout();
                return true;
            case R.id.directionsItem:
                showDirections();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFaq(){
        Intent i = new Intent(this,FAQ.class);
        startActivity(i);
    }

    public void showHelp(){

        Intent i = new Intent(this,help.class);
        Toast.makeText(this, "Please wait....", Toast.LENGTH_LONG).show();
        startActivity(i);

    }

    public void showAbout(){
        Intent i = new Intent(this,About_Us.class);

        startActivity(i);
    }

    public void showDirections(){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}
