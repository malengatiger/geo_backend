package com.boha.geo.models.intuit;

import java.util.ArrayList;
import java.util.List;

public class Line {
    List< SalesReceipt > receipts = new ArrayList<>();

    public List<SalesReceipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<SalesReceipt> receipts) {
        this.receipts = receipts;
    }
}
