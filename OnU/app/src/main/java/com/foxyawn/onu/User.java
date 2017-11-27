package com.foxyawn.onu;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
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

    public User() {

    }

    public User(String email, String password, String name, String tel, String place) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.place = place;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("email", email);
        result.put("password", password);
        result.put("name", name);
        result.put("tel", tel);
        result.put("place", place);

        return result;
    }
}
