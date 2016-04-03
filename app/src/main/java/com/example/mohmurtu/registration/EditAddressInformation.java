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
import com.example.mohmurtu.registration.model.seller.AddressDetails;
import com.example.mohmurtu.registration.model.seller.PersonalDetails;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EditAddressInformation extends Fragment implements DataListener {

    TextInputLayout laddress, lpinCode, lcity, lstate ;
    EditText address, pinCode, city, state ;
    String daddress, dpinCode, dcity, dstate ;
    Button save, back ;
    Context context ;
    AddressDetails ad ;
    int sellerId;
    ProgressDialog pd ;

    public static EditAddressInformation newInstance(String param1, String param2) {
        EditAddressInformation fragment = new EditAddressInformation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public EditAddressInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            ad = (AddressDetails) bundle.getParcelable("address");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_address_information, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Address Details");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);

        laddress = (TextInputLayout) v.findViewById(R.id.address_layout) ;
        lpinCode = (TextInputLayout) v.findViewById(R.id.address_pincode_layout);
        lcity = (TextInputLayout) v.findViewById(R.id.address_city_layout);
        lstate = (TextInputLayout) v.findViewById(R.id.address_state_layout);

        address = (EditText) v.findViewById(R.id.address_value);
        pinCode = (EditText) v.findViewById(R.id.address_pincode_value);
        city = (EditText) v.findViewById(R.id.address_city_value);
        state = (EditText) v.findViewById(R.id.address_state_value);

        save = (Button) v.findViewById(R.id.address_edit_save);
        back = (Button) v.findViewById(R.id.address_edit_back);

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

        if(ad != null){
            if(ad.getAddress().equals("Not Availalbe"))
                ad.setAddress("");
            if(ad.getPinCode().equals("Not Availalbe"))
                ad.setPinCode("");
            if(ad.getCity().equals("Not Availalbe"))
                ad.setCity("");
            if(ad.getState().equals("Not Availalbe"))
                ad.setState("");
            address.setText(ad.getAddress());
            pinCode.setText(ad.getPinCode());
            city.setText(ad.getCity());
            state.setText(ad.getState());
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

        daddress = address.getText().toString();
        dcity = city.getText().toString();
        dstate = state.getText().toString();
        dpinCode = pinCode.getText().toString();

        laddress.setError(null);
        lcity.setError(null);
        lstate.setError(null);
        lpinCode.setError(null);

        if(daddress.equals(ad.getAddress()) && dcity.equals(ad.getCity()) && dstate.equals(ad.getState()) && dpinCode.equals(ad.getPinCode()))
            Toast.makeText(context, "No Changes to Save", Toast.LENGTH_SHORT).show();
        else{
            if(daddress.equals("")){
                laddress.setError("Enter Address");
                modifyData = false;
            }
            if(dpinCode.equals("")){
                lpinCode.setError("Enter Pin Code");
                modifyData = false;
            }
            if(dcity.equals("")){
                lcity.setError("Enter City");
                modifyData = false;
            }
            if(dstate.equals("")){
                lstate.setError("Enter State");
                modifyData = false ;
            }

            if(modifyData == true){
                connectToServer();
            }
        }
    }

    public void connectToServer(){
        pd = new ProgressDialog(context);
        pd.setMessage("Updating Address Details");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId","address","pinCode","city","state"});
        params.add(new Object[]{sellerId+"", daddress, dpinCode, dcity, dstate});
        params.add(Constants.UPDATE_ADDRESS_DETAILS);
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
                ad.setAddress(daddress);
                ad.setCity(dcity);
                ad.setState(dstate);
                ad.setPinCode(dpinCode);
            }
            else
                Toast.makeText(context, "Got Error while saving", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
