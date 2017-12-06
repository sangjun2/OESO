package com.foxyawn.onu;

/**
 * Created by jsb on 2017-11-26.
 */

public class Estimation {

    String placetype;
    String district;
    String place;
    String number;
    String date;
    String price;

    Estimation (){
        this.price = "";
        this.placetype = "";
        this.district = "";
        this.place = "";
        this.number = "";
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
    public void setPlace(String place) {
        this.place = place;
    }
    public void setNumber(String number) {
        this.number = number;
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
    public String getPlace() {
        return place;
    }
    public String getNumber( ) {
        return number;
    }
    public String getDate( ) {
        return date;
    }
}
