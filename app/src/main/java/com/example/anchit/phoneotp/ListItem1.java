package com.example.anchit.phoneotp;

/**
 * Created by Aayush on 8/12/2017.
 */

public class ListItem1 {
    String state, city, district, hospital, address, pincode, phone, website;

    public ListItem1(String state, String city, String district, String hospital, String address, String pincode, String phone, String website) {
        this.state = state;
        this.city = city;
        this.district = district;
        this.hospital = hospital;
        this.address = address;
        this.pincode = pincode;
        this.phone = phone;
        this.website = website;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getHospital() {
        return hospital;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}
