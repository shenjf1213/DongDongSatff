package com.dongdong.car.entity.verifyTransactionPas;

/**
 * Created by 沈 on 2017/7/3.
 */

public class verifyTransactionPasRequest {

    private String isSucess;
    private verifyTransactionPasResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public verifyTransactionPasResult getData() {
        return data;
    }

    public void setData(verifyTransactionPasResult data) {
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
        return "verifyTransactionPasRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
