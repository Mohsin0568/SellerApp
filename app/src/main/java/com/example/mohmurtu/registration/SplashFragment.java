package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Dashboard;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


public class SplashFragment extends Fragment implements DataListener{

    Context context ;
    int sellerId;
    ProgressDialog pd ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_splash, container, false);
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        connectToSever();
        return v ;
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

    public void connectToSever(){
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.show();
        HttpWorker http = new HttpWorker(this,pd, context);
        http.execute(constructParameters());
    }

    public List constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId"});
        params.add(new Object[]{sellerId+""});
        params.add(Constants.DASHBOARD_DATA);
        params.add("GET");
        return  params;
    }

    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try {
            if (response != null) {
                ObjectMapper om = new ObjectMapper();
                Dashboard dashboard = om.readValue(response, Dashboard.class);
                getActivity().finish();
                getActivity().getSupportFragmentManager().popBackStack();
                Intent i = new Intent(context, ActivityHome.class);
//                i.putExtra("dashboarData", dashboard);
                context.startActivity(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
