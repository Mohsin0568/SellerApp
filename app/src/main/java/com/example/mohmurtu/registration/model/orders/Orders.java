package com.example.mohmurtu.registration.model.orders;

import java.util.List;

/**
 * Created by mohmurtu on 2/21/2016.
 */
public class Orders {

    private boolean issuccess ;
    private int noOfOrders ;

    List<Order> order ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public int getNoOfOrders() {
        return noOfOrders;
    }

    public void setNoOfOrders(int noOfOrders) {
        this.noOfOrders = noOfOrders;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }
}
