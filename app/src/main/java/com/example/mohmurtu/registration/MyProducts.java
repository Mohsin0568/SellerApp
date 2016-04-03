package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohmurtu.registration.adapters.ProductsAdapter;
import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Products;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.EndlessOnScrollListener;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class MyProducts extends Fragment implements DataListener{

    Context context ;
    private RecyclerView recyclerproduct;
    private static String searchString = "" ;
    FloatingActionButton floatingButton ;
    TextView noProducts ;
    int sellerId ;
    public static int INDEX = 1 ;
    public static int DISABLED = 10 ;
    public static int APPROVAL = 10 ;
    public int currentPage = 1;
    ProgressDialog pd ;
    ProductsAdapter productsAdapter ;
    LinearLayoutManager llm ;
    final static int FILTER_RESULT_CODE = 1 ;
    static boolean filterFlag = false;

    public static MyProducts newInstance() {
        MyProducts fragment = new MyProducts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyProducts() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INDEX = 1 ;
        setHasOptionsMenu(true);
        ((ActivityHome)context).getSupportActionBar().setTitle("Products");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.APPROVAL=10;
        this.DISABLED=10;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        notificationManager.cancel(0);
        SharedPrefUtil.setStringPrefs(Constants.APPROVED_NOTIFICATION_MESSAGE, "");
        SharedPrefUtil.setStringPrefs(Constants.REJECTED_NOTIFICATION_MESSAGE, "");
        View v = inflater.inflate(R.layout.fragment_my_products, container, false);
        recyclerproduct = (RecyclerView) v.findViewById(R.id.products_recylerview);
        noProducts = (TextView) v.findViewById(R.id.noProducts);
        floatingButton = (FloatingActionButton) v.findViewById(R.id.filter_button);
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        pd = new ProgressDialog(context);
        llm = new LinearLayoutManager(getActivity());
        recyclerproduct.setLayoutManager(llm);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductFilter.class);
                startActivityForResult(i, FILTER_RESULT_CODE);
            }
        });
        getDataFromServer();

        recyclerproduct.addOnScrollListener(new EndlessOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                INDEX = INDEX + 1 ;
                getDataFromServer();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i){
        super.onActivityResult(requestCode, resultCode, i);
        if(i != null){
            this.INDEX = 1 ;
            noProducts.setVisibility(View.GONE);
            productsAdapter = null;
            filterFlag = true ;
            System.out.println("Selected Radio Buttons are: " + i.getIntExtra("visibility", 10) + " " + i.getIntExtra("approval", 10));
            this.DISABLED = i.getIntExtra("visibility",10);
            this.APPROVAL = i.getIntExtra("approval",10);
            getDataFromServer();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.product_search,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void getDataFromServer(){
        System.out.println("Approval = " + this.APPROVAL + ", Visibility = " + this.DISABLED);
        pd.setMessage("Getting Products");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List constructParameters(){
        List params = new ArrayList();
        params.add(new String[]{"sellerId","NumOfRecords","index","disabled","searchString","catOneId","catTwoId","catThreeId","approval"});
        params.add(new Object[]{sellerId+"",20+"",INDEX+"", DISABLED+"", "", 0+"",0+"",0+"",APPROVAL+""});
        params.add(Constants.GET_PRODUCTS_URL);
        params.add("GET");
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try {
            ObjectMapper om = new ObjectMapper();
            noProducts.setVisibility(View.GONE);
            Products products = om.readValue(response, Products.class);
            Log.d("Result", products.issuccess()+"");
            if(products.getNoOfProducts() >= 0) {
                if (INDEX > 1) {
                    if (products.getProducts() != null && products.getProducts().size() > 0)
                        productsAdapter.addProducts(products.getProducts());
                } else {
                    productsAdapter = new ProductsAdapter(context, products.getProducts());
                    recyclerproduct.setAdapter(productsAdapter);
                }
            }
            if(INDEX == 1 && products.getNoOfProducts()==0){
                noProducts.setVisibility(View.VISIBLE);
                if(filterFlag == true){
                    floatingButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    floatingButton.setVisibility(View.GONE);
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
