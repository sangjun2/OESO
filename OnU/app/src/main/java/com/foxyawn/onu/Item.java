package com.foxyawn.onu;

import java.util.ArrayList;

/**
 * Created by jsb on 2017-11-26.
 */

public class Item {
    public String title;
    public ArrayList<String> childList;

    public Item() {

    }

    public Item(String title, ArrayList<String> childList) {
        this.title = title;
        if(childList == null) {
            this.childList = new ArrayList<>();
        } else {
            this.childList = childList;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<String> childList) {
        this.childList = childList;
    }
}
