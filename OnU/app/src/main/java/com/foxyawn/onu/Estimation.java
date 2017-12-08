package com.foxyawn.onu;

import java.util.ArrayList;

/**
 * Created by jsb on 2017-11-26.
 */

public class Estimation {

    String placetype;
    String district;
    String address;
    String person;
    String date;
    String price;
    ArrayList<String> provider;

    Estimation (){
        this.price = "";
        this.placetype = "";
        this.district = "";
        this.address = "";
        this.person = "";
        this.date = "";
        provider = new ArrayList<String>();
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProvider(ArrayList<String> provider) {
        this.provider = provider;
    }

    public void setPlacetype(String placetype) {
        this.placetype = placetype;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPerson(String person) {
        this.person = person;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public String getPlacetype( ) {
        return placetype;
    }
    public String getDistrict( ) {
        return district;
    }
    public String getAddress() {
        return address;
    }
    public String getPerson( ) {
        return person;
    }
    public String getDate( ) {
        return date;
    }

    public ArrayList<String> getProvider() {
        return provider;
    }
}
