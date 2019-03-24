package com.example.imt3673_lab_1;

/*
*   This is a class made to help store data for the transactions
*   and to have methods to access the data of a particular transaction
* */


public class TransactionsData {
    //The data
    private String recipient;
    private String date;
    private Integer balanceBefore;
    private Integer balanceAfter;

    //Constructor
    TransactionsData(String r, String d, Integer bb, Integer ba){
        this.recipient = r;
        this.date = d;
        this.balanceBefore = bb;
        this.balanceAfter = ba;
    }
    //Getters
    public String getRecipient(){
        return this.recipient;
    }
    public String getDate(){
        return this.date;
    }
    public Integer getBalanceBefore(){
        return this.balanceBefore;
    }
    public Integer getBalanceAfter(){
        return this.balanceAfter;
    }
    //Compiles the data into a string
    public String getDataString(){
        String s = this.date + ": " + this.recipient + ": " + this.balanceBefore.toString() + ": " + this.balanceAfter.toString();
        return s;
    }
}

