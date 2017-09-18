package com.dongdong.car.entity.orderRecordDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 沈 on 2017/6/21.
 */

public class DetailByOrderInfo implements Serializable {

    private String id;
    private String orderNumber;
    private String longitude;
    private String latitude;
    private String address;
    private String carNumber;
    private String orderType;
    private String doorTime;
    private String orderTime;
    private String appointmenttime;
    private String orderTotal;
    private String orderState;
    private List<OrderItemList> orderItemList;
    private List<OrderPictureList> orderPictureList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDoorTime() {
        return doorTime;
    }

    public void setDoorTime(String doorTime) {
        this.doorTime = doorTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getAppointmenttime() {
        return appointmenttime;
    }

    public void setAppointmenttime(String appointmenttime) {
        this.appointmenttime = appointmenttime;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public List<OrderItemList> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemList> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public List<OrderPictureList> getOrderPictureList() {
        return orderPictureList;
    }

    public void setOrderPictureList(List<OrderPictureList> orderPictureList) {
        this.orderPictureList = orderPictureList;
    }

    @Override
    public String toString() {
        return "DetailByOrderInfo{" +
                "id='" + id + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", orderType='" + orderType + '\'' +
                ", doorTime='" + doorTime + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", appointmenttime='" + appointmenttime + '\'' +
                ", orderTotal='" + orderTotal + '\'' +
                ", orderState='" + orderState + '\'' +
                ", orderItemList=" + orderItemList +
                ", orderPictureList=" + orderPictureList +
                '}';
    }
}
