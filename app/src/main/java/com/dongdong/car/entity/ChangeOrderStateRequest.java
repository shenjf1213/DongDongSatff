package com.dongdong.car.entity;

/**
 * Created by 沈 on 2017/8/15.
 */

public class ChangeOrderStateRequest {

    private String isSucess;
    private ChangeOrderStateResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public ChangeOrderStateResult getData() {
        return data;
    }

    public void setData(ChangeOrderStateResult data) {
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
        return "ChangeOrderStateRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class ChangeOrderStateResult {

        private String orderId;
        private ChangeOrderStateResultData order;
        private String orderSate;
        private Object remark;
        private String id;
        private String createdDate;
        private String updatedDate;
        private boolean deleted;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public ChangeOrderStateResultData getOrder() {
            return order;
        }

        public void setOrder(ChangeOrderStateResultData order) {
            this.order = order;
        }

        public String getOrderSate() {
            return orderSate;
        }

        public void setOrderSate(String orderSate) {
            this.orderSate = orderSate;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        @Override
        public String toString() {
            return "ChangeOrderStateResult{" +
                    "orderId='" + orderId + '\'' +
                    ", order=" + order +
                    ", orderSate='" + orderSate + '\'' +
                    ", remark=" + remark +
                    ", id='" + id + '\'' +
                    ", createdDate='" + createdDate + '\'' +
                    ", updatedDate='" + updatedDate + '\'' +
                    ", deleted=" + deleted +
                    '}';
        }

        public class ChangeOrderStateResultData {

        }

    }

}
