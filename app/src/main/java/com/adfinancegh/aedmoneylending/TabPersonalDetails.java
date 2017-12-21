package com.adfinancegh.aedmoneylending;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class TabPersonalDetails extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<PurposeData,ViewPagerViewHolder> mAdapter;
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private LinearLayoutManager manager;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_details_tab, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.stat_tab_recycler);
        mRecyclerView.setHasFixedSize(true);
        mReference = mReference.child(MainActivity.userName).child("User_Details");
        manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        setupAdapter();
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    private void setupAdapter() {

        //DatabaseReference tempRef = mReference.child("User_Details");
        mAdapter = new FirebaseRecyclerAdapter<PurposeData, ViewPagerViewHolder>(PurposeData.class,R.layout.card_personal_details,ViewPagerViewHolder.class,
                mReference) {
            @Override
            protected void populateViewHolder(ViewPagerViewHolder viewHolder, PurposeData pd, int position) {
                viewHolder.fname.setText(pd.getFname());
                viewHolder.mname.setText(pd.getMname());
                Log.d("AeD","Mid Name "+pd.getMname());
                if(pd.getMname().equals("")){
                    viewHolder.mname.setText(R.string.noname);
                }

                viewHolder.lname.setText(pd.getLname());
                viewHolder.mobileNo.setText(pd.getPhone());
                viewHolder.email.setText(pd.getEmail());
                viewHolder.altNo.setText(pd.getAlternate_Phone_Number());
                //STRANGELY GETALTTERNATE_PHONE_NUMBER IS RETURNING NULL INSTEAD OF ""
                if(pd.getAlternate_Phone_Number()==null){
                    viewHolder.altNo.setText(R.string.nonum);
                }
                viewHolder.gender.setText(pd.getGender());
                viewHolder.dob.setText(pd.getDate_Of_Birth());
                viewHolder.idType.setText(pd.getId_Type());
                viewHolder.idNo.setText(pd.getId_Number());
                viewHolder.location.setText(pd.getLocation());
                viewHolder.postAddr.setText(pd.getPost_Address());
                if(pd.getPost_Address().equals("")){
                    viewHolder.postAddr.setText(R.string.nopost);
                }
                viewHolder.lengthOfRes.setText(pd.getLength_of_Res());
                viewHolder.mariStat.setText(pd.getMari_stat());
                viewHolder.compName.setText(pd.getCompany_Name());
                viewHolder.jobTitle.setText(pd.getJobTitle());
                viewHolder.workPeriod.setText(pd.getWrkPeriod());
                viewHolder.salary.setText(pd.getSalary());

                viewHolder.stepThreeEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity().getBaseContext(),personal_Details.class);
                        i.putExtra("UpdatingDetails","updateInfo");
                        getActivity().startActivity(i);
                    }
                });

                viewHolder.stepOneEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity().getBaseContext(),loan_step1.class);
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

        TextView fname;
        TextView mname;
        TextView lname;
        TextView mobileNo;
        TextView email;
        TextView altNo;
        TextView stepOneEdit;
        TextView gender;
        TextView dob;
        TextView idType;
        TextView idNo;
        TextView location;
        TextView postAddr;
        TextView lengthOfRes;
        TextView mariStat;
        TextView compName;
        TextView jobTitle;
        TextView workPeriod;
        TextView salary;
        TextView stepThreeEdit;
        public ViewPagerViewHolder(View itemView) {
            super(itemView);
            fname = itemView.findViewById(R.id.first_name);
            mname = itemView.findViewById(R.id.middle_name);
            lname = itemView.findViewById(R.id.last_name);
            mobileNo = itemView.findViewById(R.id.mobile_num);
            email = itemView.findViewById(R.id.email);
            altNo = itemView.findViewById(R.id.alt_no);
            stepOneEdit = itemView.findViewById(R.id.step_one_edit);
            gender = itemView.findViewById(R.id.gender);
            //fullName.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            dob = itemView.findViewById(R.id.dob);
            idType = itemView.findViewById(R.id.id_type);
            idNo = itemView.findViewById(R.id.id_no);
            location = itemView.findViewById(R.id.location_no);
            postAddr = itemView.findViewById(R.id.po);
            lengthOfRes = itemView.findViewById(R.id.lengthOfRes);
            mariStat = itemView.findViewById(R.id.marital_stat);
            compName = itemView.findViewById(R.id.compName);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            workPeriod = itemView.findViewById(R.id.work_period);
            salary = itemView.findViewById(R.id.salary);
            stepThreeEdit = itemView.findViewById(R.id.step_three_edit);
        }


    }

}