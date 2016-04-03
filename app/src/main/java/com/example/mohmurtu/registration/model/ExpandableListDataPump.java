package com.example.mohmurtu.registration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mohmurtu on 11/14/2015.
 */
public class ExpandableListDataPump {

    private HashMap<String, List<String>> expendiableListMap;

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> women = new ArrayList<String>();
        women.add("Clothing");
        women.add("Shoes");
        women.add("Bags");
        women.add("Accessories");
        women.add("Jewellery");

        List<String> man = new ArrayList<String>();
        man.add("Clothing");
        man.add("Shoes");
        man.add("Bags");
        man.add("Accessories");
        man.add("Sunglasses");

        List<String> kids = new ArrayList<String>();

        kids.add("Boys Clothing");
        kids.add("Girl Clothing");
        kids.add("Shoes");
        kids.add("Accessories");

        expandableListDetail.put("MEN", man);
        expandableListDetail.put("WOMEN", women);
        expandableListDetail.put("KIDS", kids);

        return expandableListDetail;
    }

    public HashMap<String, List<String>> getExpendiableListMap() {
        return expendiableListMap;
    }

    public void setExpendiableListMap(HashMap<String, List<String>> expendiableListMap) {
        this.expendiableListMap = expendiableListMap;
    }

}
