package com.example.mohmurtu.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohmurtu.registration.listeners.DataListener;
import com.example.mohmurtu.registration.model.Product;
import com.example.mohmurtu.registration.model.orders.Address;
import com.example.mohmurtu.registration.model.orders.Order;
import com.example.mohmurtu.registration.util.Constants;
import com.example.mohmurtu.registration.util.HttpWorker;
import com.example.mohmurtu.registration.util.SharedPrefUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailFragment extends Fragment implements DataListener{

    Order order ;
    Address address ;
    Product product ;
    TextView productId, productTitle, quantity, price, totalAmount, orderCode, orderDate, viewAddress, copyAddress ;
    TextView confirmationLabel, confirmationDate, confirmationComments, shipmentDate, shipmentComments, deliveryDate, deliveryComments, cancellationDate, cancellationComments ;
    TextView confirmDays, shippedViaAndTracking;
    Button rejectOrderButton, confirmOrderButton, confirmShipmentButton, confirmDeliveryButton, shipmentBackButton, deliveryBackButton ;
    LinearLayout confirmLinear, shippedLinear, deliveredLinear ;
    CardView confirmationCard, shippmentCard, deliveryCard, cancellationCard;
    Context context ;
    CardView addressCard ;
    int addressCardViewCount = 0 ;
    String comments, orderDays, rejectionReason, shipComments, shipmentCourier, shipmentTrackId, shippedViaString, trackIdString, deliveryToString;
    int status = -1 ;
    int sellerId ;
    ProgressDialog pd ;
    View v;

    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public OrderDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            order = bundle.getParcelable("order");
            address = bundle.getParcelable("address");
            product = bundle.getParcelable("product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        comments = "" ;
        status = -1 ;
        ((ActivityHome)context).getSupportActionBar().setTitle("Order Action");

        pd = new ProgressDialog(context);
        sellerId = SharedPrefUtil.getIntegerPrefs(Constants.SELLER_ID);
        v = inflater.inflate(R.layout.fragment_order_detail, container, false);
        productId = (TextView) v.findViewById(R.id.product_id);
        productTitle = (TextView) v.findViewById(R.id.product_title);
        quantity =  (TextView) v.findViewById(R.id.product_ordered_quantity);
        price =  (TextView) v.findViewById(R.id.product_price);
        totalAmount =  (TextView) v.findViewById(R.id.total_order_amount);
        orderCode =  (TextView) v.findViewById(R.id.order_code);
        orderDate =  (TextView) v.findViewById(R.id.order_date);
        viewAddress =  (TextView) v.findViewById(R.id.address_view);

        rejectOrderButton = (Button) v.findViewById(R.id.reject_order_button);
        confirmOrderButton = (Button) v.findViewById(R.id.confirm_order_button);
        confirmShipmentButton = (Button) v.findViewById(R.id.confirm_order_shipment_button);
        confirmDeliveryButton = (Button) v.findViewById(R.id.confirm_order_delivery_button);
        deliveryBackButton = (Button) v.findViewById(R.id.confirm_deliver_back);
        shipmentBackButton = (Button) v.findViewById(R.id.confirm_shipment_back);

        addressCard = (CardView) v.findViewById(R.id.address_view_card);
        copyAddress = (TextView) v.findViewById(R.id.view_address_copy);

        confirmLinear = (LinearLayout) v.findViewById(R.id.confirm_order_linear);
        shippedLinear = (LinearLayout) v.findViewById(R.id.confirm_shipment_linear);
        deliveredLinear = (LinearLayout) v.findViewById(R.id.confirm_delivery_linear);

        confirmationCard = (CardView) v.findViewById(R.id.confirm_reject_card);
        shippmentCard = (CardView) v.findViewById(R.id.shipment_status_card);
        deliveryCard = (CardView) v.findViewById(R.id.delivery_status_card);
        cancellationCard = (CardView) v.findViewById(R.id.cancellation_status_card);

        viewButtonsLayout();

        rejectOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectOrder();
            }
        });

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
            }
        });
        viewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressCardViewCount == 0){
                    addressCard.setVisibility(View.VISIBLE);
                    copyAddress.setVisibility(View.VISIBLE);
                    viewAddress.setText("Hide Address");
                    addressCardViewCount = 1 ;
                }
                else if(addressCardViewCount == 1){
                    addressCard.setVisibility(View.GONE);
                    copyAddress.setVisibility(View.GONE);
                    addressCardViewCount = 0;
                    viewAddress.setText("View Address");
                }
            }
        });

        copyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipBoardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", address.getAddress());
                myClipBoardManager.setPrimaryClip(myClip);
                Toast.makeText(context, "Address copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        rejectOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectOrder();
            }
        });

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
            }
        });

        confirmShipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmShipment();
            }
        });

        shipmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confirmDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelivery();
            }
        });

        assignValues();
        return v ;
    }

    public void assignValues(){
        orderCode.setText(order.getOrderCode());
        orderDate.setText(order.getOrderDate());
        productId.setText("Product Id: " + product.getProdId()+"");
        productTitle.setText(product.getProdName());
        quantity.setText("Quantity: " + order.getOrderedQuantity() + "");
        totalAmount.setText("Total Amount: " + order.getTotalCost() + "");
        price.setText("Price: " + product.getPrice() + "");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void viewConfirmCard(){
        confirmationCard.setVisibility(View.VISIBLE);
        confirmationDate = (TextView) v.findViewById(R.id.confirmed_rejected_date);
        confirmationComments = (TextView) v.findViewById(R.id.confirm_rejected_comments);
        confirmDays = (TextView) v.findViewById(R.id.confirm_deliver_days);
        confirmationDate.setText(order.getConfirmationDate());
        confirmationComments.setText(order.getConfirmationComments());
        confirmDays.setText("Delivery Days: " + order.getConfirmDays());
    }

    public void viewRejectCard(){
        confirmationCard.setVisibility(View.VISIBLE);
        confirmationDate = (TextView) v.findViewById(R.id.confirmed_rejected_date);
        confirmationComments = (TextView) v.findViewById(R.id.confirm_rejected_comments);
        confirmationLabel = (TextView) v.findViewById(R.id.confirm_reject_label);
        confirmDays = (TextView) v.findViewById(R.id.confirm_deliver_days);
        confirmationLabel.setText("Rejected On");
        confirmDays.setText("Rejection Reason: " + order.getRejectionReason());
        confirmationDate.setText(order.getConfirmationDate());
        confirmationComments.setText(order.getConfirmationComments());
    }

    public void viewShipmentCard(){
        shippmentCard.setVisibility(View.VISIBLE);
        shipmentDate = (TextView) v.findViewById(R.id.shipment_date_value);
        shipmentComments = (TextView) v.findViewById(R.id.shipment_comments);
        shippedViaAndTracking = (TextView) v.findViewById(R.id.shipped_via_tracking);
        shipmentDate.setText(order.getShipmentDate());
        shipmentComments.setText(order.getShipmentComments());
        shippedViaAndTracking.setText(order.getShippedVia()+", " + order.getTrackingId());
    }

    public void viewDeliverCard(){
        deliveryCard.setVisibility(View.VISIBLE);
        deliveryDate = (TextView) v.findViewById(R.id.delivery_date_value);
//        deliveryComments = (TextView) v.findViewById(R.id.delivery_comments);
        confirmationDate.setText(order.getConfirmationDate());
        confirmationComments.setText(order.getConfirmationComments());
    }

    public void viewCancelCard(){

    }

    public void viewButtonsLayout(){
        int status = order.getOrderStatus();
        if(status == 0){
            confirmLinear.setVisibility(View.VISIBLE);
            shippedLinear.setVisibility(View.GONE);
            deliveredLinear.setVisibility(View.GONE);
            shippmentCard.setVisibility(View.GONE);
            deliveryCard.setVisibility(View.GONE);
            cancellationCard.setVisibility(View.GONE);
            confirmationCard.setVisibility(View.GONE);
        }
        else if(status == 1){
            confirmLinear.setVisibility(View.GONE);
            shippedLinear.setVisibility(View.VISIBLE);
            deliveredLinear.setVisibility(View.GONE);
            shippmentCard.setVisibility(View.GONE);
            deliveryCard.setVisibility(View.GONE);
            cancellationCard.setVisibility(View.GONE);
            viewConfirmCard();
        }
        else if(status == 2){
            confirmLinear.setVisibility(View.GONE);
            shippedLinear.setVisibility(View.GONE);
            deliveredLinear.setVisibility(View.GONE);
            shippmentCard.setVisibility(View.GONE);
            deliveryCard.setVisibility(View.GONE);
            cancellationCard.setVisibility(View.GONE);
            viewRejectCard();
        }
        else if(status == 3){
            confirmLinear.setVisibility(View.GONE);
            shippedLinear.setVisibility(View.GONE);
            deliveredLinear.setVisibility(View.VISIBLE);
            deliveryCard.setVisibility(View.GONE);
            cancellationCard.setVisibility(View.GONE);
            viewConfirmCard();
            viewShipmentCard();
        }
        else if(status == 4){
            confirmLinear.setVisibility(View.GONE);
            shippedLinear.setVisibility(View.GONE);
            deliveredLinear.setVisibility(View.GONE);
            cancellationCard.setVisibility(View.GONE);
            viewConfirmCard();
            viewShipmentCard();
            viewDeliverCard();
        }
        else if(status == 5){

        }
    }

    public void rejectOrder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        final View view = inflator.inflate(R.layout.reject_order_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final RadioGroup rejectAction = (RadioGroup) view.findViewById(R.id.order_rejection_radio_group);
        rejectAction.check(R.id.order_rejection_not_available);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = rejectAction.getCheckedRadioButtonId();
                RadioButton selectedRadio = (RadioButton) view.findViewById(selectedId);
                rejectionReason = selectedRadio.getText().toString();
                orderDays="";
                EditText additionalComments = (EditText) view.findViewById(R.id.order_reject_comments);
                comments = additionalComments.getText().toString();
                status = 2 ;
                connectToServer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void confirmOrder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        final View view = inflator.inflate(R.layout.confirm_order_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final RadioGroup confirmAction = (RadioGroup) view.findViewById(R.id.order_confirm_radio_group);
        confirmAction.check(R.id.order_confirm_0_2);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = confirmAction.getCheckedRadioButtonId();
                RadioButton selectedRadio = (RadioButton) view.findViewById(selectedId);
                orderDays = selectedRadio.getText().toString();
                rejectionReason = "";
                EditText additionalComments = (EditText) view.findViewById(R.id.order_reject_comments);
                comments = additionalComments.getText().toString();
                status = 1;
                connectToServer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void confirmShipment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        final View view = inflator.inflate(R.layout.confirm_shipment_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final EditText shippedVia = (EditText) view.findViewById(R.id.shipped_via);
        final EditText trackingId = (EditText) view.findViewById(R.id.shipped_tracking_id);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shippedViaString = shippedVia.getText().toString();
                trackIdString = trackingId.getText().toString();
                comments = "Shipped Via: " + shippedViaString + " with Tracking Id: " + trackIdString;
                status = 3;
                connectToServer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        shippedVia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    public void confirmDelivery(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        final View view = inflator.inflate(R.layout.confirm_delivery_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final EditText deliverTo = (EditText) view.findViewById(R.id.delivery_to);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deliveryToString = deliverTo.getText().toString();
                comments = "Deliver to " + deliveryToString;
                status = 4;
                connectToServer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void connectToServer(){
        pd.setTitle("Changing Status");
        pd.setCancelable(false);
        HttpWorker http = new HttpWorker(this, pd, context);
        http.execute(constructParameters());
    }

    public List constructParameters(){
        List params = new ArrayList();
        if(order.getOrderStatus() == 0) {
            params.add(new String[]{"prodId", "status", "orderSellerCode", "sellerId", "comments", "orderSellerId", "orderConfirmDays", "orderRejectionReason"});
            params.add(new Object[]{product.getProdId()+"", status+"", order.getOrderSellerCode()+"", sellerId+"", comments, order.getOrderSellerId()+"", orderDays, rejectionReason});
            params.add(Constants.CONFIRM_ORDER);
            params.add("POST");
        }
        else if(order.getOrderStatus() == 1){
            params.add(new String[]{"prodId", "status", "orderSellerCode", "sellerId", "comments", "orderSellerId", "shippedVia", "trackId"});
            params.add(new Object[]{product.getProdId()+"", status+"", order.getOrderSellerCode()+"", sellerId+"", comments, order.getOrderSellerId()+"", shippedViaString, trackIdString});
            params.add(Constants.CONFIRM_SHIP_ORDER);
            params.add("POST");
        }
        else if(order.getOrderStatus() == 3){
            params.add(new String[]{"prodId", "status", "orderSellerCode", "sellerId", "comments", "orderSellerId", "deliveryTo"});
            params.add(new Object[]{product.getProdId()+"", status+"", order.getOrderSellerCode()+"", sellerId+"", comments, order.getOrderSellerId()+"", deliveryToString});
            params.add(Constants.CONFIRM_SHIP_ORDER);
            params.add("POST");
        }
        return params;
    }

    @Override
    public void dataDownloaded(String response) {
        try{
            if(order.getOrderStatus() == 0){
                JSONObject json = new JSONObject(response);
                if(json.getBoolean("issuccess")){
                    order.setOrderStatus(status);
                    order.setConfirmationComments(json.getString("confirmationComments"));
                    order.setConfirmationDate(json.getString("confirmationDate"));
                    order.setConfirmDays(json.getString("confirmationDays"));
                    order.setRejectionReason(json.getString("orderRejectionReason"));
                    viewButtonsLayout();
                    pd.cancel();
//                    FragmentManager manager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    Fragment newFragment = this;
//                    this.onDestroy();
//                    ft.remove(this);
//                    ft.replace(R.id.login_fragment,newFragment);
//                    //container is the ViewGroup of current fragment
//                    ft.addToBackStack(null);
//                    ft.commit();
                }
            }
            else if(order.getOrderStatus() == 1){
                JSONObject json = new JSONObject(response);
                if(json.getBoolean("issuccess")){
                    order.setOrderStatus(status);
                    order.setShipmentComments(comments);
                    order.setShipmentDate(json.getString("shipmentDate"));
                    order.setShippedVia(json.getString("shippedVia"));
                    order.setTrackingId(json.getString("trackingId"));
                    viewButtonsLayout();
                    pd.cancel();
                }
            }
            else if(order.getOrderStatus() == 3){
                JSONObject json = new JSONObject(response);
                if(json.getBoolean("issuccess")){
                    order.setOrderStatus(status);
                    order.setDeliveryComments(comments);
                    order.setDeliveryComments(json.getString("deliveryDate"));
                    order.setDeliverTo(json.getString("deliverTo"));
                    viewButtonsLayout();
                    pd.cancel();
                }
            }
        }
        catch(Exception e){

        }
    }
}