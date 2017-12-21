package com.adfinancegh.aedmoneylending;

/**
 * Created by REUBEN on 9/11/2017.
 */

public class PersonalData {
    private String Alternate_Phone_Number;
    private String Gender;
    private String Date_Of_Birth;
    private String Id_Type;
    private String Id_Number;
    private String location;
    private String Post_Address;
    private String length_of_Res;
    private String mari_stat;
    private String Company_Name;
    private String jobTitle;
    private String wrkPeriod;
    private String salary;

    public String getAlternate_Phone_Number() {
        return Alternate_Phone_Number;
    }

    public String getGender() {
        return Gender;
    }

    public String getDate_Of_Birth() {
        return Date_Of_Birth;
    }

    public String getId_Type() {
        return Id_Type;
    }

    public String getId_Number() {
        return Id_Number;
    }

    public String getLocation() {
        return location;
    }

    public String getPost_Address() {
        return Post_Address;
    }

    public String getLength_of_Res() {
        return length_of_Res;
    }

    public String getMari_stat() {
        return mari_stat;
    }

    public String getCompany_Name() {
        return Company_Name;
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

    public void setAlternate_Phone_Number(String alternate_Phone_Number) {
        Alternate_Phone_Number = alternate_Phone_Number;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setDate_Of_Birth(String date_Of_Birth) {
        Date_Of_Birth = date_Of_Birth;
    }

    public void setId_Type(String id_Type) {
        Id_Type = id_Type;
    }

    public void setId_Number(String id_Number) {
        Id_Number = id_Number;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPost_Address(String post_Address) {
        Post_Address = post_Address;
    }

    public void setLength_of_Res(String length_of_Res) {
        this.length_of_Res = length_of_Res;
    }

    public void setMari_stat(String mari_stat) {
        this.mari_stat = mari_stat;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setWrkPeriod(String wrkPeriod) {
        this.wrkPeriod = wrkPeriod;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public PersonalData(String alternate_Phone_Number, String gender, String date_Of_Birth, String id_Type, String id_Number, String location, String post_Address, String length_of_Res, String mari_stat, String company_Name, String jobTitle, String wrkPeriod, String salary) {

        Alternate_Phone_Number = alternate_Phone_Number;
        Date_Of_Birth = date_Of_Birth;
        Gender = gender;

        Id_Type = id_Type;
        Id_Number = id_Number;
        this.location = location;
        Post_Address = post_Address;
        this.length_of_Res = length_of_Res;
        this.mari_stat = mari_stat;
        Company_Name = company_Name;
        this.jobTitle = jobTitle;
        this.wrkPeriod = wrkPeriod;
        this.salary = salary;
    }

    public PersonalData() {

    }


}
