package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.seller.PersonalDetails;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditPersonalInformation extends Fragment implements DataListener {

    TextInputLayout lname, lemail, lphone, laltPhone ;
    EditText name, email, phone, altPhone;
    String dname, demail, dphone, daltPhone ;
    Button save, back ;
    Context context ;
    PersonalDetails pd ;
    int sellerId;
    ProgressDialog pd1 ;

    public static EditPersonalInformation newInstance(String param1, String param2) {
        EditPersonalInformation fragment = new EditPersonalInformation();
        Bundle args = new Bundle();
        return fragment;
    }

    public EditPersonalInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle bundle = getArguments();
            pd = (PersonalDetails) bundle.getParcelable("personal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_personal_information, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Personal Details");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        lname = (TextInputLayout) v.findViewById(R.id.personal_name_layout);
        lemail = (TextInputLayout) v.findViewById(R.id.personal_email_layout);
        lphone = (TextInputLayout) v.findViewById(R.id.personal_phone_layout);
        laltPhone = (TextInputLayout) v.findViewById(R.id.personal_alternate_layout);

        name = (EditText) v.findViewById(R.id.personal_name_value);
        email = (EditText) v.findViewById(R.id.personal_email_value);
        phone = (EditText) v.findViewById(R.id.personal_phone_value);
        altPhone = (EditText) v.findViewById(R.id.personal_alternate_value);

        save = (Button) v.findViewById(R.id.personal_edit_save);
        back = (Button) v.findViewById(R.id.personal_edit_back);

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

        if(pd != null){
            if(pd.getAltPhoneNumber().equals("Not Availalbe"))
                pd.setAltPhoneNumber("");
            if(pd.getSellerName().equals("Not Availalbe"))
                pd.setSellerName("");
            if(pd.getPhoneNumber().equals("Not Availalbe"))
                pd.setPhoneNumber("");
            if(pd.getEmail().equals("Not Availalbe"))
                pd.setEmail("");
            name.setText(pd.getSellerName());
            email.setText(pd.getEmail());
            phone.setText(pd.getPhoneNumber());
            altPhone.setText(pd.getAltPhoneNumber());
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
        dname = name.getText().toString().trim();
        demail = email.getText().toString().trim();
        dphone = phone.getText().toString().trim();
        daltPhone = altPhone.getText().toString().trim();

        lname.setError(null);
        lemail.setError(null);
        lphone.setError(null);
        laltPhone.setError(null);

        String oldAltPhone = pd.getAltPhoneNumber();
        if(oldAltPhone == null)
            oldAltPhone = "";

        if(dname.equals(pd.getSellerName()) && demail.equals(pd.getEmail()) && dphone.equals(pd.getPhoneNumber()) && daltPhone.equals(oldAltPhone)){
            Toast.makeText(context, "No Changes to Save", Toast.LENGTH_SHORT).show();
        }
        else {
            if (dname.equals("")) {
                lname.setError("Enter Name");
                modifyData = false;
            }
            if (!(Patterns.EMAIL_ADDRESS.matcher(demail).matches())) {
                lemail.setError("Email is not valid");
                modifyData = false;
            }
            if(dphone.length() != 10){
                lphone.setError("Phone Number should be 10 digits");
                modifyData = false;
            }

            if(modifyData == true){
                connectToServer();
            }
        }

    }

    public void connectToServer(){
        pd1 = new ProgressDialog(context);
        pd1.setMessage("Updating Personal Details");
        pd1.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd1, context);
        http.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId","name","email","phoneNumber","altPhoneNumber"});
        params.add(new Object[]{sellerId+"", dname, demail, dphone, daltPhone});
        params.add(Constants.UPDATE_PERSONAL_DETAILS);
        params.add("POST");
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        pd1.cancel();
        try{
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("issuccess") == true) {
                Toast.makeText(context, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                pd.setSellerName(dname);
                pd.setEmail(demail);
                pd.setPhoneNumber(dphone);
                pd.setAltPhoneNumber(daltPhone);
            }
            else
                Toast.makeText(context, "Got Error while saving", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
