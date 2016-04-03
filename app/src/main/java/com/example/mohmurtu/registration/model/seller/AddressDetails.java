package com.example.mohmurtu.registration.model.seller;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 12/27/2015.
 */
public class AddressDetails implements Parcelable {

    private String address, city, state, pinCode ;

    public AddressDetails(){

    }

    private AddressDetails(Parcel in){
        this.address = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.pinCode = in.readString();
    }

    public static final Parcelable.Creator<AddressDetails> CREATOR = new Parcelable.Creator<AddressDetails>(){
        @Override
        public AddressDetails createFromParcel(Parcel source) {
            return new AddressDetails(source);
        }

        @Override
        public AddressDetails[] newArray(int size) {
            return new AddressDetails[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.pinCode);
    }
}
