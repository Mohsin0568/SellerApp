package com.example.mohmurtu.registration.model;

import java.util.List;

/**
 * Created by mohmurtu on 11/14/2015.
 */
public class Categories {

    private boolean issuccess ;
    private List<CategoryOne> categories ;

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public List<CategoryOne> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryOne> categories) {
        this.categories = categories;
    }
}
