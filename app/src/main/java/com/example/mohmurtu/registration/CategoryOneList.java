package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mohmurtu.registration.adapters.CatOneListAdapter;
import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Categories;
import com.example.mohmurtu.registration.model.CategoryOne;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CategoryOneList extends Fragment implements DataListener {

    ListView catOneListView;
    CatOneListAdapter catOneListAdapter;
    ProgressDialog prgDialog;
    Context context ;
    boolean flag = true ;
    RelativeLayout categoriesContent ;
    TextView categoryNotAvailable ;
    int sellerId ;

    public static CategoryOneList newInstance(String param1, String param2) {
        CategoryOneList fragment = new CategoryOneList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryOneList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        View v = inflater.inflate(R.layout.fragment_category_one_list, container, false);
        categoriesContent = (RelativeLayout) v.findViewById(R.id.category_content);
        categoryNotAvailable = (TextView) v.findViewById(R.id.category_not_listed);
        catOneListView = (ListView) v.findViewById(R.id.catOneListView);
        catOneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });
        fetchData();
        return v ;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity ;
        ((ActivityHome) activity).getSupportActionBar().setTitle("Select Main Category");
    }

    public void fetchData() {
        prgDialog = new ProgressDialog(context);
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();
        prgDialog.setMessage("Getting Categories");
        List params = constuctParameters();
        HttpWorker worker = new HttpWorker(this, prgDialog, context);
        worker.execute(params);
    }

    public List constuctParameters() {
        List<Object> params = new ArrayList<Object>();
        params.add(new String[] {"sellerId"});
        params.add(new String[] {sellerId+""});
        params.add(Constants.CHECK_CATEGORY_URL);
        params.add("GET");
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        prgDialog.cancel();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Categories cat = mapper.readValue(response, Categories.class);
            if(cat.isAccessFlag() == true && cat.issuccess() == true) {
                categoryNotAvailable.setVisibility(View.GONE);
                categoriesContent.setVisibility(View.VISIBLE);
                System.out.println(cat.getCategories().size() + "  ****   ");
                List<CategoryOne> catOneList = cat.getCategories();

                catOneListAdapter = new CatOneListAdapter(getActivity(), catOneList);
                flag = false;
                catOneListView.setAdapter(catOneListAdapter);
                catOneListAdapter.notifyDataSetChanged();
			/*
			 * for (CategoryOne categoryOne : catOneList) {
			 * System.out.println(categoryOne.getCatOneName()+" &&&&&&&&& "); }
			 */
            }
            else if (cat.isAccessFlag() == false){
                categoryNotAvailable.setVisibility(View.VISIBLE);
                categoriesContent.setVisibility(View.GONE);
            }

        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
