package com.dongdong.car.entity.transactionRecord;

import java.util.List;

/**
 * Created by 沈 on 2017/6/30.
 */

public class TransactionRecordRequest {

    private String isSucess;
    private List<TransactionRecordResult> data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public List<TransactionRecordResult> getData() {
        return data;
    }

    public void setData(List<TransactionRecordResult> data) {
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
        return "TransactionRecordRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
