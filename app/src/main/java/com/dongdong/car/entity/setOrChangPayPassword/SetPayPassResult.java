package com.dongdong.car.entity.setOrChangPayPassword;

/**
 * Created by 沈 on 2017/7/3.
 */

public class SetPayPassResult {

    private String staffId;
    private String payPassword;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    @Override
    public String toString() {
        return "SetPayPassResult{" +
                "staffId='" + staffId + '\'' +
                ", payPassword='" + payPassword + '\'' +
                '}';
    }
}
