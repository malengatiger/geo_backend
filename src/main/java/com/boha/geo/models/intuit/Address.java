package com.boha.geo.models.intuit;

import lombok.Data;

@Data
public class Address {
    private String region;
    private String postalCode;
    private String streetAddress;
    private String country;
    private String city;


    // Getter Methods

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    // Setter Methods

    public void setRegion(String region) {
        this.region = region;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
