package com.adfinancegh.aedmoneylending;

/**
 * Created by REUBEN on 11/8/2017.
 */

public class LoanHistory {
    String amount;
    String date;
    String status;

    public LoanHistory() {
    }

    public LoanHistory(String amount, String date, String status) {

        this.amount = amount;
        this.date = date;
        this.status = status;
    }


    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}


