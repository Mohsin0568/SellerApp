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
import com.example.mohmurtu.registration.model.seller.PersonalDetails;
import com.example.mohmurtu.registration.model.seller.VATDetails;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EditCompanyInformation extends Fragment implements DataListener {

    TextInputLayout lpanNumber, lvatNumber, lcstNumber ;
    EditText panNumber, vatNumber, cstNumber ;
    String dpanNumber, dvatNumber, dcstNumber ;
    Button save, back ;
    Context context ;
    VATDetails vd ;
    int sellerId;
    ProgressDialog pd ;

    public static EditCompanyInformation newInstance(String param1, String param2) {
        EditCompanyInformation fragment = new EditCompanyInformation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EditCompanyInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            vd = (VATDetails) bundle.getParcelable("company");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_company_information, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("VAT Details");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);

        lpanNumber = (TextInputLayout) v.findViewById(R.id.pan_number_layout);
        lvatNumber = (TextInputLayout) v.findViewById(R.id.vat_number_layout);
        lcstNumber  = (TextInputLayout) v.findViewById(R.id.cst_number_layout);

        panNumber = (EditText) v.findViewById(R.id.pan_number_value);
        vatNumber = (EditText) v.findViewById(R.id.vat_number_value);
        cstNumber = (EditText) v.findViewById(R.id.cst_number_value);

        save = (Button) v.findViewById(R.id.company_edit_save);
        back = (Button) v.findViewById(R.id.company_edit_back);

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

        if(vd != null){
            if(vd.getPanNumber().equals("Not Available"))
                vd.setPanNumber("");
            if(vd.getVatNumber().equals("Not Available"))
                vd.setVatNumber("");
            if(vd.getCstNumber().equals("Not Available"))
                vd.setCstNumber("");

            panNumber.setText(vd.getPanNumber());
            vatNumber.setText(vd.getVatNumber());
            cstNumber.setText(vd.getCstNumber());
        }

        return v ;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void validateData(){
        boolean modifyData = true ;
        dpanNumber = panNumber.getText().toString();
        dvatNumber = vatNumber.getText().toString();
        dcstNumber = cstNumber.getText().toString();

        lpanNumber.setError(null);
        lvatNumber.setError(null);
        lcstNumber.setError(null);

        if(dpanNumber.equals(vd.getPanNumber()) && dvatNumber.equals(vd.getVatNumber()) && dcstNumber.equals(vd.getCstNumber()))
            Toast.makeText(context, "No Changes to Save", Toast.LENGTH_SHORT).show();
        else{
            if(dpanNumber.equals("")){
                lpanNumber.setError("Enter PAN Number");
                modifyData = false;
            }
            if(dvatNumber.equals("")){
                lvatNumber.setError("Enter VAT Number");
                modifyData = false;
            }
            if(dcstNumber.equals("")){
                lcstNumber.setError("Enter CST Number");
                modifyData = false;
            }

            if(modifyData == true){
                connectToServer();
            }
        }
    }

    public void connectToServer(){
        pd = new ProgressDialog(context);
        pd.setMessage("Updating VAT Details");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId","vatNumber", "cstNumber", "panNumber"});
        params.add(new Object[]{sellerId+"", dvatNumber, dcstNumber, dpanNumber});
        params.add(Constants.UPDATE_VAT_DETAILS);
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
                vd.setPanNumber(dpanNumber);
                vd.setVatNumber(dvatNumber);
                vd.setCstNumber(dcstNumber);
            }
            else
                Toast.makeText(context, "Got Error while saving", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
