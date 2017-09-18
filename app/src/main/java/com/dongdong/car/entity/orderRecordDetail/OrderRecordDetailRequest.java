package com.dongdong.car.entity.orderRecordDetail;

import java.io.Serializable;

/**
 * Created by 沈 on 2017/6/21.
 */

public class OrderRecordDetailRequest implements Serializable {

    private String isSucess;
    private OrderRecordDetailResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public OrderRecordDetailResult getData() {
        return data;
    }

    public void setData(OrderRecordDetailResult data) {
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
        return "OrderRecordDetailRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
