package com.dongdong.car.entity.userInfo;

/**
 * Created by 沈 on 2017/6/15.
 */

public class UserInfoRequest {

    private String isSucess;
    private UserInfoResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public UserInfoResult getData() {
        return data;
    }

    public void setData(UserInfoResult data) {
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
        return "UserInfoRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
