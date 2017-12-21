package com.adfinancegh.aedmoneylending;

/**
 * Created by REUBEN on 9/11/2017.
 */

public class StepTwoData {
    private String amount;
    private String days;

    public StepTwoData(String amount, String days) {
        this.amount = amount;
        this.days = days;
    }

    public StepTwoData() {
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAmount() {

        return amount;
    }

    public String getDays() {
        return days;
    }
}
