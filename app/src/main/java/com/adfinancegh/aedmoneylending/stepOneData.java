package com.adfinancegh.aedmoneylending;

/**
 * Created by REUBEN on 9/11/2017.
 */

public class stepOneData {
    private String fname;
    private String mname;
    private String lname;
    private String phone;
    private String email;

    public stepOneData() {
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public stepOneData(String fname, String mname, String lname, String phone, String email) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;

    }

    public String getFname() {
        return fname;
    }

    public String getMname() {
        return mname;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }


}
