package com.boha.geo.models.intuit;

public class Card {
    private String expYear;
    private String expMonth;
    Address address;
    private String name;
    private String cvc;
    private String number;


    // Getter Methods

    public String getExpYear() {
        return expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getCvc() {
        return cvc;
    }

    public String getNumber() {
        return number;
    }

    // Setter Methods

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public void setAddress(Address addressObject) {
        this.address = addressObject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
