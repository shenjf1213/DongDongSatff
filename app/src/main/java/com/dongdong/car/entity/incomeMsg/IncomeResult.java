package com.dongdong.car.entity.incomeMsg;

/**
 * Created by 沈 on 2017/6/30.
 */

public class IncomeResult {

    private String payTal;
    private String payDateTime;
    private String transNo;
    private String transState;

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

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }

    @Override
    public String toString() {
        return "IncomeResult{" +
                "payTal='" + payTal + '\'' +
                ", payDateTime='" + payDateTime + '\'' +
                ", transNo='" + transNo + '\'' +
                ", transState='" + transState + '\'' +
                '}';
    }
}
