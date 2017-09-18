package com.dongdong.car.entity;

/**
 * Created by 沈 on 2017/8/8.
 */

public class CancelOrderRequest {

    private String isSucess;
    private CancelOrderResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public CancelOrderResult getData() {
        return data;
    }

    public void setData(CancelOrderResult data) {
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
        return "CancelOrderRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class CancelOrderResult {

        private String cancelId;
        private String orderid;

        public String getCancelId() {
            return cancelId;
        }

        public void setCancelId(String cancelId) {
            this.cancelId = cancelId;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        @Override
        public String toString() {
            return "CancelOrderResult{" +
                    "cancelId='" + cancelId + '\'' +
                    ", orderid='" + orderid + '\'' +
                    '}';
        }
    }
}
