package com.foxyawn.onu;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sangjun on 2017-09-12.
 */

@IgnoreExtraProperties
public class User {
    public String email;
    public String password;
    public String name;
    public String tel;
    public String place;
    public Info info;

    public User() {

    }

    public User(String email, String password, String name, String tel, String place, Info info) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.place = place;
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("password", password);
        result.put("name", name);
        result.put("tel", tel);
        result.put("place", place);
        result.put("info", info.toMap());
        return result;
    }
}
