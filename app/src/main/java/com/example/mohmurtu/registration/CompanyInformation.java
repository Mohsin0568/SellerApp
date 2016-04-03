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
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompanyInformation extends Fragment implements DataListener {

    private static String companyName = "param1";

    private String mParam1;
    private String mParam2;
    private EditText name, password, email, phone, altPhone, address, pincdoe, city, state, benecficaryName, accountNumber, bankName, branceh, ifscCode;
    private Button back, register;
    String dname, dpassword, demail, dphone, daltPhone, daddress, dpincode, dcity, dstate, dbeneficaryName, daccountNumber, dbankName, dbranch, difscCode;
    TextInputLayout lname, lpassword, lemail, lphone, latlPhone, laddress, lpincode, lcity, lstate, lbeneficaryName, laccountNumber, lbankName, lbranch, lifscCode;
    boolean processFlag = true, emailProcessFlag = true, phoneProcessFlag = true ;
    int emailCount = 2, phoneCount = 2, altPhoneCount = 2 ;
    boolean isEmailCheck = false, isPhoneCheck = false, isRegistration = false;
    ProgressDialog pd ;
    Context context = null;

//    private OnFragmentInteractionListener mListener;

    public void setContext(Context context){
        this.context = context ;
    }

    public static CompanyInformation newInstance(String param1, Context context) {
        CompanyInformation fragment = new CompanyInformation();
        Bundle args = new Bundle();
        args.putString(companyName, param1);
        fragment.setContext(context);
        fragment.setArguments(args);
        return fragment;
    }

    public CompanyInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            companyName = getArguments().getString(companyName);
            Log.d("Company Name", companyName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_company_information, container, false);
        pd = new ProgressDialog(context);
        name = (EditText) v.findViewById(R.id.edit_name);
        password = (EditText) v.findViewById(R.id.edit_password);
        email = (EditText) v.findViewById(R.id.edit_email);
        phone = (EditText) v.findViewById(R.id.edit_phone);
        altPhone = (EditText) v.findViewById(R.id.edit_alt_phone);
        address = (EditText) v.findViewById(R.id.edit_address);
        pincdoe = (EditText) v.findViewById(R.id.edit_pincode);
        city = (EditText) v.findViewById(R.id.edit_city);
        state = (EditText) v.findViewById(R.id.edit_state);
        benecficaryName = (EditText) v.findViewById(R.id.edit_beneficiary);
        accountNumber = (EditText) v.findViewById(R.id.edit_account);
        bankName = (EditText) v.findViewById(R.id.edit_bank);
        branceh = (EditText) v.findViewById(R.id.edit_branch);
        ifscCode = (EditText) v.findViewById(R.id.edit_ifsc);

        lname = (TextInputLayout) v.findViewById(R.id.layout_edit_name);
        lpassword = (TextInputLayout) v.findViewById(R.id.layout_edit_password);
        lemail = (TextInputLayout) v.findViewById(R.id.layout_edit_email);
        lphone = (TextInputLayout) v.findViewById(R.id.layout_edit_phone);
        latlPhone = (TextInputLayout) v.findViewById(R.id.layout_edit_alt_phone);
        laddress = (TextInputLayout) v.findViewById(R.id.layout_edit_address);
        lpincode = (TextInputLayout) v.findViewById(R.id.layout_edit_pincode);
        lcity = (TextInputLayout) v.findViewById(R.id.layout_edit_city);
        lstate = (TextInputLayout) v.findViewById(R.id.layout_edit_state);
        lbeneficaryName = (TextInputLayout) v.findViewById(R.id.layout_edit_beneficiary);
        laccountNumber = (TextInputLayout) v.findViewById(R.id.layout_edit_account);
        lbankName = (TextInputLayout) v.findViewById(R.id.layout_edit_bank);
        lbranch = (TextInputLayout) v.findViewById(R.id.layout_edit_branch);
        lifscCode = (TextInputLayout) v.findViewById(R.id.layout_edit_ifsc);

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dphone = phone.getText().toString();
                if((phoneCount%2) >= 1 ){
                    if(dphone == null || dphone.length() != 10){
                        lphone.setError("Enter 10 digits Phone Number");
                        processFlag = false ;
                    }
                    else {
                        isPhoneCheck = true ;
                        pd.setMessage("Checking Phone Number Availability");
                        pd.setCancelable(false);
                        connectToServer();
                    }
                }
                phoneCount++ ;
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            demail = email.getText().toString();
            if((emailCount%2) >= 1){
               if(demail == null || demail.equals("")){
                   lemail.setError("Enter Email Address");
                   processFlag = false;
               }
               else if (!( Patterns.EMAIL_ADDRESS.matcher(demail).matches())){
                   lemail.setError("Email is not valid");
                   processFlag = false;
               }
               else{
                   isEmailCheck = true ;
                   pd.setMessage("Checking Email Availability");
                   pd.setCancelable(false);
                   connectToServer();
               }
            }
            emailCount++;
            }
        });

        back = (Button) v.findViewById(R.id.button_back);
        register = (Button) v.findViewById(R.id.button_register);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegisterButton();
            }
        });

        return v ;
    }

    public void handleRegisterButton(){
        dname = name.getText().toString();
        dpassword = password.getText().toString();
        demail = email.getText().toString();
        dphone = phone.getText().toString();
        daltPhone = altPhone.getText().toString();

        daddress = address.getText().toString();
        dpincode = pincdoe.getText().toString();
        dcity = city.getText().toString();
        dstate = state.getText().toString();

        dbeneficaryName = benecficaryName.getText().toString();
        daccountNumber = accountNumber.getText().toString();
        dbankName = bankName.getText().toString();
        dbranch = branceh.getText().toString();
        difscCode = ifscCode.getText().toString();

        processFlag = true ;

        if(dname == null || dname.equals("")) {
            lname.setError("Enter Name");
            processFlag = false;
            requestFocus(lname);
        }
        if(dpassword == null || dpassword.equals("")){
            lpassword.setError("Enter Password");
            processFlag = false;
            requestFocus(lpassword);
        }
        if(demail == null || demail.equals("")){
            lemail.setError("Enter Email");
            processFlag = false;
            requestFocus(lemail);
        }
        if(dphone == null || dphone.equals("")){
            lphone.setError("Enter Phone Number");
            processFlag = false;
            requestFocus(lphone);
        }
        if(daddress == null || daddress.equals("")){
            laddress.setError("Enter Address");
            processFlag = false;
            requestFocus(laddress);
        }
        if(dpincode == null || dpincode.equals("")){
            lpincode.setError("Enter PinCode");
            processFlag = false;
            requestFocus(lpincode);
        }
        if(dcity == null || dcity.equals("")){
            lcity.setError("Enter City");
            processFlag = false;
            requestFocus(lcity);
        }
        if(dstate == null || dstate.equals("")){
            lstate.setError("Enter State");
            processFlag = false;
            requestFocus(lstate);
        }
        if(dbeneficaryName == null || dbeneficaryName.equals("")){
            lbeneficaryName.setError("Enter Beneficiary Name");
            processFlag = false;
            requestFocus(lbeneficaryName);
        }
        if(dbankName == null || dbankName.equals("")){
            lbankName.setError("Enter Bank Name");
            processFlag = false;
            requestFocus(lbankName);
        }
        if(daccountNumber == null || daccountNumber.equals("")){
            laccountNumber.setError("Enter Account Number");
            processFlag = false;
            requestFocus(laccountNumber);
        }
        if(dbranch == null || dbranch.equals("")){
            lbranch.setError("Enter Branch Name");
            processFlag = false;
            requestFocus(lbranch);
        }
        if(difscCode == null || difscCode.equals("")){
            lifscCode.setError("Enter IFSC Code");
            processFlag = false;
            requestFocus(lifscCode);
        }

        if(processFlag == true && emailProcessFlag == true && phoneProcessFlag == true){
            isRegistration = true ;
            pd.setMessage("Registering");
            pd.setCancelable(false);
            connectToServer();
        }
        else{
            Toast.makeText(context, "Correct the above errors", Toast.LENGTH_SHORT);
        }

    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        if(isEmailCheck == true) {
            params.add(new String[]{"emailCheck"});
            params.add(new Object[]{demail});
            params.add(Constants.EMAIL_CHECK);
            params.add("GET");
        }
        else if(isPhoneCheck == true){
            params.add(new String[]{"phoneNumber"});
            params.add(new Object[]{dphone});
            params.add(Constants.PHONE_CHECK);
            params.add("GET");
        }
        else if(isRegistration == true){
            Log.d("Registration",true+"");
            params.add(new String[]{"companyName","name","password","email","phoneNumber","altPhoneNumber","address","pinCode","city","state","beneficiaryName","accountNumber","bankName","branch","ifscCode"});
            params.add(new Object[]{companyName,dname,dpassword,demail,dphone,daltPhone,daddress,dpincode,dcity,dstate,dbeneficaryName,daccountNumber,dbankName,dbranch,difscCode});
            params.add(Constants.REGISTER_URL);
            params.add("POST");
        }
        return params;
    }

    public void requestFocus(View view){
        if(view.requestFocus())
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void connectToServer(){
        HttpWorker worker = new HttpWorker(this, pd, getActivity());
        worker.execute(constructParameters());
    }

    @Override
    public void dataDownloaded(String response) {
        Log.d("Response", response);
        pd.cancel();
        if (isEmailCheck == true) {
            Log.d("Response", response);
            try {
                JSONObject json = new JSONObject(response);
                boolean emailStatus = json.getBoolean("status");
                if (emailStatus == true) {
                    lemail.setError("Email already Exists");
                    emailProcessFlag = false;
                } else {
                    emailProcessFlag = true;
                    lemail.setError(null);
                }
                isEmailCheck = false;
            } catch (Exception e) {
                e.printStackTrace();
                isEmailCheck = false;
            }
        } else if (isPhoneCheck == true) {
            Log.d("Response", response);
            try {
                JSONObject json = new JSONObject(response);
                boolean phoneStatus = json.getBoolean("status");
                if (phoneStatus == true) {
                    lphone.setErrorEnabled(true);
                    lphone.setError("Phone Number already Exists");
                    phoneProcessFlag = false;
                } else {
                    phoneProcessFlag = true;
                    lphone.setError(null);
                }
                isPhoneCheck = false;
            } catch (Exception e) {
                e.printStackTrace();
                isPhoneCheck = false;
            }
        } else if (isRegistration == true) {
            try {
                JSONObject json = new JSONObject(response);
                boolean result = json.getBoolean("issuccess");
                if(result == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Registration Successful, But it might take 3-4 days for Seller Approval");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                            LoginFragment fragment = new LoginFragment();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.fragment_body, fragment);
                            transaction.commit();
                        }
                    });
                    builder.show();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}