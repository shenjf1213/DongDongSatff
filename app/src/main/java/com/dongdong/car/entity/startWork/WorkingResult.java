package com.dongdong.car.entity.startWork;

/**
 * Created by 沈 on 2017/6/16.
 */

public class WorkingResult {

    private String staffId;
    private String isWork;
    private String longitude;
    private String latitude;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getIsWork() {
        return isWork;
    }

    public void setIsWork(String isWork) {
        this.isWork = isWork;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "WorkingResult{" +
                "staffId='" + staffId + '\'' +
                ", isWork='" + isWork + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
