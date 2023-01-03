package com.boha.geo.monitor.data;

import lombok.Data;

@Data
public class CustomErrorResponse {
    int statusCode;
    String message, date;

    public CustomErrorResponse(int statusCode, String message, String date) {
        this.statusCode = statusCode;
        this.message = message;
        this.date = date;
    }

    public CustomErrorResponse() {
    }
}
