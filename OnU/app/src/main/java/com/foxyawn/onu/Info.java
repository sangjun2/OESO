package com.foxyawn.onu;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsb on 2017-12-08.
 */

public class Info {
    public String address;
    public String around;
    public String etc;
    public String facilities;
    public String introduce;
    public String name;
    public String notice;
    public String purpose;
    public String time;

    public Info() {

    }

    public Info(String address, String around, String etc, String facilities, String introduce, String name, String notice, String purpose, String time) {
        this.address = address;
        this.around = around;
        this.etc = etc;
        this.facilities = facilities;
        this.introduce = introduce;
        this.name = name;
        this.notice = notice;
        this.purpose = purpose;
        this.time = time;
    }
    @Exclude
    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("address", address);
        result.put("around", around);
        result.put("etc", etc);
        result.put("facilities", facilities);
        result.put("introduce", introduce);
        result.put("name", name);
        result.put("notice", notice);
        result.put("purpose", purpose);
        result.put("time", time);
        return result;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAround() {
        return around;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
