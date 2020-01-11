package com.example.epaycocheckout.entities;

public class Checkout {

    //required
    private String invoice;
    private String title;
    private String description;
    private String value;
    private String tax;
    private String taxBase;
    private String currency;
    private String country;

    //optional
    private String docType;
    private String docNumber;
    private String name;
    private String lastName;
    private String email;
    private String phoneCode = "+57";
    private String phoneNumber;
    private String customerId;
    private String tokenCard;
    private String urlResponse;
    private String urlConfirmation;

    private int form;
    private String cashDateEnd;

    public Checkout() {
        // empty constructor
    }

    public Checkout(String invoice, String title, String description, String value, String tax,
                    String taxBase, String currency, String country, String docType, String docNumber,
                    String name, String lastName, String email, String phoneCode, String phoneNumber,
                    String customerId, String tokenCard, String urlResponse, String urlConfirmation,
                    int form, String cashDateEnd) {
        this.invoice = invoice;
        this.title = title;
        this.description = description;
        this.value = value;
        this.tax = tax;
        this.taxBase = taxBase;
        this.currency = currency;
        this.country = country;
        this.docType = docType;
        this.docNumber = docNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneCode = phoneCode;
        this.phoneNumber = phoneNumber;
        this.customerId = customerId;
        this.tokenCard = tokenCard;
        this.urlResponse = urlResponse;
        this.urlConfirmation = urlConfirmation;
        this.form = form;
        this.cashDateEnd = cashDateEnd;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxBase() {
        return taxBase;
    }

    public void setTaxBase(String taxBase) {
        this.taxBase = taxBase;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTokenCard() {
        return tokenCard;
    }

    public void setTokenCard(String tokenCard) {
        this.tokenCard = tokenCard;
    }

    public String getUrlResponse() {
        return urlResponse;
    }

    public void setUrlResponse(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public String getUrlConfirmation() {
        return urlConfirmation;
    }

    public void setUrlConfirmation(String urlConfirmation) {
        this.urlConfirmation = urlConfirmation;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public String getCashDateEnd() {
        return cashDateEnd;
    }

    public void setCashDateEnd(String cashDateEnd) {
        this.cashDateEnd = cashDateEnd;
    }
}
