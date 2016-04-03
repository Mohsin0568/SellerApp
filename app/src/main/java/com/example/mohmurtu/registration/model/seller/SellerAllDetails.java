package com.example.mohmurtu.registration.model.seller;

/**
 * Created by mohmurtu on 12/27/2015.
 */
public class SellerAllDetails {

    private boolean issuccess ;

    private PersonalDetails personal;

    private AddressDetails address ;

    private BankDetails bank ;

    private VATDetails vat ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public PersonalDetails getPersonal() {
        return personal;
    }

    public void setPersonal(PersonalDetails personal) {
        this.personal = personal;
    }

    public AddressDetails getAddress() {
        return address;
    }

    public void setAddress(AddressDetails address) {
        this.address = address;
    }

    public BankDetails getBank() {
        return bank;
    }

    public void setBank(BankDetails bank) {
        this.bank = bank;
    }

    public VATDetails getVat() {
        return vat;
    }

    public void setVat(VATDetails vat) {
        this.vat = vat;
    }
}
