package com.example.epaycocheckout.entities;

public class BankCode {
    private String bankName;
    private int bankCode;

    public BankCode() {
        //empty constructor
    }

    public BankCode(int bankCode, String bankName) {
        this.bankName = bankName;
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getBankCode() {
        return bankCode;
    }

    public void setBankCode(int bankCode) {
        this.bankCode = bankCode;
    }
}
