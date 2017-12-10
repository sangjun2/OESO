package com.foxyawn.onu;

public class GridItem {
    String placeType;
    String district;
    String person;
    String address;
    int resId;
    String providerUid;

    public GridItem(String placeType, String district, String person, String address, int resId, String providerUid){
        this.placeType = placeType;
        this.district=district;
        this.person=person;
        this.address=address;
        this.resId= resId;
        this.providerUid=providerUid;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getProviderUid() {
        return providerUid;
    }

    public void setProviderUid(String providerUid) {
        this.providerUid = providerUid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public int getResId() {
        return resId;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public String getPerson() {
        return person;
    }

    public String getPlaceType() {
        return placeType;
    }

}

