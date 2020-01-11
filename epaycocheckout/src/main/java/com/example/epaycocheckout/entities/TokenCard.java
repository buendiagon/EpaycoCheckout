package com.example.epaycocheckout.entities;

public class TokenCard {
    private String customer_id;
    private String franchise;
    private String mask;
    private String tokenCard;

    public TokenCard() {
        //empty constructor
    }

    public TokenCard(String customer_id, String franchise, String mask, String tokenCard) {
        this.customer_id = customer_id;
        this.franchise = franchise;
        this.mask = mask;
        this.tokenCard = tokenCard;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFranchise() {
        return franchise;
    }

    public void setFranchise(String franchise) {
        this.franchise = franchise;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getTokenCard() {
        return tokenCard;
    }

    public void setTokenCard(String tokenCard) {
        this.tokenCard = tokenCard;
    }
}
