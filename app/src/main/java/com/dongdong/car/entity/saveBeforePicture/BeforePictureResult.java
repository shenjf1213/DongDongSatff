package com.dongdong.car.entity.saveBeforePicture;

/**
 * Created by 沈 on 2017/6/27.
 */

public class BeforePictureResult {

    private String staffId;
    private String carUser;
    private String orderID;
    private String order;
    private String frontLeft;
    private String frontLeftHead;
    private String frontRightHead;
    private String frontRight;
    private String frontRightBehind;
    private String frontRightTail;
    private String frontLeftBehind;
    private String frontLeftPicture;
    private String remark;
    private String id;
    private String createdDate;
    private String updatedDate;
    private String deleted;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCarUser() {
        return carUser;
    }

    public void setCarUser(String carUser) {
        this.carUser = carUser;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFrontLeft() {
        return frontLeft;
    }

    public void setFrontLeft(String frontLeft) {
        this.frontLeft = frontLeft;
    }

    public String getFrontLeftHead() {
        return frontLeftHead;
    }

    public void setFrontLeftHead(String frontLeftHead) {
        this.frontLeftHead = frontLeftHead;
    }

    public String getFrontRightHead() {
        return frontRightHead;
    }

    public void setFrontRightHead(String frontRightHead) {
        this.frontRightHead = frontRightHead;
    }

    public String getFrontRight() {
        return frontRight;
    }

    public void setFrontRight(String frontRight) {
        this.frontRight = frontRight;
    }

    public String getFrontRightBehind() {
        return frontRightBehind;
    }

    public void setFrontRightBehind(String frontRightBehind) {
        this.frontRightBehind = frontRightBehind;
    }

    public String getFrontRightTail() {
        return frontRightTail;
    }

    public void setFrontRightTail(String frontRightTail) {
        this.frontRightTail = frontRightTail;
    }

    public String getFrontLeftBehind() {
        return frontLeftBehind;
    }

    public void setFrontLeftBehind(String frontLeftBehind) {
        this.frontLeftBehind = frontLeftBehind;
    }

    public String getFrontLeftPicture() {
        return frontLeftPicture;
    }

    public void setFrontLeftPicture(String frontLeftPicture) {
        this.frontLeftPicture = frontLeftPicture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "BeforePictureResult{" +
                "staffId='" + staffId + '\'' +
                ", carUser='" + carUser + '\'' +
                ", orderID='" + orderID + '\'' +
                ", order='" + order + '\'' +
                ", frontLeft='" + frontLeft + '\'' +
                ", frontLeftHead='" + frontLeftHead + '\'' +
                ", frontRightHead='" + frontRightHead + '\'' +
                ", frontRight='" + frontRight + '\'' +
                ", frontRightBehind='" + frontRightBehind + '\'' +
                ", frontRightTail='" + frontRightTail + '\'' +
                ", frontLeftBehind='" + frontLeftBehind + '\'' +
                ", frontLeftPicture='" + frontLeftPicture + '\'' +
                ", remark='" + remark + '\'' +
                ", id='" + id + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
