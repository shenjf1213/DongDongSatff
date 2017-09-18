package com.dongdong.car.entity.incomeMsg;

/**
 * Created by 沈 on 2017/6/30.
 */

public class IncomeRequest {

    private String isSucess;
    private IncomeResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public IncomeResult getData() {
        return data;
    }

    public void setData(IncomeResult data) {
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
        return "IncomeRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
