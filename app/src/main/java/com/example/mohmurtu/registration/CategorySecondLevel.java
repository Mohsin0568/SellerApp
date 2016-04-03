package com.example.mohmurtu.registration;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.mohmurtu.registration.adapters.CatOneListAdapter;
import com.example.mohmurtu.registration.adapters.CategoryExpandableListAdapter;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategorySecondLevel extends Fragment {

    ExpandableListView expandableListView;
    CategoryExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    Context context ;
    String categoryOneName = null, categoryTwoName, categoryThreeName;

    public static CategorySecondLevel newInstance(String categoryOne) {
        CategorySecondLevel fragment = new CategorySecondLevel();
        Bundle args = new Bundle();
        args.putString("categoryOne",categoryOne);
        fragment.setArguments(args);
        return fragment;
    }

    public CategorySecondLevel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            categoryOneName = getArguments().getString("categoryOne");
            System.out.println("CategoryOne name in categorySecondLevel is " + categoryOneName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_second_level, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Select Categories");
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListDetail = CatOneListAdapter.expandableListDetail;
        System.out.println(expandableListDetail.size()+" ***** ");
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CategoryExpandableListAdapter(context, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                String [] arrayForString = expandableListTitle.get(groupPosition).split(":");
                String id = "";
                if(arrayForString.length>0){
                    id = arrayForString[1];
                    categoryTwoName = arrayForString[0];
                }
                System.out.println(" CATTWOID IS : "+id + " and name is " + categoryTwoName);
                SharedPrefUtil.setStringPrefs(Constants.CATTWO,id);
//                Constants.setInPrefrences(getApplicationContext(), Constants.CATEGORIES_PREFRENCES_NAME, "CATTWOID", id);
               /* Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
              /*  Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                )
                        .show();*/
                String _id = "";
                String text = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition);
                String [] arrayForString = text.split(":");
                if(arrayForString.length > 0){

                    _id = arrayForString[1];
                    categoryThreeName = arrayForString[0];
                    categoryTwoName = arrayForString[2];
                    System.out.println("Category three id is " + _id + " and name is " + categoryThreeName);

                }

                AddNewProduct addFragment = AddNewProduct.newInstance(categoryOneName, categoryTwoName, categoryThreeName, _id, null);
                FragmentManager manager = ((ActivityHome)context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.login_fragment, addFragment);
                transaction.addToBackStack("Back");
                transaction.commit();
//                Intent intent = new Intent(getApplicationContext(), ProductUploadActivity.class);
//                intent.putExtra("catThreeId", _id);
//                startActivity(intent);

                // can call here next Activity with below url
                // http://fantabazaar.com/eShopping/property/getPropertiesAndValues?categoryThreeId=1


                return false;
            }
        });
        return v ;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }
}
