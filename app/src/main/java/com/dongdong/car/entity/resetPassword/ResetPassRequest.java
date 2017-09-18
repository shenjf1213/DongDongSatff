package com.dongdong.car.entity.resetPassword;

/**
 * Created by 沈 on 2017/6/14.
 */

public class ResetPassRequest {

    private String isSucess;
    private ResetPassResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public ResetPassResult getData() {
        return data;
    }

    public void setData(ResetPassResult data) {
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
        return "ResetPassRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
