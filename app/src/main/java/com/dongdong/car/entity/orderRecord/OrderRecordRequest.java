package com.dongdong.car.entity.orderRecord;

/**
 * Created by 沈 on 2017/6/20.
 */

public class OrderRecordRequest {

    private String isSucess;
    private OrderRecordResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public OrderRecordResult getData() {
        return data;
    }

    public void setData(OrderRecordResult data) {
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
        return "OrderRecordRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
