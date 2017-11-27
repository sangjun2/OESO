package com.foxyawn.onu;

/**
 * Created by jsb on 2017-11-26.
 */

public class Estimation {

    String placetype;
    String district;
    String number;
    String date;

    Estimation (){
        this.placetype = "";
        this.district = "";
        this.number = "";
        this.date = "";
    }

    public void setPlacetype(String placetype) {
        this.placetype = placetype;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getPlacetype( ) {
        return placetype;
    }
    public String getDistrict( ) {
        return district;
    }
    public String getNumber( ) {
        return number;
    }
    public String getDate( ) {
        return date;
    }
}
