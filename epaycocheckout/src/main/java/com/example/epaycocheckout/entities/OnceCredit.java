package com.example.epaycocheckout.entities;

import co.epayco.android.models.Card;

public class OnceCredit {

    //Required
    private String docType;
    private String docNumber;
    private String name;
    private String lastName;
    private String email;
    private String country;
    private String phone;
    private String invoice;
    private String tax;
    private String taxBase;
    private String value;
    private String currency;
    private String dues;
    private Card card;

    //Optional
    private String expeditionDate;
    private String departament;
    private String city;
    private String cellphone;
    private String direction;
    private String description;
    private String extra1;
    private String extra2;
    private String extra3;
    private String urlResponse;
    private String urlConfirmation;
    private String confirmationMethod;
    public OnceCredit(){

    }

    public OnceCredit(String docType, String docNumber, String name,
                      String lastName, String email, String country, String phone,
                      String invoice, String tax, String taxBase, String value, String currency,
                      String dues, Card card, String expeditionDate, String departament, String city,
                      String cellphone, String direction, String description, String extra1, String extra2,
                      String extra3, String urlResponse, String urlConfirmation, String confirmationMethod) {
        this.docType = docType;
        this.docNumber = docNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.phone = phone;
        this.invoice = invoice;
        this.tax = tax;
        this.taxBase = taxBase;
        this.value = value;
        this.currency = currency;
        this.dues = dues;
        this.card = card;
        this.expeditionDate = expeditionDate;
        this.departament = departament;
        this.city = city;
        this.cellphone = cellphone;
        this.direction = direction;
        this.description = description;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.extra3 = extra3;
        this.urlResponse = urlResponse;
        this.urlConfirmation = urlConfirmation;
        this.confirmationMethod = confirmationMethod;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDues() {
        return dues;
    }

    public void setDues(String dues) {
        this.dues = dues;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getExpeditionDate() {
        return expeditionDate;
    }

    public void setExpeditionDate(String expeditionDate) {
        this.expeditionDate = expeditionDate;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
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

    public String getConfirmationMethod() {
        return confirmationMethod;
    }

    public void setConfirmationMethod(String confirmationMethod) {
        this.confirmationMethod = confirmationMethod;
    }
}
