package com.example.mohmurtu.registration.model.seller;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 12/27/2015.
 */
public class VATDetails implements Parcelable{

    private String panNumber, vatNumber, cstNumber ;

    public VATDetails(){

    }

    private VATDetails(Parcel in){
        this.panNumber = in.readString();
        this.vatNumber = in.readString();
        this.cstNumber = in.readString();
    }

    public static final Parcelable.Creator<VATDetails> CREATOR = new Parcelable.Creator<VATDetails>(){
        @Override
        public VATDetails createFromParcel(Parcel source) {
            return new VATDetails(source);
        }

        @Override
        public VATDetails[] newArray(int size) {
            return new VATDetails[size];
        }
    };

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getCstNumber() {
        return cstNumber;
    }

    public void setCstNumber(String cstNumber) {
        this.cstNumber = cstNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.panNumber);
        dest.writeString(this.vatNumber);
        dest.writeString(this.cstNumber);
    }
}
