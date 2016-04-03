package com.example.mohmurtu.registration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mohmurtu on 11/15/2015.
 */
public class Value implements Parcelable{

    String valueId, valueName, propertyName, propertyId ;

    public static final Creator<Value> CREATOR = new Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(valueId);
        dest.writeString(valueName);
        dest.writeString(propertyId);
        dest.writeString(propertyName);

    }

    private Value(Parcel in){

        this.valueId = in.readString();
        this.valueName = in.readString();
        this.propertyId = in.readString();
        this.propertyName = in.readString();

    }

    public Value(){

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
