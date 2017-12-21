package com.adfinancegh.aedmoneylending;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.key;

public class MainActivity extends AppCompatActivity {
    private TextView applyNowBtn;
    private TextView checkStatBtn;
    SharedPreferences prefs;
    String date;
    static String db_key;
    static String PREF_VARS = "db_key";
    static String KEY = "id";
    private String paymentStat;
    private String salary;
    static String userName;
    private DatabaseReference mDatabaseReference,mKeyRef;
    android.support.v4.app.NotificationCompat.Builder mBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyNowBtn = (TextView) findViewById(R.id.applyNowBtn);
        checkStatBtn = (TextView) findViewById(R.id.checkStatBtn);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        applyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyNowBtnClicked();
            }
        });


        SharedPreferences preferences =  getSharedPreferences(loan_step1.PREF_MAIL, MODE_PRIVATE);
        userName = preferences.getString(loan_step1.MAIL_KEY, null);

        if(userName!=null) {

            //PREPARING EMAIL TO BE USED AS ID IN DB
            userName = userName.replace("@","at");
            userName = userName.replace(".","dot");
            mKeyRef = FirebaseDatabase.getInstance().getReference(userName);



            ChildEventListener mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PurposeData pd = dataSnapshot.getValue(PurposeData.class);
                date = pd.getdue_date();
                db_key = dataSnapshot.getKey();
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("AeD","Date is "+date);
                mKeyRef.child("User_Details").child(db_key).child("token").setValue(token);
                Log.d("AeD","Snapshot key "+db_key);

                //GETTING SALARY AND FIRST TIMER STAT TO USED AT LOAN STEP TWO USER CLICK THE
                //NEW LOAN BUTTON AT MAIN ACTIVITY
                salary = pd.getSalary();
                paymentStat = pd.getFirst_timer();

                //DELETING FIRST TIMER IF LOAN HAS BEEN REPAID ON TIME
                if(pd.getstatus().equals("Paid-On-Time")){
                    mKeyRef.child("User_Details").child(db_key).child("first_timer").removeValue();
                }

                //GETTING  WHEN TO SEND NOTIFICATIONS

                try{
                    Calendar c  = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    c.add(Calendar.DATE,-7);
                    Date resultdate = new Date(c.getTimeInMillis());
                    String week_today = sdf.format(resultdate);

                    Log.d("AeD","Week ago "+week_today);
                    //WEEK PRE NOTICE
                    if(date.equals(week_today)){
                        sendWeekNotice();
                    }

                    //TODO RECTIFY NOTIFICATION AS THE NEXT DAY WHILE IT IS THE PREV DAY
                    Calendar c2  = Calendar.getInstance();
                    c2.add(Calendar.DATE,-1);
                    resultdate = new Date(c2.getTimeInMillis());
                    String prev_day = sdf.format(resultdate);
                    Log.d("AeD","Yesterday was "+prev_day);
                    if(date.equals(prev_day)){
                        sendDayNotice();
                    }

                    Calendar c3  = Calendar.getInstance();
                    resultdate = new Date(c3.getTimeInMillis());
                    String today = sdf.format(resultdate);
                    Log.d("AeD","Today is "+today);
                    if(date.equals(today)){
                        sendNotice();
                    }
                }catch (Exception e){
                    Log.d("AeD","Exception "+e);
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
        };


        mDatabaseReference = mDatabaseReference.child(userName).child("User_Details");
        mDatabaseReference.addChildEventListener(mListener);

        //mDatabaseReference.child(userName).child("User_Details").child(db_key).child("token").setValue(token);


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
            case R.id.mainMenumItem:
                goHome();
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

          //Intent i = new Intent(this,CustHist.class);
          //Intent i = new Intent(this,statLogin.class);
        //Intent i = new Intent(this,FirstScreen.class);
          Intent i = new Intent(this,help.class);
          startActivity(i);

    }

    public void showAbout(){
        Intent i = new Intent(this,About_Us.class);
        startActivity(i);
    }

    private void goHome(){

       Intent i = new Intent(this,FirstScreen.class);
       startActivity(i);

    }

    public void showDirections(){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }

    public void applyNowBtnClicked(){

        Intent i = new Intent(this,com.adfinancegh.aedmoneylending.loan_step2.class);
        i.putExtra("CustomerStat","re-applying");
        i.putExtra("paymentStat",paymentStat);
        i.putExtra("salary",salary);
        startActivity(i);
    }

    public void checkStatClicked(View v){
        //Intent i = new Intent(this,status_main.class);
        Intent i = new Intent(this,statusMainTab.class);
        startActivity(i);

    }



    public  void sendNotice(){
        //CREATING NOTIFICATION
        int uniqueId=2597;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aed_logo);
        mBuilder = new NotificationCompat.Builder(this).setLargeIcon(bitmap);
        mBuilder.setSmallIcon(R.drawable.aedsmall);
        mBuilder.setContentTitle("Facility Re-Payment");
        mBuilder.setContentText("Your AeD facility is due for payment today.Kindly,make sure you have funds in your account to prevent charges on late payment.");
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        mBuilder.setStyle(new NotificationCompat.InboxStyle());
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSoundUri);

        Intent i = new Intent(this,status_main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueId,mBuilder.build());


    }

    public  void sendWeekNotice(){
        //CREATING NOTIFICATION
        int uniqueId=2597;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aed_logo);
        mBuilder = new NotificationCompat.Builder(this).setLargeIcon(bitmap);
        mBuilder.setSmallIcon(R.drawable.aedsmall);
        mBuilder.setContentTitle("Facility Re-Payment");
        mBuilder.setContentText("Your AeD facility would be due for payment a week from today.");
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        mBuilder.setStyle(new NotificationCompat.InboxStyle());
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSoundUri);

        Intent i = new Intent(this,status_main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueId,mBuilder.build());


    }

    public  void sendDayNotice(){
        //CREATING NOTIFICATION
        int uniqueId=2597;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aed_logo);
        mBuilder = new NotificationCompat.Builder(this).setLargeIcon(bitmap);
        mBuilder.setSmallIcon(R.drawable.aedsmall);
        mBuilder.setContentTitle("Facility Re-Payment");
        mBuilder.setContentText("Your AeD facility would be due tomorrow.Kindly,make sure you have funds in your account to prevent charges on late payment.");
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        mBuilder.setStyle(new NotificationCompat.InboxStyle());
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSoundUri);

        Intent i = new Intent(this,status_main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueId,mBuilder.build());


    }


}
