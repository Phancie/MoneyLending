package com.adfinancegh.aedmoneylending;

/**
 * Created by REUBEN on 9/11/2017.
 */
//FIREBASE MODEL CLASS

public class PurposeData {
    private String fname;
    private String mname;
    private String lname;
    private String phone;
    private String email;
    private String amount;
    private String actual_amount;
    private String days;
    private String alternate_Phone_Number;
    private String gender;
    private String date_Of_Birth;
    private String id_Type;
    private String id_Number;
    private String location;
    private String post_Address;
    private String length_of_Res;
    private String mari_stat;
    private String company_Name;
    private String jobTitle;
    private String wrkPeriod;
    private String salary;
    private String loan_Purpose;
    private String bank;
    private String account_Name;
    private String account_Number;
    private String bankBranch;
    private String guarantor_Full_Name;
    private String guarantor_Date_Of_Birth;
    private String guarantor_Phone_Number;
    private String due_date;
    private String status;
    private String token;
    private String first_timer;

    public String getFirst_timer() {
        return first_timer;
    }


    public String getToken() {
        return token;
    }

    public String getdue_date() {
        return due_date;
    }

    public String getstatus() {
        return status;
    }

    public PurposeData() {
    }


    public String getLoan_Purpose() {
        return loan_Purpose;
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

    public String getAmount() {
        return amount;
    }

    public String getDays() {
        return days;
    }

    public String getAlternate_Phone_Number() {
        return alternate_Phone_Number;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_Of_Birth() {
        return date_Of_Birth;
    }

    public String getId_Type() {
        return id_Type;
    }

    public String getId_Number() {
        return id_Number;
    }

    public String getLocation() {
        return location;
    }

    public String getPost_Address() {
        return post_Address;
    }

    public String getLength_of_Res() {
        return length_of_Res;
    }

    public String getMari_stat() {
        return mari_stat;
    }

    public String getCompany_Name() {
        return company_Name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getWrkPeriod() {
        return wrkPeriod;
    }

    public String getSalary() {
        return salary;
    }

    public String getBank() {
        return bank;
    }

    public String getAccount_Name() {
        return account_Name;
    }

    public String getAccount_Number() {
        return account_Number;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public String getGuarantor_Full_Name() {
        return guarantor_Full_Name;
    }

    public String getGuarantor_Date_Of_Birth() {
        return guarantor_Date_Of_Birth;
    }

    public String getGuarantor_Phone_Number() {
        return guarantor_Phone_Number;
    }

    public String getActual_amount() {
        return actual_amount;
    }

    public PurposeData(String fname, String mname, String lname, String phone, String email,
                       String amount,String actual_amount, String days, String alternate_Phone_Number,
                       String gender, String date_Of_Birth, String id_Type, String id_Number,
                       String location, String post_Address, String length_of_Res,
                       String mari_stat, String company_Name, String jobTitle, String wrkPeriod,
                       String salary, String loan_Purpose, String bank, String account_Name,
                       String account_Number, String bankBranch, String guarantor_Full_Name,
                       String guarantor_Date_Of_Birth, String guarantor_Phone_Number,String token,String status,String due_date,String first_timer) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.amount = amount;
        this.actual_amount = actual_amount;
        this.days = days;
        this.alternate_Phone_Number = alternate_Phone_Number;
        this.gender = gender;
        this.date_Of_Birth = date_Of_Birth;
        this.id_Type = id_Type;
        this.id_Number = id_Number;
        this.location = location;
        this.post_Address = post_Address;
        this.length_of_Res = length_of_Res;
        this.mari_stat = mari_stat;
        this.company_Name = company_Name;
        this.jobTitle = jobTitle;
        this.wrkPeriod = wrkPeriod;
        this.salary = salary;
        this.loan_Purpose = loan_Purpose;
        this.bank = bank;
        this.account_Name = account_Name;
        this.account_Number = account_Number;
        this.bankBranch = bankBranch;
        this.guarantor_Full_Name = guarantor_Full_Name;
        this.guarantor_Date_Of_Birth = guarantor_Date_Of_Birth;
        this.guarantor_Phone_Number = guarantor_Phone_Number;
        this.token = token;
        this.due_date = due_date;
        this.status = status;
        this.first_timer = first_timer;
    }
}
