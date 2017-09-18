package com.dongdong.car.entity.uploadImage;

/**
 * Created by 沈 on 2017/4/27.
 */

public class UploadImageRequest {

    private String error;
    private String errmsg;
    private String fileName;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "uploadImageRequest{" +
                "error='" + error + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
