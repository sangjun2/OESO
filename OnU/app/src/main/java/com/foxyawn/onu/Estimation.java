package com.foxyawn.onu;

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

    Estimation (){
        this.price = "";
        this.placetype = "";
        this.district = "";
        this.address = "";
        this.person = "";
        this.date = "";
    }

    public void setPrice(String price) {
        this.price = price;
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
}
