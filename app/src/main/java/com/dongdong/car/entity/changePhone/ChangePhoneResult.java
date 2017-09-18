package com.dongdong.car.entity.changePhone;

/**
 * Created by 沈 on 2017/6/14.
 */

public class ChangePhoneResult {

    private String id;
    private String userName;
    private String displayUserName;
    private String mobileTelePhone;
    private String password;
    private String staffType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayUserName() {
        return displayUserName;
    }

    public void setDisplayUserName(String displayUserName) {
        this.displayUserName = displayUserName;
    }

    public String getMobileTelePhone() {
        return mobileTelePhone;
    }

    public void setMobileTelePhone(String mobileTelePhone) {
        this.mobileTelePhone = mobileTelePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    @Override
    public String toString() {
        return "ChangePhoneResult{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", displayUserName='" + displayUserName + '\'' +
                ", mobileTelePhone='" + mobileTelePhone + '\'' +
                ", password='" + password + '\'' +
                ", staffType='" + staffType + '\'' +
                '}';
    }
}
