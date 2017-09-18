package com.dongdong.car.entity;

import java.util.List;

/**
 * Created by 沈 on 2017/9/1.
 */

public class GetPageRequest {

    private String isSucess;
    private List<GetPageResult> data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public List<GetPageResult> getData() {
        return data;
    }

    public void setData(List<GetPageResult> data) {
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
        return "GetPageRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class GetPageResult {

        private String orderId;
        private String pageIndex;
        private String orderState;
        private String remark;
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

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
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
            return "GetPageResult{" +
                    "orderId='" + orderId + '\'' +
                    ", pageIndex='" + pageIndex + '\'' +
                    ", orderState='" + orderState + '\'' +
                    ", remark='" + remark + '\'' +
                    ", id='" + id + '\'' +
                    ", createdDate='" + createdDate + '\'' +
                    ", updatedDate='" + updatedDate + '\'' +
                    ", deleted=" + deleted +
                    '}';
        }
    }
}
