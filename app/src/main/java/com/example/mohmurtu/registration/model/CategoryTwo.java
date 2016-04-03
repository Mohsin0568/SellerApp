package com.example.mohmurtu.registration.model;

import java.util.List;

/**
 * Created by mohmurtu on 11/14/2015.
 */
public class CategoryTwo {

    private String catTwoId, catTwoName ;
    private List<CategoryThree> catThrees ;

    public String getCatTwoId() {
        return catTwoId;
    }

    public void setCatTwoId(String catTwoId) {
        this.catTwoId = catTwoId;
    }

    public String getCatTwoName() {
        return catTwoName;
    }

    public void setCatTwoName(String catTwoName) {
        this.catTwoName = catTwoName;
    }

    public List<CategoryThree> getCatThrees() {
        return catThrees;
    }

    public void setCatThrees(List<CategoryThree> catThrees) {
        this.catThrees = catThrees;
    }
}
