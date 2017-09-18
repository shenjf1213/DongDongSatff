package com.dongdong.car.entity.withdraw;

/**
 * Created by 沈 on 2017/6/29.
 */

public class WithdrawRequest {

    private String isSucess;
    private WithdrawResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public WithdrawResult getData() {
        return data;
    }

    public void setData(WithdrawResult data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "WithdrawRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
