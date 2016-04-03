package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PasswordChangeFragment extends Fragment implements DataListener {

    Context context ;
    EditText password, newPassword, confirmPassword ;
    TextInputLayout lpassowrd, lnewPassword, lconfirmPassword ;
    Button submit ;
    String dpassword, dnewPassword, dconfirmPassword;
    int sellerId ;
    ProgressDialog pd ;

    public static PasswordChangeFragment newInstance(String param1, String param2) {
        PasswordChangeFragment fragment = new PasswordChangeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PasswordChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password_change, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Change Password");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        password = (EditText) v.findViewById(R.id.current_password);
        newPassword = (EditText) v.findViewById(R.id.new_password);
        confirmPassword = (EditText) v.findViewById(R.id.confirm_password);

        lpassowrd = (TextInputLayout) v.findViewById(R.id.layout_current_password);
        lnewPassword = (TextInputLayout) v.findViewById(R.id.layout_new_password);
        lconfirmPassword = (TextInputLayout) v.findViewById(R.id.layout_confirm_password);

        submit = (Button) v.findViewById(R.id.change_password_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordValidation();
            }
        });
        return v ;
    }

    public void changePasswordValidation(){
        dpassword = password.getText().toString();
        dconfirmPassword = confirmPassword.getText().toString();
        dnewPassword = newPassword.getText().toString();
//        lnewPassword.setErrorEnabled(false);
//        lconfirmPassword.setErrorEnabled(false);
//        lpassowrd.setErrorEnabled(false);
        boolean isValidate = true ;
        if (dpassword == null || dpassword.equals("")){
            lpassowrd.setErrorEnabled(true);
            lpassowrd.setError("Enter Current Password");
            isValidate = false;
        }
        if (dnewPassword == null || dnewPassword.equals("")){
            lnewPassword.setErrorEnabled(true);
            lnewPassword.setError("Enter New Password");
            isValidate = false;
        }
        if(dconfirmPassword == null || dconfirmPassword.equals("")){
            lconfirmPassword.setErrorEnabled(true);
            lconfirmPassword.setError("Confirm New Password");
            isValidate = false;
        }
        if(isValidate){
            if(!(dnewPassword.equals(dconfirmPassword))){
                lconfirmPassword.setErrorEnabled(true);
                lconfirmPassword.setError("Password Doesn't Match");
                lnewPassword.setErrorEnabled(true);
                lnewPassword.setError("Password Doesn't Match");
                isValidate = false;
            }
            else{
                connectToServer();
            }
        }
    }

    public void connectToServer(){
        pd = new ProgressDialog(context);
        pd.setMessage("Changing Password");
        pd.setCancelable(false);
        HttpWorker httpWorker = new HttpWorker(this, pd, context);
        httpWorker.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"oldPassword", "newPassword", "sellerId"});
        params.add(new Object[]{dpassword, dnewPassword, sellerId+""});
        params.add(Constants.CHANGE_PASSWORD);
        params.add("POST");
        return params;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try{
            JSONObject json = new JSONObject(response);
            final boolean result = json.getBoolean("issuccess");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage(json.getString("message"));
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (result == true) {
                        getFragmentManager().popBackStackImmediate();
                    }
                    else{
                        password.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                    }
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}