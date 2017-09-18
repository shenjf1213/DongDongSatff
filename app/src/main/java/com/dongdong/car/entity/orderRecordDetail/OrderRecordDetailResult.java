package com.dongdong.car.entity.orderRecordDetail;

import java.io.Serializable;

/**
 * Created by 沈 on 2017/6/21.
 */

public class OrderRecordDetailResult implements Serializable {

    private DetailByOrderInfo orderInfo;
    private DetailByUserInfo userInfo;
    private String longitude;
    private String latitude;
    private String address;
    private String maxLat;
    private String id;
    private String remark;

    public DetailByOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(DetailByOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public DetailByUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(DetailByUserInfo userInfo) {
        this.userInfo = userInfo;
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

    public String getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(String maxLat) {
        this.maxLat = maxLat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderRecordDetailResult{" +
                "orderInfo=" + orderInfo +
                ", userInfo=" + userInfo +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", maxLat='" + maxLat + '\'' +
                ", id='" + id + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
