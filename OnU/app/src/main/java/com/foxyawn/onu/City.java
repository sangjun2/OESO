package com.foxyawn.onu;

/**
 * Created by Sangjun on 2017-09-07.
 */

public class City {
    private String name;
    private District[] districts;

    public City() {
        this.name = null;
        this.districts = null;
    }

    public City(String name, District[] districts) {
        this.name = name;
        this.districts = districts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District[] getDistricts() {
        return districts;
    }

    public void setDistricts(District[] districts) {
        this.districts = districts;
    }
}
