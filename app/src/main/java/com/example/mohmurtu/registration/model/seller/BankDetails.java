package com.example.mohmurtu.registration.model.seller;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohmurtu on 12/27/2015.
 */
public class BankDetails implements Parcelable {

    private String beneficiaryName, accountNumber, bankName, branch, ifscCode ;

    public BankDetails(){

    }

    private BankDetails(Parcel in){
        this.beneficiaryName = in.readString();
        this.accountNumber = in.readString();
        this.bankName = in.readString();
        this.branch = in.readString();
        this.ifscCode = in.readString();
    }

    public static final Parcelable.Creator<BankDetails> CREATOR = new Parcelable.Creator<BankDetails>(){
        @Override
        public BankDetails createFromParcel(Parcel source) {
            return new BankDetails(source);
        }

        @Override
        public BankDetails[] newArray(int size) {
            return new BankDetails[size];
        }
    };

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(beneficiaryName);
        dest.writeString(accountNumber);
        dest.writeString(bankName);
        dest.writeString(branch);
        dest.writeString(ifscCode);

    }
}
