package com.boha.geo.models.intuit;

public class CaptureDetail {
    private String created;
    private String amount;
    Context ContextObject;


    // Getter Methods

    public String getCreated() {
        return created;
    }

    public String getAmount() {
        return amount;
    }

    public Context getContext() {
        return ContextObject;
    }

    // Setter Methods

    public void setCreated(String created) {
        this.created = created;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setContext(Context contextObject) {
        this.ContextObject = contextObject;
    }
}

