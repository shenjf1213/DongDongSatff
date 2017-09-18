package com.dongdong.car.entity.verifyTransactionPas;

/**
 * Created by 沈 on 2017/7/3.
 */

public class verifyTransactionPasResult {

    private String staffId;
    private String pwd;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "verifyTransactionPasResult{" +
                "staffId='" + staffId + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
