package com.example.mohmurtu.registration.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mohmurtu.registration.MainActivity;
import com.example.mohmurtu.registration.NoInternetConnection;
import com.example.mohmurtu.registration.listeners.ImageDataListener;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class HttpImageWorker extends AsyncTask<List,Void,String> {
    List params ;
    ImageDataListener listener ;
    Context context ;
    boolean isNetwork = false;
    public HttpImageWorker(ImageDataListener listener, Context context) {
        super();
        this.listener = listener;
        this.context = context ;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        if(!NetworkUtility.isNetworkConnectionAvailable(context))
        {
            Intent i = new Intent((MainActivity)context, NoInternetConnection.class);
            context.startActivity(i);

        }else{
            isNetwork =true;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if ( s != null && (!(s.equals(""))))
            listener.imageDataListener(s);
        else{
            Log.d("Error","Error while getting response from server");
        }
    }

    @Override
    protected String doInBackground(List... lists) {
        if(isNetwork) {
            Log.d("Inside", "DoInBackgroud Method");
            HttpURLConnection connection;
            int methodType = 0;
            InputStream reader;
            String response = null;
            String[] keys = (String[]) lists[0].get(0);
            Object[] values = (Object[]) lists[0].get(1);
            String url = lists[0].get(2).toString();
            String method = lists[0].get(3).toString();
            String bodyParams = null;
            if (method.equals("GET")) {
                bodyParams = Parameters.constructBodyParameters(keys, values);
                url = url + bodyParams;
                Log.v("Connection URL", url);
                methodType = 1;
                Log.d("Mehod Type", "GET");
            } else if (method.equals("POST")) {
                bodyParams = Parameters.constructJsonParameters(keys, values);
                Log.d("Body Params", bodyParams);
                methodType = 2;
                Log.d("Mehod Type", "POST");
            }
            try {
                URL connUrl = new URL(Constants.BASE_URL + url);
                connection = (HttpURLConnection) connUrl.openConnection();
                connection.setReadTimeout(Constants.SOCKET_TIMEOUT /* milliseconds */);
                connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT /* milliseconds */);

                if (methodType == 1) {
                    connection.setRequestMethod("GET");
                } else {
                    Log.d("Sending Post Request", "true");
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write(bodyParams);
                    Log.d("Request Send", "true");
                    bw.flush();
                    bw.close();
                    os.close();
                }

                connection.connect();
                reader = new BufferedInputStream(connection.getInputStream());
                response = getStringFromInputStream(reader);
                Log.v("Response", response);
            } catch (Exception e) {
                Log.d("Efanta Exception", "Got exception in doInBackground method " + e);
                e.printStackTrace();
            }

            return response;
        }
        else
            return "";
    }


    private String getStringFromInputStream(InputStream inputStream)
            throws IOException {

        Log.d("Reading Reponse","true");
        byte[] b = new byte[1024];
        int size;
        StringBuilder returnString = new StringBuilder();
        while ((size = inputStream.read(b)) != -1) {
            returnString.append(new String(b, 0, size));
        }
        return returnString.toString();

    }
}
