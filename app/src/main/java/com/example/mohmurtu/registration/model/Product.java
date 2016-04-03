package com.example.mohmurtu.registration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable{

    public String prodId;
    public String prodName;
    public long price, mrp;
    public int discount, catId, disabled, isApproved, quantity ;
    public String prodDesc;
    public String catOneName ="";
    public String catTwoName ="";
    public String catThreeName ="";
    public String coverImagePath ;

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public long getMrp() {
        return mrp;
    }

    public void setMrp(long mrp) {
        this.mrp = mrp;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    List<Property> propAndValues;

    public List<String> imageURL;



    public Product(){

    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prodId);
        dest.writeString(prodName);
        dest.writeInt(quantity);
        dest.writeLong(price);
        dest.writeDouble(catId);
        dest.writeInt(disabled);
        dest.writeLong(mrp);
        dest.writeString(prodDesc);
        dest.writeString(catOneName);
        dest.writeString(catTwoName);
        dest.writeString(catThreeName);
        dest.writeList(propAndValues);
        dest.writeStringList(imageURL);

    }

    private Product(Parcel in){

        this.prodId = in.readString();
        this.prodName = in.readString();
        this.quantity = in.readInt();
        this.price = in.readLong();
        this.mrp = in.readLong();
        this.catId = in.readInt();
        this.disabled = in.readInt();
        this.prodDesc = in.readString();
        this.catOneName = in.readString();
        this.catTwoName = in.readString();
        this.catThreeName = in.readString();
        this.propAndValues = new ArrayList<Property>();
        in.readList(propAndValues,null);

       in.readStringList(imageURL);

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };



    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getCatOneName() {
        return catOneName;
    }

    public void setCatOneName(String catOneName) {
        this.catOneName = catOneName;
    }

    public String getCatTwoName() {
        return catTwoName;
    }

    public void setCatTwoName(String catTwoName) {
        this.catTwoName = catTwoName;
    }

    public String getCatThreeName() {
        return catThreeName;
    }

    public void setCatThreeName(String catThreeName) {
        this.catThreeName = catThreeName;
    }

    public List<Property> getPropAndValues() {
        return propAndValues;
    }

    public void setPropAndValues(List<Property> propAndValues) {
        this.propAndValues = propAndValues;
    }

    public List<String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }
}
