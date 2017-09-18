package com.dongdong.car.entity.completeOrder;

/**
 * Created by 沈 on 2017/7/2.
 */

public class CompleteOrderResult {

    private String staffId;
    private String orderId;
    private String payTal;
    private String payDateTime;
    private String codeType;
    private String transNo;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayTal() {
        return payTal;
    }

    public void setPayTal(String payTal) {
        this.payTal = payTal;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    @Override
    public String toString() {
        return "CompleteOrderResult{" +
                "staffId='" + staffId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", payTal='" + payTal + '\'' +
                ", payDateTime='" + payDateTime + '\'' +
                ", codeType='" + codeType + '\'' +
                ", transNo='" + transNo + '\'' +
                '}';
    }
}
