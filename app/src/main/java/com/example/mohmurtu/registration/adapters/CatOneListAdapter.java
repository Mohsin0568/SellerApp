package com.example.mohmurtu.registration.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mohmurtu.registration.ActivityHome;
import com.example.mohmurtu.registration.CategorySecondLevel;
import com.example.mohmurtu.registration.R;
import com.example.mohmurtu.registration.model.CategoryOne;
import com.example.mohmurtu.registration.model.CategoryThree;
import com.example.mohmurtu.registration.model.CategoryTwo;
import com.example.mohmurtu.registration.model.ExpandableListDataPump;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

public class CatOneListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<CategoryOne> catOneList;
    String catOneName = null;


    public CatOneListAdapter(Activity activity, List<CategoryOne> catOneList) {
        this.activity = activity;
        this.catOneList = catOneList;
    }

    @Override
    public int getCount() {
        return catOneList.size();
    }

    @Override
    public Object getItem(int location) {
        return catOneList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.category_one_list,
                    null);

        final CategoryOne categoryOne = catOneList.get(position);

        TextView nameOfProductSecondColumn = (TextView) convertView
                .findViewById(R.id.catOneTextView);
		/*TextView idTextView = (TextView) convertView
				.findViewById(R.id.idTextView);*/
        nameOfProductSecondColumn.setText(categoryOne.getCatOneName());
//		idTextView.setText(categoryOne.getCatOneId());
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPrefUtil.setStringPrefs(Constants.CATONE, categoryOne.getCatOneId());
                System.out.println(" CAT ONE IS : "+categoryOne.getCatOneId());
                canCallNextFragment(categoryOne);

            }
        });
        return convertView;
    }
    public static HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();


    void canCallNextFragment(CategoryOne categoryOne) {
        System.out.println(" ****** ");

        expandableListDetail.clear();

        System.out.println(categoryOne.getCatOneName());
        catOneName = categoryOne.getCatOneName();
        List<String> keyList = new ArrayList<String>();
        List<List<String>> valueList = new ArrayList<List<String>>();
        keyList.clear();
        if (categoryOne.getCatTwos().size() > 0) {

            List<CategoryTwo> catTwoList = categoryOne.getCatTwos();
            for (CategoryTwo categoryTwo : catTwoList) {
                keyList.add(categoryTwo.getCatTwoName()+":"+categoryTwo.getCatTwoId());

                List<CategoryThree> catThreeList=  categoryTwo.getCatThrees();
                List<String> helpListForValuesList = new ArrayList<String>();
                if(catThreeList.size() > 0) {
                    for (CategoryThree categoryThree : catThreeList) {
                        helpListForValuesList.add(categoryThree.getCatThreeName()+":"+categoryThree.getCatThreeId()+":"+categoryTwo.getCatTwoName()); // Modified by Mohsin
                    }
                }
                valueList.add(helpListForValuesList);

            }
            int keyListSize = valueList.size();
            for (int i = 0; i < keyListSize; i++) {
                expandableListDetail.put(keyList.get(i), valueList.get(i));
            }
            System.out.println(valueList.size()+" << valueList size  keyList size > "+keyList.size());
            ExpandableListDataPump expandableListDataPump = new ExpandableListDataPump();
            expandableListDataPump.setExpendiableListMap(expandableListDetail);
        }
        System.out.println(" ******  ");

        CategorySecondLevel secondLevel = CategorySecondLevel.newInstance(catOneName);
        FragmentManager manager = ((ActivityHome)activity).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.login_fragment,secondLevel);
        transaction.addToBackStack("Back");
        transaction.commit();
//        Intent intent = new Intent(activity.getApplicationContext(), CatOneDescActivity.class);
//        activity.startActivity(intent);
        //new MainActivity().displayView(6);
    }
}
