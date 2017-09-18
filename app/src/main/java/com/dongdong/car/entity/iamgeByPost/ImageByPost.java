package com.dongdong.car.entity.iamgeByPost;

/**
 * Created by 沈 on 2017/6/26.
 */

public class ImageByPost {

    public ImageByPost() {

    }

    private String frontLeft;
    private String frontLeftHead;
    private String frontRightHead;
    private String frontRight;
    private String frontRightBehind;
    private String frontRightTail;
    private String frontLeftBehind;
    private String frontLeftPicture;

    public String getFrontLeft() {
        return frontLeft;
    }

    public void setFrontLeft(String frontLeft) {
        this.frontLeft = frontLeft;
    }

    public String getFrontLeftHead() {
        return frontLeftHead;
    }

    public void setFrontLeftHead(String frontLeftHead) {
        this.frontLeftHead = frontLeftHead;
    }

    public String getFrontRightHead() {
        return frontRightHead;
    }

    public void setFrontRightHead(String frontRightHead) {
        this.frontRightHead = frontRightHead;
    }

    public String getFrontRight() {
        return frontRight;
    }

    public void setFrontRight(String frontRight) {
        this.frontRight = frontRight;
    }

    public String getFrontRightBehind() {
        return frontRightBehind;
    }

    public void setFrontRightBehind(String frontRightBehind) {
        this.frontRightBehind = frontRightBehind;
    }

    public String getFrontRightTail() {
        return frontRightTail;
    }

    public void setFrontRightTail(String frontRightTail) {
        this.frontRightTail = frontRightTail;
    }

    public String getFrontLeftBehind() {
        return frontLeftBehind;
    }

    public void setFrontLeftBehind(String frontLeftBehind) {
        this.frontLeftBehind = frontLeftBehind;
    }

    public String getFrontLeftPicture() {
        return frontLeftPicture;
    }

    public void setFrontLeftPicture(String frontLeftPicture) {
        this.frontLeftPicture = frontLeftPicture;
    }

    @Override
    public String toString() {
        return "ImageByPost{" +
                "frontLeft='" + frontLeft + '\'' +
                ", frontLeftHead='" + frontLeftHead + '\'' +
                ", frontRightHead='" + frontRightHead + '\'' +
                ", frontRight='" + frontRight + '\'' +
                ", frontRightBehind='" + frontRightBehind + '\'' +
                ", frontRightTail='" + frontRightTail + '\'' +
                ", frontLeftBehind='" + frontLeftBehind + '\'' +
                ", frontLeftPicture='" + frontLeftPicture + '\'' +
                '}';
    }
}
