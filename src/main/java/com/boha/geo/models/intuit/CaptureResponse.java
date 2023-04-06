package com.boha.geo.models.intuit;

public class CaptureResponse {
    private String created;
    private String status;
    private String amount;
    private String currency;
    Card card;
    private boolean capture;
    private String id;
    Context context;
    private String authCode;
    CaptureDetail captureDetail;


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

    public Card getCard() {
        return card;
    }

    public boolean getCapture() {
        return capture;
    }

    public String getId() {
        return id;
    }

    public Context getContext() {
        return context;
    }

    public String getAuthCode() {
        return authCode;
    }

    public CaptureDetail getCaptureDetail() {
        return captureDetail;
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

    public void setCard(Card cardObject) {
        this.card = cardObject;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setCaptureDetail(CaptureDetail captureDetail) {
        this.captureDetail = captureDetail;
    }
}


    // Getter Methods



    // Setter Methods



