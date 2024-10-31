package com.example.model;

public class Address {
    private Integer houseNo;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String country;
    private Integer pinCode;

    public Address(Integer houseNo, String addressLine1, String addressLine2, String addressLine3, String city, String state, String country, Integer pinCode) {
        this.houseNo = houseNo;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return houseNo + " "+ addressLine1 + " " + addressLine2 + " " + addressLine3 + " " + city + " " + state + " " + country + " " + pinCode;
    }

    public void setHouseNo(Integer houseNo) {
        this.houseNo = houseNo;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }
}
