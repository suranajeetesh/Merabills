package com.example.merabills.model;

import java.math.BigInteger;

public class Transaction {
    private String title;
    private String amount;
    private String bankName;
    private String bankTransaction;

    public Transaction(String title, String amount, String bankName, String bankTransaction) {
        this.title = title;
        this.amount = amount;
        this.bankName = bankName;
        this.bankTransaction = bankTransaction;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public BigInteger getAmounts(){
        return new BigInteger(amount ==null ? "0" : amount );
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankTransaction() {
        return bankTransaction;
    }

    public void setBankTransaction(String bankTransaction) {
        this.bankTransaction = bankTransaction;
    }
}
