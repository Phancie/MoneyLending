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
import android.support.v7.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by REUBEN on 9/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    android.support.v4.app.NotificationCompat.Builder mBuilder;
    private DatabaseReference mDatabaseReference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        ChildEventListener mListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                PurposeData pd = dataSnapshot.getValue(PurposeData.class);
//                String date = pd.getdue_date();
//                try{
//                    if(date.isEmpty()){
//                        sendNotification("Your account has been debit with your requested amount.Check your status for  more details.");
//                    }
//                    else {
//                        sendNotification("Please be reminded that your AeD facility is overdue and may incur charges.");
//                    }
//                }catch (Exception e){}
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        SharedPreferences preferences =  getSharedPreferences(loan_step1.PREF_PHONE, MODE_PRIVATE);
//        String userName = preferences.getString(loan_step1.PHONE_KEY, null);
//        mDatabaseReference = mDatabaseReference.child(userName).child("User_Details");
//        mDatabaseReference.addChildEventListener(mListener);
    //    sendNotification("Your account has been debited");
//        super.onMessageReceived(remoteMessage);
//        Map<String, String> data = remoteMessage.getData();
//        String myCustomKey = data.get("my_custom_key");


    }
    public  void sendNotification(String messageBody){
        //CREATING NOTIFICATION
        int uniqueId=2597;

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aed_logo);
        mBuilder = new NotificationCompat.Builder(this).setLargeIcon(bitmap);
        mBuilder.setSmallIcon(R.drawable.aedsmall);
        mBuilder.setContentTitle("AeD Finance");
        mBuilder.setContentText(messageBody);
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
