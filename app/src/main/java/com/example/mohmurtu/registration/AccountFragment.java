package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.seller.SellerAllDetails;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements DataListener{

    Context context ;
    Button backButton ;
    TextView name, password, phoneNumber, altNumber, emailId;
    TextView address, city, state, pinCode;
    TextView companyName, panNumber, vatNumber, cstNumber;
    TextView beneficiaryName, accountNumber, bankName, branch, ifscCode;

    String dname, dphoneNumber, daltNumber, demailId ;
    String daddress, dcity, dstate, dpinCode;
    String dcompanyName, dpanNumber, dvatNumber, dcstNumber ;
    String dbeneficiaryName, daccountNumber, dbankName, dbranch, difscCode;

    ImageView personalIcon, addressIcon, companyIcon, bankIcon;
    int sellerId ;
    ProgressDialog pd ;
    SellerAllDetails seller ;

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Account Details");
        backButton = (Button) v.findViewById(R.id.account_back_button);
        personalIcon = (ImageView) v.findViewById(R.id.account_personal_edit_icon);
        addressIcon = (ImageView) v.findViewById(R.id.account_address_edit_icon);
        companyIcon = (ImageView) v.findViewById(R.id.account_company_edit_icon);
        bankIcon = (ImageView) v.findViewById(R.id.account_bank_edit_icon);

        name = (TextView) v.findViewById(R.id.account_personal_name);
        password = (TextView) v.findViewById(R.id.account_personal_change_password);
        phoneNumber = (TextView) v.findViewById(R.id.account_personal_phone_number);
        altNumber = (TextView) v.findViewById(R.id.account_personal_alt_phone);
        emailId = (TextView) v.findViewById(R.id.account_personal_email);

        address = (TextView) v.findViewById(R.id.account_address);
        city = (TextView) v.findViewById(R.id.account_address_city);
        state = (TextView) v.findViewById(R.id.account_address_state);
        pinCode = (TextView) v.findViewById(R.id.account_address_pincode);

        companyName = (TextView) v.findViewById(R.id.account_company_name);
        panNumber = (TextView) v.findViewById(R.id.account_company_pan);
        vatNumber = (TextView) v.findViewById(R.id.account_company_vat_number);
        cstNumber = (TextView) v.findViewById(R.id.account_company_cst_number);

        beneficiaryName = (TextView) v.findViewById(R.id.account_bank_beneficiary_name);
        accountNumber = (TextView) v.findViewById(R.id.account_bank_account_number);
        bankName = (TextView) v.findViewById(R.id.account_bank_name);
        branch = (TextView) v.findViewById(R.id.account_bank_branch);
        ifscCode = (TextView) v.findViewById(R.id.account_bank_ifsc);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                DashboardFragment frag = new DashboardFragment();
                transaction.replace(R.id.login_fragment, frag);
                transaction.commit();
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                PasswordChangeFragment frag = new PasswordChangeFragment();
                transaction.replace(R.id.login_fragment, frag);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        personalIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                EditPersonalInformation frag = new EditPersonalInformation();
                Bundle bundle = new Bundle();
                bundle.putParcelable("personal", seller.getPersonal());
                frag.setArguments(bundle);
                transaction.replace(R.id.login_fragment, frag);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        addressIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                EditAddressInformation frag = new EditAddressInformation();
                Bundle bundle = new Bundle();
                bundle.putParcelable("address", seller.getAddress());
                frag.setArguments(bundle);
                transaction.replace(R.id.login_fragment, frag);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        bankIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                EditBankInformation frag = new EditBankInformation();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bank", seller.getBank());
                frag.setArguments(bundle);
                transaction.replace(R.id.login_fragment, frag);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });

        companyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                EditCompanyInformation frag = new EditCompanyInformation();
                Bundle bundle = new Bundle();
                bundle.putParcelable("company", seller.getVat());
                frag.setArguments(bundle);
                transaction.replace(R.id.login_fragment, frag);
                transaction.addToBackStack("back");
                transaction.commit();
            }
        });
        connectToServer();
        return v;
    }

    public void connectToServer(){
        pd = new ProgressDialog(context);
        pd.setTitle("Getting Data");
        pd.setCancelable(false);
        HttpWorker worker = new HttpWorker(this, pd, context);
        worker.execute(constructParameters());
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"sellerId"});
        params.add(new Object[]{sellerId+""});
        params.add(Constants.GET_ACCOUNT_DETAILS);
        params.add("GET");
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
            ObjectMapper mapper = new ObjectMapper();
            seller = mapper.readValue(response, SellerAllDetails.class);
            System.out.println(seller.getPersonal().getCompanyName() + " " + seller.getAddress().getCity() + " " + seller.getBank().getBankName() + " " + seller.getVat().getCstNumber());
            name.setText(seller.getPersonal().getSellerName());
            phoneNumber.setText(seller.getPersonal().getPhoneNumber());
            password.setText("Change Password");
            String alt = seller.getPersonal().getAltPhoneNumber();
            if(alt == null)
                altNumber.setText("Not Available");
            else
                altNumber.setText(alt);
            emailId.setText(seller.getPersonal().getEmail());

            address.setText(seller.getAddress().getAddress());
            city.setText(seller.getAddress().getCity());
            state.setText(seller.getAddress().getState());
            pinCode.setText(seller.getAddress().getPinCode());

            companyName.setText(seller.getPersonal().getCompanyName());
            panNumber.setText(seller.getVat().getPanNumber());
            vatNumber.setText(seller.getVat().getVatNumber());
            cstNumber.setText(seller.getVat().getCstNumber());

            beneficiaryName.setText(seller.getBank().getBeneficiaryName());
            accountNumber.setText(seller.getBank().getAccountNumber());
            bankName.setText(seller.getBank().getBankName());
            branch.setText(seller.getBank().getBranch());
            ifscCode.setText(seller.getBank().getIfscCode());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
