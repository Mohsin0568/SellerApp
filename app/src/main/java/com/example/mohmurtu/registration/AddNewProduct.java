package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohmurtu.registration.ImagesGallery.AlbumImagesView;
import com.example.mohmurtu.registration.adapters.ValuesAdapter;
import com.example.mohmurtu.registration.imagesUtil.ImageInternalFetcher;
import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.listeners.ImageDataListener;
import com.example.mohmurtu.registration.model.Product;
import com.example.mohmurtu.registration.model.Properties;
import com.example.mohmurtu.registration.model.Property;
import com.example.mohmurtu.registration.model.Value;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpImageWorker;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddNewProduct extends Fragment implements DataListener, ImageDataListener{

    String catOneName, catTwoName, catThreeName, catThreeId;
    Context context ;
    Button coverImageButton, addProduct, multiImagesButton ;
    ImageView coverImage ;
    EditText title, quantity, price, description, brand, mrp ;
    String dtitle, dquantity, dprice, ddescription, dbrand, dmrp, ddiscount ;
    TextInputLayout ltitle, lquantity, lprice, ldescription, lbrand, lmrp ;
    TextView discount ;
    LinearLayout dynamiclayout ;
    ProgressDialog pd ;
    boolean gettingProperties = true ;
    HashMap<String,Property> selectionPropValues;
    private ImageLoader imageLoader ;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_MULTI_IMAGE = 2 ;
    String coverImagePath="";
    List otherImagesPaths ;
    int sellerId ;
    public static boolean imageUploadStatus = false;
    private ViewGroup mSelectedImagesContainer, mSelectedMainImageContainer;
    int noOfOtherImagesSelected = 0, noOfOtherImagesUploaded = 0 ;
    int prodId ;
    boolean updateProduct = false ;
    Product productForUpdate ;
    public static AddNewProduct newInstance(String catOneName, String catTwoName, String catThreeName, String catThreeId, Product prods) {
        AddNewProduct fragment = new AddNewProduct();
        Bundle args = new Bundle();
        args.putString("catOneName", catOneName);
        args.putString("catTwoName", catTwoName);
        args.putString("catThreeName", catThreeName);
        args.putString("catThreeId", catThreeId);
        args.putParcelable("product", prods);
        fragment.setArguments(args);
        return fragment;
    }

    public AddNewProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catOneName = getArguments().getString("catOneName");
            catTwoName = getArguments().getString("catTwoName");
            catThreeName = getArguments().getString("catThreeName");
            catThreeId = getArguments().getString("catThreeId");
            productForUpdate = getArguments().getParcelable("product");
            if(productForUpdate != null)
                updateProduct = true ; // Fragment opens for product update
            else
                updateProduct = false;
        }
        selectionPropValues = new HashMap<String,Property>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        otherImagesPaths = null;
        coverImagePath = "";
        View v = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        ((ActivityHome)context).getSupportActionBar().setTitle("Upload Product");
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        mSelectedImagesContainer = (ViewGroup) v.findViewById(R.id.selected_photos_container);
        mSelectedMainImageContainer = (ViewGroup) v.findViewById(R.id.main_selected_photos_container);
        TextView tv = (TextView) v.findViewById(R.id.text_header);
        tv.setText(catOneName + " --> " + catTwoName + " --> " + catThreeName);
        dynamiclayout = (LinearLayout) v.findViewById(R.id.dynamiclay);
        title = (EditText) v.findViewById(R.id.edit_title);
        quantity = (EditText) v.findViewById(R.id.edit_quantity);
        price = (EditText) v.findViewById(R.id.edit_price);
        mrp = (EditText) v.findViewById(R.id.edit_mrp);
        description = (EditText) v.findViewById(R.id.edit_desc);
        brand = (EditText) v.findViewById(R.id.edit_brand);

        ltitle = (TextInputLayout) v.findViewById(R.id.layout_edit_title);
        lquantity = (TextInputLayout) v.findViewById(R.id.layout_edit_quantity);
        lprice = (TextInputLayout) v.findViewById(R.id.layout_edit_price);
        lmrp = (TextInputLayout) v.findViewById(R.id.layout_edit_mrp);
        ldescription = (TextInputLayout) v.findViewById(R.id.layout_edit_desc);
        lbrand = (TextInputLayout) v.findViewById(R.id.layout_edit_brand);
        gettingProperties = true;
        discount = (TextView) v.findViewById(R.id.text_discount);
        connectToServer("Loading Properties");

        coverImageButton = (Button) v.findViewById(R.id.single_image_button);
        coverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 selectCoverImage();
            }
        });

        addProduct = (Button) v.findViewById(R.id.upload_product);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductDetails();
            }
        });

        multiImagesButton = (Button) v.findViewById(R.id.multi_image_button);
        multiImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMultipleImages();
            }
        });

        /*price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String p = price.getText().toString();
                String m = mrp.getText().toString();
                if(!(p.equals("")) && !(m.equals(""))){
                    System.out.println("Inside calculation percentage");
                    ddiscount = calculateDiscount(Long.parseLong(m), Long.parseLong(p)) + "";
                    discount.setText("Discount: " + ddiscount + "%");
                }
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
                String p = price.getText().toString();
                String m = mrp.getText().toString();
                if(!(p.equals("")) && !(m.equals(""))){
                    System.out.println("Inside calculation percentage");
                    ddiscount = calculateDiscount(Long.parseLong(m), Long.parseLong(p)) + "";
                    discount.setText("Discount: " + ddiscount + "%");
                }
            }
        });

        if(updateProduct == true){
            assignValuesTOUIForUpdate();
        }


        return v;
    }

    public void assignValuesTOUIForUpdate(){
        title.setText(productForUpdate.getProdName());
        quantity.setText(productForUpdate.getQuantity()+"");
        price.setText(productForUpdate.getPrice()+"");
        description.setText(productForUpdate.getProdDesc());
        mrp.setText(productForUpdate.getMrp()+"");
        discount.setText("Discount: " + calculateDiscount(productForUpdate.getMrp(), productForUpdate.getPrice())+"%");
        addProduct.setText("Submit For Approval");
    }

    public long calculateDiscount(long mrp, long price){
        long diff = mrp - price ;
        if(diff == 0)
            return 0 ;
        else
            return (diff*100)/mrp;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity ;
    }

    public void validateProductDetails(){
        dtitle = title.getText().toString();
        ddescription = description.getText().toString();
        dquantity = quantity.getText().toString();
        dprice = price.getText().toString();
        dbrand = brand.getText().toString();
        dmrp = mrp.getText().toString();
        long priceD = dprice.equals("") ? 0 : Long.parseLong(dprice);
        long mrpD = dmrp.equals("") ? 0 : Long.parseLong(dmrp);

        if(dtitle.equals("")){
            ltitle.setError("Enter Title");
        }
        else if(dquantity.equals("")){
            lquantity.setError("Enter Quantity");
        }
        else if(dprice.equals("")){
            lprice.setError("Enter Price");
        }
        else if(dmrp.equals("")){
            lmrp.setError("Enter MRP");
        }
        else if(dbrand.equals("")){
            lbrand.setError("Enter Brand");
        }
        else if(ddescription.equals("")){
            ldescription.setError("Enter Description");
        }
        else if(mrpD < priceD){
            lmrp.setError("MRP should be more or equal to Price");
            lmrp.setErrorEnabled(true);
        }
        else if(coverImagePath == null || coverImagePath.equals("")){
            ltitle.setError(null);
            lquantity.setError(null);
            lprice.setError(null);
            lmrp.setError(null);
            lbrand.setError(null);
            ldescription.setError(null);
            Toast.makeText(context, "Select Cover Image", Toast.LENGTH_SHORT).show();
        }
        else if(otherImagesPaths == null || otherImagesPaths.size()<1){
            ltitle.setError(null);
            lquantity.setError(null);
            lprice.setError(null);
            lmrp.setError(null);
            lbrand.setError(null);
            ldescription.setError(null);
            Toast.makeText(context, "Select atleast one other Image", Toast.LENGTH_SHORT).show();
        }
        else{
            ltitle.setError(null);
            lquantity.setError(null);
            lprice.setError(null);
            lbrand.setError(null);
            lmrp.setError(null);
            ldescription.setError(null);
            gettingProperties = false;
            ddiscount = calculateDiscount(mrpD, priceD)+"";
            connectToServer("Uploading Product");
        }
    }

    public void selectMultipleImages(){
        Intent intent = new Intent(context,AlbumImagesView.class);
        startActivityForResult(intent,RESULT_MULTI_IMAGE);
    }

    public void selectCoverImage(){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
//        imagesLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            coverImagePath = cursor.getString(columnIndex);
            mSelectedMainImageContainer.removeAllViews();
            mSelectedMainImageContainer.setVisibility(View.VISIBLE);
            View imageHolder = LayoutInflater.from(context).inflate(R.layout.image_scroll_view, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            Uri uri = Uri.fromFile(new File(coverImagePath));
            ImageInternalFetcher imageFetcher = new ImageInternalFetcher(getActivity(), 500);
            imageFetcher.loadImage(uri,thumbnail);
            mSelectedMainImageContainer.addView(imageHolder);
            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
//            coverImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            coverImage.setVisibility(View.VISIBLE);
            coverImageButton.setText("Change Cover Image");
        }
        else if (requestCode == RESULT_MULTI_IMAGE){
            if(data != null) {
                multiImagesButton.setText("Change Other Images");
                otherImagesPaths = data.getIntegerArrayListExtra("urls");
                noOfOtherImagesSelected = otherImagesPaths.size();
                mSelectedImagesContainer.removeAllViews();
                ImageInternalFetcher imageFetcher = new ImageInternalFetcher(getActivity(), 500);
                if (otherImagesPaths.size() > 1)
                    mSelectedImagesContainer.setVisibility(View.VISIBLE);
                for (Object x : otherImagesPaths) {
                    Log.d("URLPath", x.toString());
                    View imageHolder = LayoutInflater.from(context).inflate(R.layout.image_scroll_view, null);
                    ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
                    Uri uri = Uri.fromFile(new File(x.toString()));
                    imageFetcher.loadImage(uri, thumbnail);
                    mSelectedImagesContainer.addView(imageHolder);
                    int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                    int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                    thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
                }
            }

        }
    }

    public List<Object> constructParameters(){
        List<Object> params = new ArrayList<Object>();
        if(gettingProperties) {
            params.add(new String[]{"categoryThreeId"});
            params.add(new Object[]{catThreeId});
            params.add(Constants.GET_PROPERTIES_URL);
            params.add("GET");
        }
        else if(updateProduct){
            JSONArray jsonArray = null;
            try {
                System.out.println("Inside update product construct parameters");
                jsonArray = new JSONArray();
                Iterator it = selectionPropValues.entrySet().iterator();
                int i = 0 ;
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Property property = (Property)pair.getValue();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("prodPropertyId", productForUpdate.getPropAndValues().get(i).getProdPropertyId());
                    jsonObject.put("propName", property.getPropertyName());
                    jsonObject.put("propId", property.getPropertyId());
                    jsonObject.put("propValueId", property.getPropValues().get(0).getValueId());
                    jsonObject.put("propValue", property.getPropValues().get(0).getValueName());
                    jsonArray.put(jsonObject);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.add(new String[]{"productId", "catId", "productName", "productDescription", "price", "mrp", "disabled", "discount", "quantity", "approval", "propAndValues","deleteImages"});
            params.add(new Object[]{productForUpdate.getProdId(), productForUpdate.getCatId(), dtitle, ddescription, dprice, dmrp, "0", ddiscount, dquantity, "1", jsonArray, "0"}); // delete images 0 means current images should be deleted
            params.add(Constants.UPDATE_PRODUCT);
            params.add("POST");
        }
        else{
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray();
                Iterator it = selectionPropValues.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Property property = (Property)pair.getValue();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("propertyName", property.getPropertyName());
                    jsonObject.put("propertyId", property.getPropertyId());
                    jsonObject.put("valueId", property.getPropValues().get(0).getValueId());
                    jsonObject.put("valueName", property.getPropValues().get(0).getValueName());
                    jsonArray.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.add(new String[]{"sellerId", "productName", "productDescription", "price", "mrp", "discount", "quantity", "categoryThreeId", "propAndValues"});
            params.add(new Object[]{sellerId, dtitle, ddescription, dprice, dmrp, ddiscount, dquantity, catThreeId, jsonArray});
            params.add(Constants.UPLOAD_PRODUCT_URL);
            params.add("POST");
        }
        return params;
    }

    public List<Object> constructImageParams(int imageNumber, String fileData, int productId){
        List<Object> params = new ArrayList<Object>();
        params.add(new String[]{"catOne","catTwo","catThree","productId","Imagenumber","fileData"});
        params.add(new Object[]{catOneName, catTwoName, catThreeName, productId, imageNumber, fileData});
        params.add(Constants.UPLOAD_IMAGE_URL);
        params.add("POST");
        return params;
    }

    public void connectToServer(String message){
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCancelable(false);
        HttpWorker httpWorker = new HttpWorker(this,pd,context);
        httpWorker.execute(constructParameters());
    }

    public void connectToImageServer(int prodId){
        System.out.println("Uploading Cover image");
        String imageData = encodeString(coverImagePath);
        List<Object> params = constructImageParams(1, imageData, prodId);
        HttpImageWorker imageWorker = new HttpImageWorker(this,context);
        imageWorker.execute(params);
    }

    public String encodeString(String path) {

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    @Override
    public void dataDownloaded(String response) {
        try {
            if (gettingProperties) {
                pd.cancel();
                gettingProperties = false ;
                ObjectMapper mapper = new ObjectMapper();
                Properties properties = mapper.readValue(response, Properties.class);
                if(properties.issuccess() == true){
                    List<Property> props = properties.getPropAndValues();
                    for(Property prop : props){
                        LinearLayout contentLay = new LinearLayout(getActivity());
                        contentLay.setOrientation(LinearLayout.HORIZONTAL);
                        contentLay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        TextView headingView = new TextView(getActivity());
                        headingView.setTextSize(16);
//                        headingView.setTextColor(getActivity().getResources().getColor(R.color.black_color));
                        headingView.setPadding(10, 20, 10, 0);
                        headingView.setGravity(Gravity.CENTER_VERTICAL);
                        headingView.setText(prop.getPropertyName());

                        contentLay.addView(headingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        Spinner spinner = (Spinner) getActivity().getLayoutInflater().inflate(R.layout.layout_spinner, null);
                        contentLay.addView(spinner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ValuesAdapter valuesAdapter = new ValuesAdapter(getActivity(), prop.getPropValues(),prop.getPropertyName(),prop.getPropertyId());
                        valuesAdapter.setDropDownViewResource(R.layout.adapter_value_item);
                        spinner.setAdapter(valuesAdapter);

                        spinner.setSelected(false);

                        dynamiclayout.addView(contentLay,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        Property propertyObj = new Property();
                        propertyObj.setPropertyName(prop.getPropertyName());
                        propertyObj.setPropertyId(prop.getPropertyId());
                        ArrayList<Value> values= new ArrayList<Value>();
                        Value value = new Value();
                        value.setValueId(prop.getPropValues().get(0).getValueId());
                        value.setValueName(prop.getPropValues().get(0).getValueName());
                        values.add(value);
                        prop.setPropValues(values);
                        selectionPropValues.put(prop.getPropertyName(), propertyObj);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    Value value = (Value) view.getTag();
                                    String selection = value.getValueName();
                                    Property property = new Property();
                                    property.setPropertyName(value.getPropertyName());
                                    property.setPropertyId(value.getPropertyId());
                                    ArrayList<Value> values = new ArrayList<Value>();
                                    values.add(value);
                                    property.setPropValues(values);
                                    selectionPropValues.put(value.getPropertyName(), property);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
            else if(updateProduct){
                if(response == null || response.equals("")){
                    Log.d("Error", "Response is null");
                    pd.cancel();
                }
                else{
                    JSONObject json = new JSONObject(response);
                    boolean issucess = json.getBoolean("issuccess");
                    if (issucess == true) {
                        connectToImageServer(Integer.parseInt(productForUpdate.getProdId()));
                    }
                }
            }
            else{
                if(response == null || response.equals("")) {
                    pd.cancel();
                    Log.d("Error","Response is null");
                }
                else{
                    JSONObject json = new JSONObject(response);
                    boolean issucess = json.getBoolean("issuccess");
                    if (issucess == true) {
                        this.prodId = json.getInt("productId");
                        System.out.println("Product id is " + prodId);
                        connectToImageServer(prodId);
                    }
                }
            }
        }
        catch(Exception e){
            Log.d("Exception","Got exception while getting properties"+e);
            e.printStackTrace();
        }
    }

    public void imageDataListener(String response){
        try{
            JSONObject json = new JSONObject(response);
            imageUploadStatus = json.getBoolean("issuccess");
            if(noOfOtherImagesUploaded < noOfOtherImagesSelected){
                String imageEncData = encodeString(otherImagesPaths.get(noOfOtherImagesUploaded).toString());
                List<Object> params = constructImageParams(noOfOtherImagesUploaded+2, imageEncData, prodId);
                HttpImageWorker imageWorker = new HttpImageWorker(this,context);
                System.out.println("Uploading image number " + (noOfOtherImagesUploaded+2) );
                noOfOtherImagesUploaded++;
                imageWorker.execute(params);
            }
            else {
                pd.cancel();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                if(imageUploadStatus == true) {
                    if(updateProduct){
                        Toast.makeText(context, "Product submitted for approval", Toast.LENGTH_SHORT);
                        getFragmentManager().popBackStackImmediate();
                    }
                    else {
                        alertDialogBuilder.setMessage("Do you want to upload another product in same Category");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                resetAllObjects();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                resetAllObjects();
                                Fragment fragment = new CategoryOneList();
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.login_fragment, fragment);
                                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                transaction.commit();
                            }
                        });
                        AlertDialog dialog = alertDialogBuilder.create();
                        dialog.show();
                    }
                }
                else{
                    alertDialogBuilder.setMessage("Product not uploaded, Try again");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            resetAllObjects();
                        }
                    });
                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetAllObjects(){
        coverImageButton.setText("Select Cover Image");
        multiImagesButton.setText("Select Other Images");
        coverImagePath = "";
        otherImagesPaths.clear();
        noOfOtherImagesSelected = 0;
        noOfOtherImagesUploaded = 0 ;
        mSelectedImagesContainer.setVisibility(View.INVISIBLE);
        mSelectedMainImageContainer.setVisibility(View.INVISIBLE);
        title.setText("");
        quantity.setText("");
        price.setText("");
        description.setText("");
        brand.setText("");
        mrp.setText("");
        discount.setText("Discount: 0%");
    }
}