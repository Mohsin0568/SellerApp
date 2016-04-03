package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.seller.BankDetails;
import com.example.mohmurtu.registration.model.seller.PersonalDetails;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditBankInformation extends Fragment implements DataListener{

    TextInputLayout lbeneficiaryName, lbankName, lbranch, laccountNumber, lifscCode;
    EditText beneficiaryName, bankName, branch, accountNumber, ifscCode;
    String dbeneficiaryName, dbankName, dbranch, daccountNumber, difscCode;
    Button save, back ;
    Context context ;
    BankDetails bd ;
    int sellerId;
    ProgressDialog pd ;

    public static EditBankInformation newInstance(String param1, String param2) {
        EditBankInformation fragment = new EditBankInformation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EditBankInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            bd = (BankDetails) bundle.getParcelable("bank");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_bank_information, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Bank Details");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);

        lbeneficiaryName = (TextInputLayout) v.findViewById(R.id.beneficiary_name_layout);
        lbankName = (TextInputLayout) v.findViewById(R.id.bank_name_layout);
        lbranch = (TextInputLayout) v.findViewById(R.id.branch_layout);
        laccountNumber = (TextInputLayout) v.findViewById(R.id.account_number_layout);
        lifscCode = (TextInputLayout) v.findViewById(R.id.ifsc_layout);

        beneficiaryName = (EditText) v.findViewById(R.id.beneficiary_name_value);
        bankName = (EditText) v.findViewById(R.id.bank_name_value);
        branch = (EditText) v.findViewById(R.id.branch_value);
        accountNumber = (EditText) v.findViewById(R.id.account_number_value);
        ifscCode = (EditText) v.findViewById(R.id.ifsc_value);

        save = (Button) v.findViewById(R.id.bank_edit_save);
        back = (Button) v.findViewById(R.id.bank_edit_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        if(bd != null){
            if(bd.getBeneficiaryName().equals("Not Availalbe"))
                bd.setBeneficiaryName("");
            if(bd.getAccountNumber().equals("Not Availalbe"))
                bd.setAccountNumber("");
            if(bd.getBankName().equals("Not Availalbe"))
                bd.setBankName("");
            if(bd.getBranch().equals("Not Availalbe"))
                bd.setBranch("");
            if(bd.getIfscCode().equals("Not Availalbe"))
                bd.setIfscCode("");
            beneficiaryName.setText(bd.getBeneficiaryName());
            accountNumber.setText(bd.getAccountNumber());
            bankName.setText(bd.getBankName());
            branch.setText(bd.getBranch());
            ifscCode.setText(bd.getIfscCode());
        }
        return  v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void validateData(){
        boolean modifyData = true ;
        dbeneficiaryName = beneficiaryName.getText().toString();
        daccountNumber = accountNumber.getText().toString();
        dbankName = bankName.getText().toString();
        dbranch = branch.getText().toString();
        difscCode = ifscCode.getText().toString();

        lbeneficiaryName.setError(null);
        lbankName.setError(null);
        lbranch.setError(null);
        laccountNumber.setError(null);
        lifscCode.setError(null);

        if(dbeneficiaryName.equals(bd.getBeneficiaryName()) && daccountNumber.equals(bd.getAccountNumber()) && dbankName.equals(bd.getBankName())
                && dbranch.equals(bd.getBranch()) && difscCode.equals(bd.getIfscCode()))
            Toast.makeText(context, "No Changes to Save", Toast.LENGTH_SHORT).show();
        else{
            if(dbeneficiaryName.equals("")) {
                lbeneficiaryName.setError("Enter Beneficiary Name");
                modifyData = false;
            }
            if(daccountNumber.equals("")){
                laccountNumber.setError("Enter Account Number");
                modifyData = false;
            }
            if(dbankName.equals("")){
                lbankName.setError("Enter Bank Name");
                modifyData = false;
            }
            if(dbranch.equals("")){
                lbranch.setError("Enter Branch");
                modifyData = false;
            }
            if(difscCode.equals("")){
                lifscCode.setError("Enter IFSC Code");
                modifyData = false;
            }

            if(modifyData == true){
                connectToServer();
            }
        }
    }

    public void connectToServer(){
        pd = new ProgressDialog(context);
        pd.setMessage("Updating Bank Details");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId","beneficiaryName","bankName","branch","accountNumber","ifscCode"});
        params.add(new Object[]{sellerId+"", dbeneficiaryName, dbankName, dbranch, daccountNumber, difscCode});
        params.add(Constants.UPDATE_BANK_DETAILS);
        params.add("POST");
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try{
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("issuccess") == true) {
                Toast.makeText(context, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                bd.setBeneficiaryName(dbeneficiaryName);
                bd.setAccountNumber(daccountNumber);
                bd.setBankName(dbankName);
                bd.setBranch(dbranch);
                bd.setIfscCode(difscCode);
            }
            else
                Toast.makeText(context, "Got Error while saving", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
