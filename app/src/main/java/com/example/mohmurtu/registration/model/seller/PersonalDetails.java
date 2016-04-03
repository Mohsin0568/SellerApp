package com.example.mohmurtu.registration.model.seller;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 12/27/2015.
 */
public class PersonalDetails implements Parcelable{

    private String sellerId, sellerName, phoneNumber, altPhoneNumber, email, companyName ;

    private PersonalDetails(Parcel in){
        this.sellerId = in.readString();
        this.phoneNumber = in.readString();
        this.altPhoneNumber = in.readString();
        this.email = in.readString();
        this.companyName = in.readString();
    }

    public PersonalDetails(){

    }

    public static final Parcelable.Creator<PersonalDetails> CREATOR = new Parcelable.Creator<PersonalDetails>(){
        @Override
        public PersonalDetails createFromParcel(Parcel source) {
            return new PersonalDetails(source);
        }

        @Override
        public PersonalDetails[] newArray(int size) {
            return new PersonalDetails[size];
        }
    };

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAltPhoneNumber() {
        return altPhoneNumber;
    }

    public void setAltPhoneNumber(String altPhoneNumber) {
        this.altPhoneNumber = altPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sellerId);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.altPhoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.companyName);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
