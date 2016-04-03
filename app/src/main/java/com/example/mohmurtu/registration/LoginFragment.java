package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginFragment extends Fragment implements DataListener {

    Button login, register ;
    EditText emailOrPhone, password ;
    TextInputLayout lemailOrPhone, lpassword ;
    CheckBox rememberMe ;
    String demailOrPhone = "", dpassword = "" ;
    boolean drememberMe = false;
    boolean processFlag = true ;
    Context context ;
    ProgressDialog pd ;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity)context).getSupportActionBar().setTitle("Login");
        ((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        login = (Button) v.findViewById(R.id.button_login);
        register = (Button) v.findViewById(R.id.button_register);
        emailOrPhone = (EditText) v.findViewById(R.id.edit_email_phone);
        password = (EditText) v.findViewById(R.id.edit_userPassword);
        lemailOrPhone = (TextInputLayout) v.findViewById(R.id.layout_edit_email_phone);
        lpassword = (TextInputLayout) v.findViewById(R.id.layout_edit_userPassword);
        rememberMe = (CheckBox) v.findViewById(R.id.checkbox_remember);
        pd = new ProgressDialog(context);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginEvent();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegisterEvent();
            }
        });
        return v ;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        ((MainActivity)activity).getSupportActionBar().setTitle("Login");
    }

    public void handleLoginEvent(){
        demailOrPhone = emailOrPhone.getText().toString();
        dpassword = password.getText().toString();
        drememberMe = rememberMe.isChecked();
        Log.d("Data Entered", demailOrPhone + " " + dpassword + " " + drememberMe);
        if(demailOrPhone.equals("")){
            lemailOrPhone.setError("Enter Email or Phone");
        }
        else if(dpassword.equals("")){
            lpassword.setError("Enter Password");
        }
        else{
            lpassword.setError(null);
            lemailOrPhone.setError(null);
            pd.setMessage("Validating Login");
            pd.setCancelable(false);
            HttpWorker worker = new HttpWorker(this,pd,getActivity());
            worker.execute(constructParameters());
        }
    }

    public List<Object> constructParameters(){

        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"emailOrPhone","password"});
        params.add(new Object[]{demailOrPhone,dpassword});
        params.add(Constants.LOGIN_URL);
        params.add("POST");
        return params;
    }

    public void handleRegisterEvent(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RegisterHome registerHome = new RegisterHome();
        transaction.replace(R.id.fragment_body,registerHome);
        transaction.addToBackStack("Back");
        transaction.commit();
    }

    public void dataDownloaded(String response){
        pd.cancel();
        try{
            if(response != null) {
                JSONObject jsonObject = new JSONObject(response);
                boolean result = jsonObject.getBoolean("issuccess");
                String message = jsonObject.getString("message");
                if (result == false) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            password.setText("");
                        }
                    });
                    builder.show();
                } else {
                    int sellerId = jsonObject.getInt("sellerId");
                    SharedPrefUtil.setBooleanPrefs(Constants.REMEMBER_ME, drememberMe);
                    SharedPrefUtil.setIntegerPrefs(Constants.SELLER_ID, sellerId);
                    SharedPrefUtil.setBooleanPrefs(Constants.IS_LOGGED_IN, true);
                    getActivity().finish();
                    getActivity().getSupportFragmentManager().popBackStack();
                    Intent intent = new Intent(context,ActivityHome.class);
                    intent.putExtra("productNotification",false);
                    context.startActivity(intent);
                }
            }
        }
        catch(Exception e){
            Log.d("LoginFragment Exception",e.toString());
            e.printStackTrace();
        }
    }
}
