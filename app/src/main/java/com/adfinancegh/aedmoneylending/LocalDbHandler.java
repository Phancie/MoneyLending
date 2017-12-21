package com.adfinancegh.aedmoneylending;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.adfinancegh.aedmoneylending.R.string.full_name;
import static com.adfinancegh.aedmoneylending.R.string.idType;

/**
 * Created by REUBEN on 9/19/2017.
 */

//TODO DATA IS STORING IN A STRANGE WAY

public class LocalDbHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public static final String TABLE_STEP_ONE = "stepOne";
    public static final String TABLE_STEP_TWO = "stepTwo";
    public static final String TABLE_DETAILS = "Details";
    public static final String TABLE_PURPOSE = "purpose";
    public static final String COLUMN_FIRST_NAME = "fName";
    public static final String COLUMN_MID_NAME = "mName";
    public static final String COLUMN_LAST_NAME = "LName";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE_NO = "PhoneNo";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_ALT_NO = "altNo";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_COMP_NAME = "compName";
    public static final String COLUMN_ID_TYPE = "idType";
    public static final String COLUMN_ID_NO = "idNo";
    public static final String COLUMN_JOB_TITLE = "jobTitle";
    public static final String COLUMN_LENGTH_OF_RES = "lengthOfRes";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_POST_ADDR = "postaddr";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_MARI_STAT = "maritalStatus";
    public static final String COLUMN_WORK_PERIOD = "workPeriod";
//    public static final String COLUMN_ACC_NAME = "accName";
//    public static final String COLUMN_ACC_NO = "accNo";
//    public static final String COLUMN_BANK_NAME = "bankName";
//    public static final String COLUMN_BANK_BRANCH = "bankBranch";
//    public static final String COLUMN_GUARANT_NAME = "guarantName";
//    public static final String COLUMN_GUARANT_NO = "guarantNo";
//    public static final String COLUMN_GUARANT_DOB = "guarantDob";
//    public static final String COLUMN_LOAN_PURPOSE = "loanPurpose";


    public LocalDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        Log.d("AeD", "DB constructor");
    }
    //TODO APPLY STEP ONE CONCEPT TO ALL

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_STEP_ONE + "(" +
                COLUMN_FIRST_NAME + " TEXT," +
                COLUMN_MID_NAME + " TEXT," +
                COLUMN_LAST_NAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PHONE_NO + " TEXT" +
                ");";


        String query1 = "CREATE TABLE " + TABLE_STEP_TWO + "(" +
                COLUMN_DAYS + " TEXT," +
                COLUMN_AMOUNT + " TEXT " +
                ");";


        String query2 = "CREATE TABLE " + TABLE_DETAILS + "(" +
                COLUMN_ALT_NO + " TEXT, " +
                COLUMN_GENDER + " TEXT," +
                COLUMN_DOB + " TEXT, " +
                COLUMN_COMP_NAME + " TEXT, " +
                COLUMN_ID_TYPE + " TEXT, " +
                COLUMN_ID_NO + " TEXT, " +
                COLUMN_JOB_TITLE + " TEXT, " +
                COLUMN_LENGTH_OF_RES + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_POST_ADDR + " TEXT, " +
                COLUMN_SALARY + " TEXT, " +
                COLUMN_WORK_PERIOD + " TEXT, " +
                COLUMN_MARI_STAT + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(query);
        Log.d("AeD", "Table 1 created");
        sqLiteDatabase.execSQL(query1);
        Log.d("AeD", "Table 2 created");
        sqLiteDatabase.execSQL(query2);
        Log.d("AeD", "Table 3 created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_ONE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_TWO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PURPOSE);
        onCreate(sqLiteDatabase);
    }

    public Long addStepOneData(stepOneData sod) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, sod.getFname());
        values.put(COLUMN_MID_NAME, sod.getMname());
        values.put(COLUMN_LAST_NAME, sod.getLname());
        values.put(COLUMN_EMAIL, sod.getEmail());
        values.put(COLUMN_PHONE_NO, sod.getPhone());
        SQLiteDatabase db = getWritableDatabase();
        long rowInserted = db.insert(TABLE_STEP_ONE, null, values);


        if (rowInserted != -1)
            Log.d("AeD", "Data storage successful id " + rowInserted);
        else
            Log.d("AeD", "Data storage error");
        db.close();
        return rowInserted;
    }

    public Long addStepTwoData(StepTwoData std) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAYS, std.getDays());
        values.put(COLUMN_AMOUNT, std.getAmount());
        SQLiteDatabase db = getWritableDatabase();
        long rowInserted = db.insert(TABLE_STEP_TWO, null, values);
        if (rowInserted != -1)
            Log.d("AeD", "Data storage step 2 successful id " + rowInserted);
        else
            Log.d("AeD", "Data storage step 2 error");
        db.close();
        return rowInserted;
    }

    public Long addDetailsData(PersonalData pd) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALT_NO, pd.getAlternate_Phone_Number());
        values.put(COLUMN_GENDER, pd.getGender());
        values.put(COLUMN_DOB, pd.getDate_Of_Birth());
        values.put(COLUMN_COMP_NAME, pd.getCompany_Name());
        values.put(COLUMN_ID_TYPE, pd.getId_Type());
        values.put(COLUMN_ID_NO, pd.getId_Number());
        values.put(COLUMN_JOB_TITLE, pd.getJobTitle());
        values.put(COLUMN_LENGTH_OF_RES, pd.getLength_of_Res());
        values.put(COLUMN_LOCATION, pd.getLocation());
        values.put(COLUMN_POST_ADDR, pd.getPost_Address());
        values.put(COLUMN_SALARY, pd.getSalary());
        values.put(COLUMN_WORK_PERIOD, pd.getWrkPeriod());
        values.put(COLUMN_MARI_STAT, pd.getMari_stat());

        SQLiteDatabase db = getWritableDatabase();
        long rowInserted = db.insert(TABLE_DETAILS, null, values);
        if (rowInserted != -1)
            Log.d("AeD", "Data storage Details successful id " + rowInserted);
        else
            Log.d("AeD", "Data storage Details error");
        db.close();
        return rowInserted;
    }


//    public void addPurposeData(PurposeData pd) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_LOAN_PURPOSE, pd.getLoan_Purpose());
//        values.put(COLUMN_ACC_NAME, pd.getAccount_Name());
//        values.put(COLUMN_ACC_NO, pd.getAccount_Number());
//        values.put(COLUMN_BANK_NAME, pd.getBank());
//        values.put(COLUMN_BANK_BRANCH, pd.getBankBranch());
//        values.put(COLUMN_GUARANT_NAME, pd.getGuarantor_Full_Name());
//        values.put(COLUMN_GUARANT_DOB, pd.getGuarantor_Date_Of_Birth());
//        values.put(COLUMN_GUARANT_NO, pd.getGuarantor_Phone_Number());
//
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert(TABLE_PURPOSE, null, values);
//        //db.close();
//    }

    public void deleteStepOne() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_STEP_ONE + ";");
    }

    public void deleteStepTwo() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_STEP_TWO + ";");
    }

    public void deleteDetails() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_DETAILS + ";");
    }

//    public void deletePurpose() {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DROP TABLE " + TABLE_PURPOSE + ";");
//    }

    public List getStepOneData() {
        Log.d("AeD", "In addStepOneDataRet");
        List dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STEP_ONE;

        Cursor c = db.rawQuery(query, null);


        if (c.moveToFirst()) {
            //Loop through the table rows
            do {
                stepOneData sod = new stepOneData();
                sod.setFname(c.getString(0));
                sod.setMname(c.getString(1));
                sod.setLname(c.getString(2));
                sod.setEmail(c.getString(3));
                sod.setPhone(c.getString(4));

                dataList.add(sod);
            } while (c.moveToNext());
        }

        //Log.d("AeD","DB full name "+dbString);
        db.close();

        return dataList;
    }

    public List getStepTwoData() {
        List dataList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STEP_TWO;

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                StepTwoData std = new StepTwoData();
                std.setDays(c.getString(0));
                std.setAmount(c.getString(1));
                dataList.add(std);
            }

            while (c.moveToNext()) ;
        }
        db.close();
        return dataList;
    }

    public List getTableDetailsData() {
        List dataList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DETAILS;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToFirst()) {
            do {
                PersonalData pd = new PersonalData();
                pd.setAlternate_Phone_Number(c.getString(0));
                pd.setGender(c.getString(1));
                pd.setDate_Of_Birth(c.getString(2));
                pd.setCompany_Name(c.getString(3));
                pd.setId_Type(c.getString(4));
                pd.setId_Number(c.getString(5));
                pd.setJobTitle(c.getString(6));
                pd.setLength_of_Res(c.getString(7));
                pd.setLocation(c.getString(8));
                pd.setPost_Address(c.getString(9));
                pd.setSalary(c.getString(10));
                pd.setWrkPeriod(c.getString(11));
                pd.setMari_stat(c.getString(12));
                dataList.add(pd);
            }while (c.moveToNext());
        }
        db.close();
        return dataList;
    }

//    public String getPurposeData() {
//        String dbString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_PURPOSE;
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex("amount")) != null) {
//                dbString += c.getString(c.getColumnIndex("accName"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("accNo"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("bankName"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("bankBranch"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("guarantName"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("guarantNo"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("guarantDob"));
//                dbString += "\n";
//                dbString += c.getString(c.getColumnIndex("loanPurpose"));
//                dbString += "\n";
//            }
//        }
//        //db.close();
//        return dbString;
//    }
}
