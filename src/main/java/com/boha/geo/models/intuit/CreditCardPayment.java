package com.boha.geo.models.intuit;

public class CreditCardPayment {
    CreditChargeInfo creditChargeInfo;
    CreditChargeResponse creditChargeResponse;


    // Getter Methods

    public CreditChargeInfo getCreditChargeInfo() {
        return creditChargeInfo;
    }

    public CreditChargeResponse getCreditChargeResponse() {
        return creditChargeResponse;
    }

    // Setter Methods

    public void setCreditChargeInfo(CreditChargeInfo creditChargeInfo) {
        this.creditChargeInfo = creditChargeInfo;
    }

    public void setCreditChargeResponse(CreditChargeResponse creditChargeResponse) {
        this.creditChargeResponse = creditChargeResponse;
    }
}
