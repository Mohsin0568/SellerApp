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
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Product;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment implements DataListener {
    Product product ;
    EditText price, quantity, mrp ;
    TextView categories, productId, productTitle, discountText ;
    RadioGroup visibilityGroup ;
    RadioButton enable, disable ;
    Button save, back ;
    Context context ;
    ProgressDialog pd ;
    String priceData, mrpData, quantityData, productIdData, visibleData, discountData ;
    View v ;
    TextInputLayout quantityLayout, priceLayout, mrpLayout ;

    public static ProductDetailsFragment newInstance(String param1, String param2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            product = bundle.getParcelable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_product_details, container, false);
        price = (EditText) v.findViewById(R.id.product_price_value);
        quantity = (EditText) v.findViewById(R.id.product_quantity_value);
        mrp = (EditText) v.findViewById(R.id.product_mrp_value);
        productId = (TextView) v.findViewById(R.id.product_id_value);
        productTitle = (TextView) v.findViewById(R.id.product_title_value);
        categories = (TextView) v.findViewById(R.id.categories_text);
        visibilityGroup = (RadioGroup) v.findViewById(R.id.visibility_radio_group);
        enable = (RadioButton) v.findViewById(R.id.radio_enable);
        disable = (RadioButton) v.findViewById(R.id.radio_disable);
        save = (Button) v.findViewById(R.id.product_edit_save);
        back = (Button) v.findViewById(R.id.product_edit_back);
        discountText = (TextView) v.findViewById(R.id.product_discount_value);
        priceLayout = (TextInputLayout) v.findViewById(R.id.product_price_layout);
        quantityLayout = (TextInputLayout) v.findViewById(R.id.product_quantity_layout);
        mrpLayout = (TextInputLayout) v.findViewById(R.id.product_mrp_layout);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        /*price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                discountText.setText(new Double(calculateDiscount()).intValue()+"%");
            }
        });*/

        /*mrp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                discountText.setText(new Double(calculateDiscount()).intValue()+"%");
            }
        });*/
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                discountText.setText(new Long(calculateDiscount())+"%");
            }
        });
        assignValues();
        return v ;
    }

    public void assignValues(){
        productId.setText(product.getProdId());
        productTitle.setText(product.getProdName());
        quantity.setText(product.getQuantity()+"");
        price.setText(product.getPrice() + "");
        mrp.setText(product.getMrp() + "");
        discountText.setText(product.getDiscount()+"%");
        categories.setText(product.getCatOneName() + " > " + product.getCatTwoName() + " > " + product.getCatThreeName());
        int vis = product.getDisabled();
        if(vis == 0){
            visibilityGroup.check(R.id.radio_enable);
        }
        else
            visibilityGroup.check(R.id.radio_disable);
    }

    public void validateData(){
        quantityData = quantity.getText().toString();
        priceData = price.getText().toString();
        mrpData = mrp.getText().toString();
        productIdData = product.getProdId();
        int visisibilitySelectedId = visibilityGroup.getCheckedRadioButtonId();
        RadioButton selectedRadio = (RadioButton) v.findViewById(visisibilitySelectedId);
        if(selectedRadio.getText().toString().equals("Enable"))
            visibleData = "0" ;
        else
            visibleData = "1";

        if(priceData == null || priceData.equals("")){
            priceLayout.setError("Enter Price");
        }
        else if(quantityData == null || quantityData.equals("")){
            quantityLayout.setError("Enter quantity");
            priceLayout.setError(null);
        }
        else if(mrpData == null || mrpData.equals("")){
            mrpLayout.setError("Enter MRP");
            priceLayout.setError(null);
            quantityLayout.setError(null);
        }
        else if (quantityData.equals("0")){
            priceLayout.setError(null);
            quantityLayout.setError(null);
            mrpLayout.setError(null);
            confirmQuantityZero();
        }
        else if (Double.parseDouble(priceData) > Double.parseDouble(mrpData)){
            quantityLayout.setError(null);
            mrpLayout.setError(null);
            priceLayout.setError("Price should be less than MRP");
        }
        else {
            quantityLayout.setError(null);
            priceLayout.setError(null);
            mrpLayout.setError(null);
            connectToServer();
        }
    }

    public void confirmQuantityZero(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Making quantity zero will not make your product visible");
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectToServer();
            }
        });
        builder.setNegativeButton("Change Quantity", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public long calculateDiscount(){
        if(mrp.getText().toString().equals("") || price.getText().toString().equals("")){
            return 0 ;
        }
        else {
            long mrp1 = Long.parseLong(mrp.getText().toString());
            long price1 = Long.parseLong(price.getText().toString());
            long diff = mrp1 - price1;
            if (diff == 0)
                return 0;
            else
                return (diff * 100) / mrp1;
        }
    }

    public void connectToServer(){
        discountData = new Double(calculateDiscount()).intValue() + "";
        discountText.setText(discountData + "%");
        pd = new ProgressDialog(context);
        pd.setTitle("Updating Product");
        pd.setCancelable(false);
        pd.show();
        HttpWorker httpWorker = new HttpWorker(this, pd, context);
        httpWorker.execute(constructParameters());
    }

    public List constructParameters(){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"productId", "quantity", "price", "mrp", "discount", "disabled", "productName", "productDescription", "approval","propAndValues","deleteImages"});
        params.add(new Object[]{productIdData, quantityData, priceData, mrpData, discountData, visibleData, product.getProdName(), product.getProdDesc(), product.getIsApproved(),new JSONArray(),"1"}); // deleteImages 1 means don't delete images
        params.add(Constants.UPDATE_PRODUCT);
        params.add("POST");
        return  params;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity ;
    }

    @Override
    public void dataDownloaded(String response) {
        pd.cancel();
        try{
            JSONObject json = new JSONObject(response);
            boolean res = json.getBoolean("issuccess");
            if(res == true){
                getFragmentManager().popBackStackImmediate();
                Toast.makeText(context, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Product Details not Updated", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
