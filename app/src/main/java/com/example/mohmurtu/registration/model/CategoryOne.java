package com.example.mohmurtu.registration.model;

import java.util.List;

/**
 * Created by mohmurtu on 11/14/2015.
 */
public class CategoryOne {

    private String catOneId, catOneName ;
    private List<CategoryTwo> catTwos ;

    public String getCatOneId() {
        return catOneId;
    }

    public void setCatOneId(String catOneId) {
        this.catOneId = catOneId;
    }

    public String getCatOneName() {
        return catOneName;
    }

    public void setCatOneName(String catOneName) {
        this.catOneName = catOneName;
    }

    public List<CategoryTwo> getCatTwos() {
        return catTwos;
    }

    public void setCatTwos(List<CategoryTwo> catTwos) {
        this.catTwos = catTwos;
    }
}
