package com.dongdong.car.entity;

/**
 * Created by 沈 on 2017/7/28.
 */

public class PushRequest {

    private String MsgType;
    private PushResult Data;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public PushResult getData() {
        return Data;
    }

    public void setData(PushResult data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "PushRequest{" +
                "MsgType='" + MsgType + '\'' +
                ", Data=" + Data +
                '}';
    }

    public class PushResult {

        private String OrderId;
        private String OrderState;

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getOrderState() {
            return OrderState;
        }

        public void setOrderState(String orderState) {
            OrderState = orderState;
        }

        @Override
        public String toString() {
            return "PushResult{" +
                    "OrderId='" + OrderId + '\'' +
                    ", OrderState='" + OrderState + '\'' +
                    '}';
        }
    }
}
