package com.example.mohmurtu.registration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohmurtu on 11/15/2015.
 */
public class Property implements Parcelable{

    String propertyId, propertyName, valueId, valueName, prodPropertyId ;
    List<Value> propValues ;

    private Property(Parcel in){
        this.prodPropertyId = in.readString();
        this.propertyId = in.readString();
        this.valueId = in.readString();
        this.valueName = in.readString();
        this.propertyName = in.readString();

        this.propValues = new ArrayList<Value>();
        in.readList(propValues,null);
    }

    public Property(){

    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prodPropertyId);
        dest.writeString(propertyId);
        dest.writeString(valueId);
        dest.writeString(valueName);
        dest.writeString(propertyName);
        dest.writeList(propValues);
    }

    public String getProdPropertyId() {
        return prodPropertyId;
    }

    public void setProdPropertyId(String prodPropertyId) {
        this.prodPropertyId = prodPropertyId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public List<Value> getPropValues() {
        return propValues;
    }

    public void setPropValues(List<Value> propValues) {
        this.propValues = propValues;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
