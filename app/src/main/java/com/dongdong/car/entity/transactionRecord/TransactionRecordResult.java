package com.dongdong.car.entity.transactionRecord;

/**
 * Created by 沈 on 2017/6/30.
 */

public class TransactionRecordResult {

    private String id;
    private double payTal;
    private String payDateTime;
    private String transNo;
    private String payType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPayTal() {
        return payTal;
    }

    public void setPayTal(double payTal) {
        this.payTal = payTal;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "TransactionRecordResult{" +
                "id='" + id + '\'' +
                ", payTal=" + payTal +
                ", payDateTime='" + payDateTime + '\'' +
                ", transNo='" + transNo + '\'' +
                ", payType='" + payType + '\'' +
                '}';
    }
}
