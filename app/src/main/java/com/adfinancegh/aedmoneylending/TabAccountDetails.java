package com.adfinancegh.aedmoneylending;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by REUBEN on 10/16/2017.
 */

public class TabAccountDetails extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<PurposeData,ViewPagerViewHolder> mAdapter;
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog mDialog = null;
    private String paymentStat;
    private String salary;
    private LinearLayoutManager manager;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_main_tab, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.stat_tab_recycler);
        mRecyclerView.setHasFixedSize(true);
        mReference = FirebaseDatabase.getInstance().getReference().child(MainActivity.userName).child("User_Details");
        manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        //mDialog = ProgressDialog.show(getActivity(),"","Please wait...",true);
        mRecyclerView.setAdapter(mAdapter);
        setupAdapter();
        mAdapter.notifyDataSetChanged();
        return rootView;


    }

    private void setupAdapter() {
        //TODO MAYBE CREATE LAYOUT RES FILE FOR DATA CARD


        //DatabaseReference tempRef = mReference.child("User_Details");
        mAdapter = new FirebaseRecyclerAdapter<PurposeData, ViewPagerViewHolder>(PurposeData.class,R.layout.card_view_display,ViewPagerViewHolder.class,
                mReference) {
            @Override
            protected void populateViewHolder(ViewPagerViewHolder viewHolder, PurposeData pd, int position) {
                viewHolder.fullName.setText(pd.getFname()+" "+pd.getMname()+" "+pd.getLname());
                viewHolder.mobileNo.setText(pd.getPhone());
                viewHolder.loanAmt.setText(pd.getAmount());
                Log.d("AeD","Actual amt is "+pd.getActual_amount());
                viewHolder.payDay.setText(pd.getDays());
                viewHolder.status.setText(pd.getstatus());
                viewHolder.due_date.setText(pd.getdue_date());

                Log.d("AeD", "populateViewHolder called");
                //USING THIS VARIABLE TO CHECK IF PAYMENT HAS BEEN MADE BEFORE
                paymentStat = pd.getFirst_timer();
                //GETTING SALARY FOR SUBSEQUENT LOAN CALCULATION
                salary = pd.getSalary();

                //GET NEW LOAN CLICKED
                viewHolder.mNewLoan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("AeD","New Loan Clicked");
                        Intent i = new Intent(getActivity().getBaseContext(),loan_step2.class);
                        i.putExtra("CustomerStat","re-applying");
                        i.putExtra("paymentStat",paymentStat);
                        i.putExtra("salary",salary);
                        getActivity().startActivity(i);
                    }
                });

                //GREET USER
                Date dt = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(dt);
                int hours = c.get(Calendar.HOUR_OF_DAY);
                //int min = c.get(Calendar.MINUTE);

                String greet = null;

                if(hours>=1 && hours<12){
                    greet = "Good Morning " + pd.getFname();
                    viewHolder.greet.setText(greet);
                }else if(hours>=12 && hours<16){
                    greet = "Good Afternoon " + pd.getFname();
                    viewHolder.greet.setText(greet);
                }else if(hours>=16 && hours<21){
                    greet = "Good Evening " + pd.getFname();
                    viewHolder.greet.setText(greet);
                }else if(hours>=21 && hours<24){
                    greet = "Good Night " + pd.getFname();
                    viewHolder.greet.setText(greet);
                }
            }


        };
       // mDialog.dismiss();
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mRecyclerView.scrollToPosition(positionStart);
                    Log.d("AeD", "Item range called");
                }
            }


        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public static class ViewPagerViewHolder extends RecyclerView.ViewHolder{

        TextView greet;
        TextView fullName;
        TextView mobileNo;
        TextView loanAmt;
        TextView payDay;
        TextView status;
        TextView due_date;
        TextView mNewLoan;

        public ViewPagerViewHolder(View itemView) {
            super(itemView);
            greet = itemView.findViewById(R.id.greetingDisp);
            fullName = itemView.findViewById(R.id.fullname);
            mobileNo = itemView.findViewById(R.id.phoneNo);
            loanAmt = itemView.findViewById(R.id.loanAmount);
            payDay = itemView.findViewById(R.id.payDay);
            status = itemView.findViewById(R.id.status);
            due_date = itemView.findViewById(R.id.payDate);
            mNewLoan = itemView.findViewById(R.id.new_loan);
        }


    }


}


