package com.foxyawn.onu;

import android.widget.ImageView;

public class GridItem {
    String name=" ";
    String address=" ";
    String introduce=" ";
    String price=" ";
    int resId=0;
    String providerUid=" ";
    Info info=new Info();
    ImageView sonImageView;

    public GridItem(String name, String address, String introduce, String price, int resId, String providerUid,Info info){
        this.name = name;
        this.address=address;
        this.introduce=introduce;
        this.price=price;
        this.resId= resId;
        this.providerUid=providerUid;
        this.info = info;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public int getResId() {
        return resId;
    }

    public String getAddress() {
        return address;
    }


    public void setSonImageView(ImageView sonImageView) {
        this.sonImageView = sonImageView;
    }

    public ImageView getSonImageView() {
        return sonImageView;
    }
}

