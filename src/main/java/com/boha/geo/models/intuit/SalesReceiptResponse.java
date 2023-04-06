package com.boha.geo.models.intuit;

import java.util.ArrayList;
import java.util.List;

public class SalesReceiptResponse {
    SalesReceipt SalesReceiptObject;
    private String time;


    // Getter Methods

    public SalesReceipt getSalesReceipt() {
        return SalesReceiptObject;
    }

    public String getTime() {
        return time;
    }

    // Setter Methods

    public void setSalesReceipt(SalesReceipt SalesReceiptObject) {
        this.SalesReceiptObject = SalesReceiptObject;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


