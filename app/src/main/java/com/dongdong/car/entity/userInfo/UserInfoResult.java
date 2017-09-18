package com.dongdong.car.entity.userInfo;

/**
 * Created by 沈 on 2017/6/15.
 */

public class UserInfoResult {

    private String satffID;
    private String userName;
    private String password;
    private String staffType;
    private String displayUserName;
    private String mobilePhone;
    private String sex;
    private String cardNo;
    private String contactUser;
    private String contactMobile;
    private String headImage;
    private String staffLevel;
    private String staffWork;
    private String brankCard;
    private double payTotal;
    private String fAreaCode;
    private String bankName;
    private String payPassword;
    private String orderDayNumber;
    private String orderWorkTime;
    private String feedBackRate;
    private String backRate;

    public String getSatffID() {
        return satffID;
    }

    public void setSatffID(String satffID) {
        this.satffID = satffID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDisplayUserName() {
        return displayUserName;
    }

    public void setDisplayUserName(String displayUserName) {
        this.displayUserName = displayUserName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getStaffLevel() {
        return staffLevel;
    }

    public void setStaffLevel(String staffLevel) {
        this.staffLevel = staffLevel;
    }

    public String getStaffWork() {
        return staffWork;
    }

    public void setStaffWork(String staffWork) {
        this.staffWork = staffWork;
    }

    public String getBrankCard() {
        return brankCard;
    }

    public void setBrankCard(String brankCard) {
        this.brankCard = brankCard;
    }

    public double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(double payTotal) {
        this.payTotal = payTotal;
    }

    public String getfAreaCode() {
        return fAreaCode;
    }

    public void setfAreaCode(String fAreaCode) {
        this.fAreaCode = fAreaCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getOrderDayNumber() {
        return orderDayNumber;
    }

    public void setOrderDayNumber(String orderDayNumber) {
        this.orderDayNumber = orderDayNumber;
    }

    public String getOrderWorkTime() {
        return orderWorkTime;
    }

    public void setOrderWorkTime(String orderWorkTime) {
        this.orderWorkTime = orderWorkTime;
    }

    public String getFeedBackRate() {
        return feedBackRate;
    }

    public void setFeedBackRate(String feedBackRate) {
        this.feedBackRate = feedBackRate;
    }

    public String getBackRate() {
        return backRate;
    }

    public void setBackRate(String backRate) {
        this.backRate = backRate;
    }

    @Override
    public String toString() {
        return "UserInfoResult{" +
                "satffID='" + satffID + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", staffType='" + staffType + '\'' +
                ", displayUserName='" + displayUserName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", sex='" + sex + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", contactUser='" + contactUser + '\'' +
                ", contactMobile='" + contactMobile + '\'' +
                ", headImage='" + headImage + '\'' +
                ", staffLevel='" + staffLevel + '\'' +
                ", staffWork='" + staffWork + '\'' +
                ", brankCard='" + brankCard + '\'' +
                ", payTotal=" + payTotal +
                ", fAreaCode='" + fAreaCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", payPassword='" + payPassword + '\'' +
                ", orderDayNumber='" + orderDayNumber + '\'' +
                ", orderWorkTime='" + orderWorkTime + '\'' +
                ", feedBackRate='" + feedBackRate + '\'' +
                ", backRate='" + backRate + '\'' +
                '}';
    }
}
