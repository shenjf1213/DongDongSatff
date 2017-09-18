package com.dongdong.car.entity;

/**
 * Created by 沈 on 2017/8/8.
 */

public class GetOrderMsgRequest {

    private String isSucess;
    private GetOrderMsgResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public GetOrderMsgResult getData() {
        return data;
    }

    public void setData(GetOrderMsgResult data) {
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
        return "GetOrderMsgRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class GetOrderMsgResult {

        private String id;
        private String orderState;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        @Override
        public String toString() {
            return "GetOrderMsgResult{" +
                    "id='" + id + '\'' +
                    ", orderState='" + orderState + '\'' +
                    '}';
        }
    }
}
