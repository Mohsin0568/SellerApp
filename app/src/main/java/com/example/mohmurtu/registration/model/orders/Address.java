package com.example.mohmurtu.registration.model.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 2/21/2016.
 */
public class Address implements Parcelable{

    private long addressId;
    private String uName, address, landmark, phone, city, pinCode ;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Address(){

    }

    private Address(Parcel in){
        this.addressId = in.readLong();
        this.uName = in.readString();
        this.address = in.readString();
        this.landmark = in.readString();
        this.phone = in.readString();
        this.city = in.readString();
        this.pinCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.addressId);
        dest.writeString(this.uName);
        dest.writeString(this.address);
        dest.writeString(this.landmark);
        dest.writeString(this.phone);
        dest.writeString(this.city);
        dest.writeString(this.pinCode);
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {

        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
