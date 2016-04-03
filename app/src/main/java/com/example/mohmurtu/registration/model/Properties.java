package com.example.mohmurtu.registration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohmurtu on 11/15/2015.
 */
public class Properties{

    boolean issuccess ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    ArrayList<Property> propAndValues ;

    public ArrayList<Property> getPropAndValues() {
        return propAndValues;
    }

    public void setPropAndValues(ArrayList<Property> propAndValues) {
        this.propAndValues = propAndValues;
    }
}
