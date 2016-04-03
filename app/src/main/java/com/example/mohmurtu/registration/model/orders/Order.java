package com.example.mohmurtu.registration.model.orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mohmurtu.registration.model.Product;

public class Order implements Parcelable {

    private int orderStatus, orderedQuantity;
    private String orderCode, orderDate, orderSellerCode, statusMessage ;
    private String confirmationDate, confirmationComments, shipmentDate, shipmentComments, deliveryDate, deliveryComments, cancellationDate, cancellationComments;
    private String confirmDays, shippedVia, trackingId, deliverTo, rejectionReason ;
    private long orderId, orderSellerId, totalCost ;

    private Address address ;
    private Product product ;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public String getShippedVia() {
        return shippedVia;
    }

    public void setShippedVia(String shippedVia) {
        this.shippedVia = shippedVia;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getConfirmDays() {
        return confirmDays;
    }

    public void setConfirmDays(String confirmDays) {
        this.confirmDays = confirmDays;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getConfirmationComments() {
        return confirmationComments;
    }

    public void setConfirmationComments(String confirmationComments) {
        this.confirmationComments = confirmationComments;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentComments() {
        return shipmentComments;
    }

    public void setShipmentComments(String shipmentComments) {
        this.shipmentComments = shipmentComments;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryComments() {
        return deliveryComments;
    }

    public void setDeliveryComments(String deliveryComments) {
        this.deliveryComments = deliveryComments;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationComments() {
        return cancellationComments;
    }

    public void setCancellationComments(String cancellationComments) {
        this.cancellationComments = cancellationComments;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }



    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderSellerCode() {
        return orderSellerCode;
    }

    public void setOrderSellerCode(String orderSellerCode) {
        this.orderSellerCode = orderSellerCode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderSellerId() {
        return orderSellerId;
    }

    public void setOrderSellerId(long orderSellerId) {
        this.orderSellerId = orderSellerId;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order(){

    }

    private Order(Parcel in){

        this.orderStatus = in.readInt();
        this.orderedQuantity = in.readInt();
        this.orderCode = in.readString();
        this.orderDate = in.readString();
        this.orderSellerCode = in.readString();
        this.statusMessage = in.readString();
        this.orderId = in.readLong();
        this.orderSellerId = in.readLong();
        this.totalCost = in.readLong();
        this.confirmationDate = in.readString();
        this.confirmationComments = in.readString();
        this.shipmentDate = in.readString();
        this.shipmentComments = in.readString();
        this.deliveryDate = in.readString();
        this.deliveryComments = in.readString();
        this.cancellationDate = in.readString();
        this.cancellationComments = in.readString();
        this.confirmDays = in.readString();
        this.shippedVia = in.readString();
        this.trackingId = in.readString();
        this.deliverTo = in.readString();
        this.rejectionReason = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.orderedQuantity);
        dest.writeString(this.orderCode);
        dest.writeString(this.orderDate);
        dest.writeString(this.orderSellerCode);
        dest.writeString(this.statusMessage);
        dest.writeLong(this.orderId);
        dest.writeLong(this.orderSellerId);
        dest.writeLong(this.totalCost);
        dest.writeString(this.confirmationDate);
        dest.writeString(this.confirmationComments);
        dest.writeString(this.shipmentDate);
        dest.writeString(this.shipmentComments);
        dest.writeString(this.deliveryDate);
        dest.writeString(this.deliveryComments);
        dest.writeString(this.cancellationDate);
        dest.writeString(this.cancellationComments);
        dest.writeString(this.confirmDays);
        dest.writeString(this.shippedVia);
        dest.writeString(this.trackingId);
        dest.writeString(this.deliverTo);
        dest.writeString(this.rejectionReason);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {

        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
