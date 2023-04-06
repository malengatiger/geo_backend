package com.boha.geo.models.intuit;

public class BillAddr {
    private String Id;
    private String Line1;
    private String City;
    private String CountrySubDivisionCode;
    private String PostalCode;
    private String Lat;
    private String Long;


    // Getter Methods

    public String getId() {
        return Id;
    }

    public String getLine1() {
        return Line1;
    }

    public String getCity() {
        return City;
    }

    public String getCountrySubDivisionCode() {
        return CountrySubDivisionCode;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getLat() {
        return Lat;
    }

    public String getLong() {
        return Long;
    }

    // Setter Methods

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setLine1(String Line1) {
        this.Line1 = Line1;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public void setCountrySubDivisionCode(String CountrySubDivisionCode) {
        this.CountrySubDivisionCode = CountrySubDivisionCode;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public void setLat(String Lat) {
        this.Lat = Lat;
    }

    public void setLong(String Long) {
        this.Long = Long;
    }
}

