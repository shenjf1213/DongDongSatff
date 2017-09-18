package com.dongdong.car.entity.orderRecordDetail;

import java.io.Serializable;

/**
 * Created by 沈 on 2017/6/21.
 */

public class DetailByUserInfo implements Serializable {

    private String id;
    private String enabled;
    private String mobilePhone;
    private String userName;
    private String displayName;
    private String sex;
    private String carNumber;
    private String carModel;
    private String carColor;
    private String headPicture;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DetailByUserInfo{" +
                "id='" + id + '\'' +
                ", enabled='" + enabled + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", sex='" + sex + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carColor='" + carColor + '\'' +
                ", headPicture='" + headPicture + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
