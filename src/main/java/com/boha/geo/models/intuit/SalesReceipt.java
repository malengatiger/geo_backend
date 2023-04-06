package com.boha.geo.models.intuit;

import java.util.ArrayList;

public class SalesReceipt {
    CustomerRef customerRef;
    CreditCardPayment creditCardPayment;
    private String txnSource;


    // Getter Methods

    public CustomerRef getCustomerRef() {
        return customerRef;
    }

    public CreditCardPayment getCreditCardPayment() {
        return creditCardPayment;
    }

    public String getTxnSource() {
        return txnSource;
    }

    // Setter Methods

    public void setCustomerRef(CustomerRef customerRef) {
        this.customerRef = customerRef;
    }

    public void setCreditCardPayment(CreditCardPayment creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }

    public void setTxnSource(String txnSource) {
        this.txnSource = txnSource;
    }
}

