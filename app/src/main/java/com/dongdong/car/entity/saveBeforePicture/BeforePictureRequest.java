package com.dongdong.car.entity.saveBeforePicture;

/**
 * Created by 沈 on 2017/6/27.
 */

public class BeforePictureRequest {

    private String isSucess;
    private BeforePictureResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public BeforePictureResult getData() {
        return data;
    }

    public void setData(BeforePictureResult data) {
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
        return "BeforePictureRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
