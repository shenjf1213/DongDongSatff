package com.dongdong.car.entity.withdrawMsg;

/**
 * Created by 沈 on 2017/6/30.
 */

public class WithdrawMsgRequest {

    private String isSucess;
    private WithdrawMsgResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public WithdrawMsgResult getData() {
        return data;
    }

    public void setData(WithdrawMsgResult data) {
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
        return "WithdrawMsgRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
