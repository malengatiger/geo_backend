package com.boha.geo.models.intuit;

public class Context {
    private String tax = null;
    private boolean recurring;
    DeviceInfo deviceInfo;


    // Getter Methods

    public String getTax() {
        return tax;
    }

    public boolean getRecurring() {
        return recurring;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    // Setter Methods

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public void setDeviceInfo(DeviceInfo deviceInfoObject) {
        this.deviceInfo = deviceInfoObject;
    }
}
