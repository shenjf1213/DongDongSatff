package com.dongdong.car.entity.withdraw;

/**
 * Created by 沈 on 2017/6/29.
 */

public class WithdrawResult {

    private String staffId;
    private String cashTotal;
    private String payMoney;
    private String changeMoney;
    private String cashDateTime;
    private String tranceDateTime;
    private String transNo;
    private String isApprovel;
    private String approvelState;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
        this.cashTotal = cashTotal;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(String changeMoney) {
        this.changeMoney = changeMoney;
    }

    public String getCashDateTime() {
        return cashDateTime;
    }

    public void setCashDateTime(String cashDateTime) {
        this.cashDateTime = cashDateTime;
    }

    public String getTranceDateTime() {
        return tranceDateTime;
    }

    public void setTranceDateTime(String tranceDateTime) {
        this.tranceDateTime = tranceDateTime;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getIsApprovel() {
        return isApprovel;
    }

    public void setIsApprovel(String isApprovel) {
        this.isApprovel = isApprovel;
    }

    public String getApprovelState() {
        return approvelState;
    }

    public void setApprovelState(String approvelState) {
        this.approvelState = approvelState;
    }

    @Override
    public String toString() {
        return "WithdrawResult{" +
                "staffId='" + staffId + '\'' +
                ", cashTotal='" + cashTotal + '\'' +
                ", payMoney='" + payMoney + '\'' +
                ", changeMoney='" + changeMoney + '\'' +
                ", cashDateTime='" + cashDateTime + '\'' +
                ", tranceDateTime='" + tranceDateTime + '\'' +
                ", transNo='" + transNo + '\'' +
                ", isApprovel='" + isApprovel + '\'' +
                ", approvelState='" + approvelState + '\'' +
                '}';
    }
}
