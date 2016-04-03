package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mohmurtu.registration.adapters.OrdersAdapter;
import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.orders.Orders;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


public class MyOrders extends Fragment implements DataListener{
    RecyclerView ordersRecyler ;
    FloatingActionButton filterButton;
    Context context ;
    ProgressDialog pd ;
    int sellerId ;
    public static int INDEX = 1 ;
    OrdersAdapter ordersAdapter ;
    LinearLayoutManager llm ;
    TextView noProduct ;

    public static MyOrders newInstance(String param1, String param2) {
        MyOrders fragment = new MyOrders();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ActivityHome)context).getSupportActionBar().setTitle("Orders");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        pd = new ProgressDialog(context);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ordersRecyler = (RecyclerView) v.findViewById(R.id.orders_recylerview);
        filterButton = (FloatingActionButton) v.findViewById(R.id.orders_filter_button);
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        noProduct = (TextView) v.findViewById(R.id.noOrders);
        pd = new ProgressDialog(context);
        llm = new LinearLayoutManager(getActivity());
        ordersRecyler.setLayoutManager(llm);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflator = LayoutInflater.from(context);
                final View view1 = inflator.inflate(R.layout.order_filter, null);
                builder.setView(view1);
                builder.setCancelable(false);
                final RadioGroup filterOptions = (RadioGroup) view1.findViewById(R.id.order_filter_group);
                filterOptions.check(R.id.all_products);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

//                Intent i = new Intent(context, ProductFilter.class);
//                startActivityForResult(i, FILTER_RESULT_CODE);
            }
        });
        getDataFromServer();
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void getDataFromServer(){
        pd.setMessage("Getting Orders");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List constructParameters(){
        List params = new ArrayList();
        params.add(new String[]{"sellerId","index","noOfOrders"});
        params.add(new Object[]{sellerId+"",INDEX+"",20+""});
        params.add(Constants.GET_ORDERS);
        params.add("GET");
        return params;
    }


    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try{
            ObjectMapper om = new ObjectMapper();
            Orders orders = om.readValue(response, Orders.class);
            Log.d("Result", orders.issuccess()+"");
            if(orders.getNoOfOrders() > 0) {
                if (INDEX > 1) {
                    if (orders.getOrder() != null && orders.getOrder().size() > 0)
                        ordersAdapter.addOrders(orders.getOrder());
                } else {
                    ordersAdapter = new OrdersAdapter(context, orders.getOrder());
                    ordersRecyler.setAdapter(ordersAdapter);
                }
            }
            if(ordersAdapter == null){
                noProduct.setVisibility(View.VISIBLE);
                filterButton.setVisibility(View.GONE);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
