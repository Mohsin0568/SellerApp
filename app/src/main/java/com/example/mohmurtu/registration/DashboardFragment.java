package com.example.mohmurtu.registration;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Dashboard;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements DataListener{

    ProgressBar productsProgress, pendingProgress, soldProgress, dueProgress, ratingsProgress, commentsProgress ;
    TextView productsText, pendingText, soldText, dueText, ratingsText, commentsText ;
    int sellerId ;
    HttpWorker workder[] = new HttpWorker[6];
    Context context ;
    SwipeRefreshLayout refreshLayout ;
    String paths[] = {Constants.TOTAL_PRODUCTS, Constants.PENDING_ORDERS, Constants.PRODUCTS_SOLD, Constants.AMOUNT_DUE, Constants.REVIEW, Constants.COMMENTS_COUNT};

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        ((ActivityHome)context).getSupportActionBar().setTitle("Dashboard");

        productsProgress = (ProgressBar) v.findViewById(R.id.total_products_progress);
        pendingProgress = (ProgressBar) v.findViewById(R.id.pending_orders_progress);
        soldProgress = (ProgressBar) v.findViewById(R.id.products_sold_progress);
        dueProgress = (ProgressBar) v.findViewById(R.id.amount_due_progress);
        ratingsProgress = (ProgressBar) v.findViewById(R.id.ratings_progress);
        commentsProgress = (ProgressBar) v.findViewById(R.id.review_comments_progress);

        productsText = (TextView) v.findViewById(R.id.total_products_value);
        pendingText = (TextView) v.findViewById(R.id.pending_orders_value);
        soldText = (TextView) v.findViewById(R.id.products_sold_value);
        dueText = (TextView) v.findViewById(R.id.amount_due_value);
        ratingsText = (TextView) v.findViewById(R.id.ratings_value);
        commentsText = (TextView) v.findViewById(R.id.review_comments_value);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_page_dashboard);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDashboard();
            }
        });

        connectToServer();

        return  v ;
    }

    public void refreshDashboard(){
        productsProgress.setVisibility(View.VISIBLE);
        pendingProgress.setVisibility(View.VISIBLE);
        soldProgress.setVisibility(View.VISIBLE);
        dueProgress.setVisibility(View.VISIBLE);
        ratingsProgress.setVisibility(View.VISIBLE);
        commentsProgress.setVisibility(View.VISIBLE);

        productsText.setVisibility(View.GONE);
        pendingText.setVisibility(View.GONE);
        soldText.setVisibility(View.GONE);
        dueText.setVisibility(View.GONE);
        ratingsText.setVisibility(View.GONE);
        commentsText.setVisibility(View.GONE);

        connectToServer();
    }

    public void connectToServer(){
        for(int i = 0 ; i < 6 ; i++){
            workder[i] = new HttpWorker(this, null, context);
            workder[i].execute(constructParameters(i));
        }
    }

    public List constructParameters(int position){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId"});
        params.add(new Object[]{sellerId+""});
        params.add(paths[position]);
        params.add("GET");
        return  params;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void dataDownloaded(String response) {
        try{
            if(response != null){
                ObjectMapper mapper = new ObjectMapper();
                Dashboard dashboard = mapper.readValue(response, Dashboard.class);
                if(dashboard.issuccess() == true){
                    String type = dashboard.getType();
                    System.out.println("Type is " + type);
                    if(type.equals("review")){
                        ratingsProgress.setVisibility(View.GONE);
                        ratingsText.setVisibility(View.VISIBLE);
                        ratingsText.setText(dashboard.getCount());
                    }
                    else if(type.equals("totalComments")){
                        commentsProgress.setVisibility(View.GONE);
                        commentsText.setVisibility(View.VISIBLE);
                        commentsText.setText(dashboard.getCount());
                    }
                    else if(type.equals("totalProducts")){
                        productsProgress.setVisibility(View.GONE);
                        productsText.setVisibility(View.VISIBLE);
                        productsText.setText(dashboard.getCount());
                    }
                    else if(type.equals("pendingOrders")){
                        pendingProgress.setVisibility(View.GONE);
                        pendingText.setVisibility(View.VISIBLE);
                        pendingText.setText(dashboard.getCount());
                    }
                    else if(type.equals("productsSold")){
                        soldProgress.setVisibility(View.GONE);
                        soldText.setVisibility(View.VISIBLE);
                        soldText.setText(dashboard.getCount());
                    }
                    else if(type.equals("amountDue")){
                        dueProgress.setVisibility(View.GONE);
                        dueText.setVisibility(View.VISIBLE);
                        dueText.setText(dashboard.getCount());
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
