package com.dongdong.car.entity.autoOrders;

/**
 * Created by 沈 on 2017/7/3.
 */

public class AutoOrdersRequest {

    private String isSucess;
    private AutoOrdersResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public AutoOrdersResult getData() {
        return data;
    }

    public void setData(AutoOrdersResult data) {
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
        return "AutoOrdersRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class AutoOrdersResult {
        private String receviceValue;
        private String keyName;

        public String getReceviceValue() {
            return receviceValue;
        }

        public void setReceviceValue(String receviceValue) {
            this.receviceValue = receviceValue;
        }

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        @Override
        public String toString() {
            return "AutoOrdersResult{" +
                    "receviceValue='" + receviceValue + '\'' +
                    ", keyName='" + keyName + '\'' +
                    '}';
        }
    }
}
