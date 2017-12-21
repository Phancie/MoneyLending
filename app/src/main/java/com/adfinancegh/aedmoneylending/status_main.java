package com.adfinancegh.aedmoneylending;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.adfinancegh.aedmoneylending.MainActivity.db_key;

public class status_main extends AppCompatActivity {

    private String userName = "";
    ListView mStepOneListView;
    private DatabaseReference mDatabaseReference;
    private UserDataAdapter mAdapter;
    private TextView mFullName;
    private List<String> listItem;
    String db_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_main);
        mStepOneListView = (ListView)findViewById(R.id.data_list_view);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFullName = (TextView)findViewById(R.id.fullname);


        //RETRIEVING EMAIL ID
        SharedPreferences preferences =  getSharedPreferences(loan_step1.PREF_MAIL, MODE_PRIVATE);
        userName = preferences.getString(loan_step1.MAIL_KEY, null);

        //PREPARING EMAIL TO BE USED AS ID IN DB
        userName = userName.replace("@","at");
        userName = userName.replace(".","dot");

        //CREATING MENU LIST
        listItem = new ArrayList<>();
        listItem.add(0,"Logout");

       mDatabaseReference.child(userName).child("User_Details");


    }




    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new UserDataAdapter(this,mDatabaseReference,userName);
        mStepOneListView.setAdapter(mAdapter);
        new LoginMainAsyncTask().execute();


    }


    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.cleanup();

    }

    public void refresh(){
        Intent i = new Intent(this,status_main.class);
        startActivity(i);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        menu.add(0,0,Menu.NONE,"Logout");
        menu.add(0,1,Menu.NONE,"Refresh");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        logOut();
//        return true;

        switch (item.getItemId()) {
            case 0:
                logOut();
                return true;
            case 1:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this,statLogin.class);
        startActivity(i);

    }

    private class LoginMainAsyncTask extends AsyncTask<String, String, String> {
        private  ProgressDialog mProgressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(status_main.this);
            mProgressDialog.setMax(100);
            //mProgressDialog.show(status_main.this,"","Retrieving Data.Please wait...",true);

        }

        protected String doInBackground(String... args) {
            // do background work here
            try{

                mProgressDialog.dismiss();

            }catch (Exception e){}

            return null;
        }
        protected void onPostExecute(String result) {
            // do UI work here
            //super.onPostExecute(result);
            Log.d("AeD","In post execute");
            if(mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);


    }

}
