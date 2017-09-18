package com.dongdong.car.entity.nearOrder;

/**
 * Created by 沈 on 2017/6/22.
 */

public class NearOrderRequest {

    private String isSucess;
    private NearOrderResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public NearOrderResult getData() {
        return data;
    }

    public void setData(NearOrderResult data) {
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
        return "NearOrderRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
