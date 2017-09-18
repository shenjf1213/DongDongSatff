package com.dongdong.car.entity.nearOrder;

/**
 * Created by 沈 on 2017/6/22.
 */

public class NearOrderList {

    private String orderId;
    private String orderType;
    private String maxLax;
    private String address;
    private String carNumber;
    private String carModel;
    private String carColor;
    private String orderTime;
    private String latitude;
    private String longitude;
    private String orderNumber;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getMaxLax() {
        return maxLax;
    }

    public void setMaxLax(String maxLax) {
        this.maxLax = maxLax;
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

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "NearOrderList{" +
                "orderId='" + orderId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", maxLax='" + maxLax + '\'' +
                ", address='" + address + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carColor='" + carColor + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
