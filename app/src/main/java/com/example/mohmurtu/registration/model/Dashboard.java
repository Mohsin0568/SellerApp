package com.example.mohmurtu.registration.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 12/16/2015.
 */
public class Dashboard{

    private boolean issuccess ;
    private String type, count ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
