package com.dongdong.car.entity.setOrChangPayPassword;

/**
 * Created by 沈 on 2017/7/3.
 */

public class SetPayPassRequest {

    private String isSucess;
    private SetPayPassResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public SetPayPassResult getData() {
        return data;
    }

    public void setData(SetPayPassResult data) {
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
        return "SetPayPassRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
