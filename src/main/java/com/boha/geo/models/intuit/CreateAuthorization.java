package com.boha.geo.models.intuit;

public class CreateAuthorization {
    private String amount;
    private String currency;
    private String capture;
    private String token;


    // Getter Methods

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCapture() {
        return capture;
    }

    public String getToken() {
        return token;
    }

    // Setter Methods

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCapture(String capture) {
        this.capture = capture;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
