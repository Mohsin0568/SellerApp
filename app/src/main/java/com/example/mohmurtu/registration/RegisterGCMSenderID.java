package com.example.mohmurtu.registration;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterGCMSenderID extends IntentService implements DataListener {

    int sellerId ;
    String token ;
    public RegisterGCMSenderID(){
        super("Register Device");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
            System.out.println("Inside onHandleIntent method of RegisterGCMSenderID");
            if (intent != null) {
                InstanceID id = InstanceID.getInstance(this);
                token = id.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                System.out.println("Token is " + token);
                if(!(token.equals(""))) {
                    HttpWorker http = new HttpWorker(this, null, getApplicationContext());
                    http.sendDataToServer(constructParameters());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId","gcmToken"});
        params.add(new Object[]{sellerId, token});
        params.add(Constants.REGISTER_GCM_ID);
        params.add("POST");
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        try{
            JSONObject json = new JSONObject(response);
            boolean flag = json.getBoolean("issucess");
            if(flag == true){
                SharedPrefUtil.setBooleanPrefs(Constants.IS_GCM_ID_UPLOADED_IN_SERVER, true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
