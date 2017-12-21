package com.adfinancegh.aedmoneylending;

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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by REUBEN on 10/16/2017.
 */

public class TabOtherDetails extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<PurposeData,ViewPagerViewHolder> mAdapter;
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private LinearLayoutManager manager;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_details_tab, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.stat_tab_recycler);
        mRecyclerView.setHasFixedSize(true);
        mReference = FirebaseDatabase.getInstance().getReference().child(MainActivity.userName).child("User_Details");
        manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        Log.d("AeD", "In other fragment");
        setupAdapter();
        mAdapter.notifyDataSetChanged();
        return rootView;
    }


    private void setupAdapter() {
        //TODO MAYBE CREATE LAYOUT RES FILE FOR DATA CARD

        //DatabaseReference tempRef = mReference.child("User_Details");
        mAdapter = new FirebaseRecyclerAdapter<PurposeData, ViewPagerViewHolder>(PurposeData.class,R.layout.card_other_details,ViewPagerViewHolder.class,
                mReference) {
            @Override
            protected void populateViewHolder(ViewPagerViewHolder viewHolder, PurposeData pd, int position) {
                viewHolder.loanPurp.setText(pd.getLoan_Purpose());
                viewHolder.bank.setText(pd.getBank());
                viewHolder.accNo.setText(pd.getAccount_Number());
                viewHolder.accName.setText(pd.getAccount_Name());
                viewHolder.bankBranch.setText(pd.getBankBranch());
                viewHolder.gFullName.setText(pd.getGuarantor_Full_Name());
                viewHolder.gTelNo.setText(pd.getGuarantor_Phone_Number());
                viewHolder.gDob.setText(pd.getGuarantor_Date_Of_Birth());
                viewHolder.stepFourEdits.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity().getBaseContext(),loan_purpose.class);
                        i.putExtra("UpdatingDetails","updateInfo");
                        getActivity().startActivity(i);
                    }
                });
            }


        };

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

        TextView loanPurp;
        TextView bank;
        TextView accNo;
        TextView accName;
        TextView bankBranch;
        TextView gFullName;
        TextView gDob;
        TextView gTelNo;
        TextView stepFourEdits;

        public ViewPagerViewHolder(View itemView) {
            super(itemView);
            loanPurp = itemView.findViewById(R.id.loanPurp);
            bank = itemView.findViewById(R.id.bank);
            accName = itemView.findViewById(R.id.accName);
            accNo = itemView.findViewById(R.id.accNo);
            bankBranch = itemView.findViewById(R.id.bankBranch);
            gFullName = itemView.findViewById(R.id.gName);
            gDob = itemView.findViewById(R.id.gDob);
            gTelNo = itemView.findViewById(R.id.gPhone);
            stepFourEdits = itemView.findViewById(R.id.step_four_edit);
        }

    }

}
