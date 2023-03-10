package com.example.merabills.model;

import java.math.BigDecimal;

public class Transaction {
    private final String title;
    private final String amount;
    private final String bankName;
    private final String bankTransaction;

    public Transaction(String title, String amount, String bankName, String bankTransaction) {
        this.title = title;
        this.amount = amount;
        this.bankName = bankName;
        this.bankTransaction = bankTransaction;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getAmounts(){
        return new BigDecimal(amount ==null ? "0" : amount );
    }

}
