package com.example.mohmurtu.registration.model;

import java.util.List;

/**
 * Created by mohmurtu on 11/30/2015.
 */
public class Products {

    private boolean issuccess ;
    private List<Product> products ;
    private int noOfProducts ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(int noOfProducts) {
        this.noOfProducts = noOfProducts;
    }
}
