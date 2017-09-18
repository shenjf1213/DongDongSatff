package com.dongdong.car.entity.changePhone;

/**
 * Created by 沈 on 2017/6/14.
 */

public class ChangePhoneRequest {

    private String isSucess;
    private ChangePhoneResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public ChangePhoneResult getData() {
        return data;
    }

    public void setData(ChangePhoneResult data) {
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
        return "ChangePhoneRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
