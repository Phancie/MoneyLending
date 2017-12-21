package com.adfinancegh.aedmoneylending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by REUBEN on 9/12/2017.
 */

public class UserDataAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String userName;
    private String section;
    private ArrayList<DataSnapshot> mDataSnapshots;
    private ProgressDialog mProgressDialog;
    private int month, year, day;
    static String key;
    SharedPreferences prefs;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mDataSnapshots.add(dataSnapshot);
            key = dataSnapshot.getKey();
            Log.d("AeD","Token new "+key);
            notifyDataSetChanged();
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




    public UserDataAdapter(Activity activity,DatabaseReference ref,String name){

        userName = name;
        mActivity = activity;
        mDatabaseReference = ref.child(name).child("User_Details");
        mDatabaseReference.addChildEventListener(mListener);
       // Log.d("AeD","Constructor key "+key);
        mDataSnapshots = new ArrayList<>();


    }



    static class ViewHolder{
        TextView greet;
        TextView fullName;
        TextView mobileNo;
        TextView loanAmt;
        TextView payDay;
        TextView status;
        TextView due_date;

        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public PurposeData getItem(int i) {
        DataSnapshot snapshot = mDataSnapshots.get(i);
        return snapshot.getValue(PurposeData.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.status_data,viewGroup,false);
            final ViewHolder holder = new ViewHolder();
            holder.greet = convertView.findViewById(R.id.greetingDisp);
            holder.fullName = convertView.findViewById(R.id.fullname);
            holder.mobileNo = convertView.findViewById(R.id.phoneNo);
            holder.loanAmt = convertView.findViewById(R.id.loanAmount);
            holder.payDay = convertView.findViewById(R.id.payDay);
            holder.status = convertView.findViewById(R.id.status);
            holder.due_date = convertView.findViewById(R.id.payDate);
            holder.params = (LinearLayout.LayoutParams)holder.fullName.getLayoutParams();
            convertView.setTag(holder);
        }

        //GOTO getItem method and make it return the class you are init it to
        final PurposeData pd = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();


        String full_name = pd.getFname()+" "+ pd.getMname()+"  "+ pd.getLname();
        holder.fullName.setText(full_name);

        String mobile_num = pd.getPhone();
        holder.mobileNo.setText(mobile_num);

        String loan_amount = pd.getAmount()+"0";
        holder.loanAmt.setText(loan_amount);

        String days = pd.getDays()+" Days";
        holder.payDay.setText(days);

        String status  = pd.getstatus();
        if(status==null){
            holder.status.setText("Pending");
        }
        holder.status.setText(status);

        String dueDate = pd.getdue_date();

        //SETTING  CURRENT DATE
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Date resultdate = new Date(calendar.getTimeInMillis());
        String date = sdf.format(resultdate);
        //String date = String.valueOf(year)+"/"+String.valueOf(month)+"/"+ String.valueOf(day);
        if(date.equals(dueDate)){
            //DATE IS DUE FOR PAYMENT
            holder.status.setTextColor(Color.RED);
            holder.due_date.setTextColor(Color.RED);


        }
        if(dueDate==null){
            holder.due_date.setText("Pending");
        }
        Log.d("AeD","Server date is "+date);
        holder.due_date.setText(dueDate);
        getKey();

        //GREET USER
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        //int min = c.get(Calendar.MINUTE);

        String greet = null;

        if(hours>=1 && hours<12){
            greet = "Good Morning " + pd.getFname();
            holder.greet.setText(greet);
        }else if(hours>=12 && hours<16){
            greet = "Good Afternoon " + pd.getFname();
            holder.greet.setText(greet);
        }else if(hours>=16 && hours<21){
            greet = "Good Evening " + pd.getFname();
            holder.greet.setText(greet);
        }else if(hours>=21 && hours<24){
            greet = "Good Night " + pd.getFname();
            holder.greet.setText(greet);
        }


        return convertView;
    }


    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }

    public void getKey(){
        //prefs = getSharedPreferences(loan_step1.PREF_VARS, MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();
        Log.d("AeD","Method key "+key);
    }


}
