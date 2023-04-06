package com.boha.geo.models.intuit;

public class AuthorizationResponse {
    private String created;
    private String status;
    private String amount;
    private String currency;
    private String token;
    private boolean capture;
    private String id;
    private String authCode;


    // Getter Methods

    public String getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getToken() {
        return token;
    }

    public boolean getCapture() {
        return capture;
    }

    public String getId() {
        return id;
    }

    public String getAuthCode() {
        return authCode;
    }

    // Setter Methods

    public void setCreated(String created) {
        this.created = created;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
