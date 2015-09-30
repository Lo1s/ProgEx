package com.knabria.jiris.progex;

/**
 * Created by jiris on 27.09.2015.
 */

// POJO for ContraAccount object from the JSON
public class ContraAccount {

    private int accountNumber;
    private String accountName;
    private int bankCode;

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBankCode() {
        return bankCode;
    }

    public void setBankCode(int bankCode) {
        this.bankCode = bankCode;
    }
}
