package com.dongdong.car.entity.startWork;

/**
 * Created by 沈 on 2017/6/16.
 */

public class WorkingRequest {

    private String isSucess;
    private WorkingResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public WorkingResult getData() {
        return data;
    }

    public void setData(WorkingResult data) {
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
        return "WorkingRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
