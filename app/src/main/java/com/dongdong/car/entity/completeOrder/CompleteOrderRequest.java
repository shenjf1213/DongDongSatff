package com.dongdong.car.entity.completeOrder;

import java.util.List;

/**
 * Created by 沈 on 2017/7/2.
 */

public class CompleteOrderRequest {

    private String isSucess;
    private List<CompleteOrderResult> data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public List<CompleteOrderResult> getData() {
        return data;
    }

    public void setData(List<CompleteOrderResult> data) {
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
        return "CompleteOrderRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
