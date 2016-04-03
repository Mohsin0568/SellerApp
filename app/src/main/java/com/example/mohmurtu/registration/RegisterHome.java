package com.example.mohmurtu.registration;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterHome extends Fragment {

    EditText companyName ;
    Button continueButton ;
    CheckBox termsCheck ;
    TextView termsText ;
    TextInputLayout layoutCompanyName ;
    Context context ;

    public static RegisterHome newInstance() {
        RegisterHome fragment = new RegisterHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_home,container,false);
//        getActivity().getActionBar().setTitle("Register");
        termsText = (TextView) v.findViewById(R.id.terms_text);
        companyName = (EditText) v.findViewById(R.id.company_name);
        continueButton = (Button) v.findViewById(R.id.continue_button);
        termsCheck = (CheckBox) v.findViewById(R.id.terms_checkbox);
        layoutCompanyName = (TextInputLayout) v.findViewById(R.id.layout_edit_company);
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Clicked");
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedContinue();
            }
        });
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        ((MainActivity)activity).getSupportActionBar().setTitle("Registration");
        ((MainActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void clickedContinue(){
        boolean termsState = termsCheck.isChecked();
        String compName = companyName.getText().toString();
        if(termsState == false){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setMessage("Accept Terms and Conditions");
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertBuilder.show();
        }

        else if(compName == null || compName.equals(""))
            layoutCompanyName.setError("Provide Company Name");
        else{
            layoutCompanyName.setErrorEnabled(false);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            CompanyInformation cinfo = CompanyInformation.newInstance(compName,getActivity());
            transaction.replace(R.id.fragment_body, cinfo);
            transaction.addToBackStack("Back");
            transaction.commit();
        }
    }

    public interface OnFragmentInteractionListener {
        public void defineInterfaceMethod();
    }
}
