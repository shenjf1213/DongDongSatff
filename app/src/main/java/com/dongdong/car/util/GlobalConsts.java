package com.dongdong.car.util;

/**
 * Created by 沈 on 2017/3/31.
 */

public class GlobalConsts {

    public static final String DONG_DONG_IMAGE_URL = "http://api.hongkeg.com"; // 服务器的照片地址地址
    public static final String CALL_SERVICE_PHONE_1 = "010-56183199";
    public static final String CALL_SERVICE_PHONE_2 = "400-1234-8888";
    public static final String CALL_SERVICE_PHONE_3 = "400-1234-8888";

    /**
     * 接口数据
     */
    public static final String DONG_DONG_URL = "http://api.hongkeg.com/"; // 服务器地址
    public static final String DONG_DONG_INTRANET_URL = "http://192.168.2.147:8023/"; // 服务器内网地址
    public static final String UPLOAD_IMAGE_URL = DONG_DONG_URL + "api/Account/UploadFile"; // 上传头像
    public static final String REGISTER_CODE_URL = DONG_DONG_URL + "SMS/GetSms?telephone="; // 获取短信验证码
    public static final String FORGET_PASSWORD_URL = DONG_DONG_URL + "api/Account/ForgetPassword"; // 忘记密码
    public static final String LOGIN_URL = DONG_DONG_URL + "api/Staff/Login"; // 登录接口
    public static final String GET_USER_INFO_MESSAGE_URL = DONG_DONG_URL + "api/Staff/GetStaffInfo?Id="; // 获取当前登录员工的个人信息
    public static final String CHANGE_PHONE_URL = DONG_DONG_URL + "Staff/ChangePhone"; // 修改手机号码
    public static final String RESET_PASSWORD_URL = DONG_DONG_URL + "Staff/ChangePassword"; // 修改密码
    public static final String IS_START_WORKING_URL = DONG_DONG_URL + "api/Staff/IsStarWork"; // 开始或停止工作
    public static final String GET_NEAR_ORDER_LIST_URL = DONG_DONG_URL + "api/Staff/NearOrderList"; // 获取附近订单的接口
    public static final String GET_ORDER_DETAIL_URL = DONG_DONG_URL + "api/Staff/GetStaffOrderInfo?orderID="; // 根据订单编号查询订单的详细信息
    public static final String BEFORE_CLEANING_UPLOAD_PICTURE_URL = DONG_DONG_URL + "api/Staff/UploadStaffFreatHeadImg"; // 洗前照片上传
    public static final String SAVE_BEFORE_CLEANING_PICTURE_URL = DONG_DONG_URL + "api/Staff/SaveStaffFreatHeadImag"; // 保存清洗前的照片
    public static final String AFTER_CLEANING_UPLOAD_PICTURE_URL = DONG_DONG_URL + "api/Staff/UploadStaffBihindHeadImg"; // 洗后照片上传
    public static final String SAVE_AFTER_CLEANING_PICTURE_URL = DONG_DONG_URL + "api/Staff/SaveStaffBihindHeadImg"; // 洗后照片保存
    public static final String GRAB_ORDERS_URL = DONG_DONG_URL + "api/Staff/CreateSingle"; // 抢单模式
    public static final String CANCEL_ORDER_URL = DONG_DONG_URL + "api/Staff/CancelStaffOrder"; // 用户取消订单
    public static final String GET_TRANSACTION_RECORD_URL = DONG_DONG_URL + "api/Staff/StaffTrans?staffId="; // 获取服务人员的交易记录
    public static final String GET_TRANSACTION_RECORD_WITHDRAW_URL = DONG_DONG_URL + "api/Staff/GetCashInfo?TransNo="; // 查询提现的具体信息
    public static final String GET_TRANSACTION_RECORD_INCOME_URL = DONG_DONG_URL + "api/Staff/GetPayInfo?TransNo="; // 查询收入的具体信息
    public static final String GENERATE_NEAR_ORDER_URL = DONG_DONG_URL + "api/Staff/CreateNearOrder"; // 生成附近的订单
    public static final String BIND_DEVICE_TOKEN_URL = DONG_DONG_URL + "api/Umeng/BindDevicetoken"; // 绑定友盟推送token
    public static final String GET_ORDER_MSG_BY_ID_URL = DONG_DONG_URL + "api/Staff/GetStaffOrderAll?staffId="; // 根据用户id获取当前是否接单已经接单信息
    public static final String CREATE_ORDER_STATE_URL = DONG_DONG_URL + "api/Order/CreateOrderStateRecord"; // 修改订单的进行状态
    public static final String SAVE_PAGE_UNEXPECTED_EXIT_URL = DONG_DONG_URL + "api/Staff/SavePageException"; // 订单正在进行中时强退后保存当前页面状态
    public static final String GET_PAGE_UNEXPECTED_EXIT_URL = DONG_DONG_URL + "api/Staff/GetPageException?orderId="; // 获取订单未完成时的业务页面

    /**
     * handler常量值
     */
    public static final int TAKE_PICTURES_UPLOAD = 1899;
    public static final int ALBUM_UPLOAD = 1900;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1901;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1902;
    public static final int UPLOAD_IMAGE_HANDLER = 1;
    public static final int FORGET_PASSWORD_HANDLER = 2;
    public static final int LOGIN_HANDLER = 3;
    public static final int GET_USER_INFO_MESSAGE_HANDLER = 4;
    public static final int CHANGE_PHONE_HANDLER = 5;
    public static final int RESET_PASSWORD_HANDLER = 6;
    public static final int IS_START_WORKING_HANGLER = 7;
    public static final int GET_NEAR_ORDER_LIST_HANDLER = 8;
    public static final int GET_ORDER_RECORD_LIST_1_HANDLER = 9;
    public static final int GET_ORDER_RECORD_LIST_2_HANDLER = 10;
    public static final int GET_ORDER_RECORD_LIST_3_HANDLER = 11;
    public static final int GET_ORDER_DETAIL_HANDLER = 15;
    public static final int SAVE_BEFORE_CLEANING_PICTURE_HANDLER = 16;
    public static final int BEFORE_CLEANING_UPLOAD_PICTURE_HANDLER = 17;
    public static final int SAVE_AFTER_CLEANING_PICTURE_HANDLER = 18;
    public static final int AFTER_CLEANING_UPLOAD_PICTURE_HANDLER = 19;
    public static final int GRAB_ORDERS_HANDLER = 20;
    public static final int CANCEL_ORDER_HANDLER = 21;
    public static final int WITHDRAW_TOTAL_HANDLER = 22;
    public static final int GET_TRANSACTION_RECORD_HANDLER = 23;
    public static final int GET_TRANSACTION_RECORD_WITHDRAW_HANDLER = 24;
    public static final int GET_TRANSACTION_RECORD_INCOME_HANDLER = 25;
    public static final int COMPLETE_ORDER_HANDLER = 26;
    public static final int SET_OR_CHANGE_PAY_PASSWORD_HANDLER = 27;
    public static final int VERIFY_TRANSACTION_PASS_HANDLER = 28;
    public static final int TOTAL_DATA_HANDLER = 29;
    public static final int AUTO_ORDERS_HANDLER = 30;
    public static final int GENERATE_NEAR_ORDER_HANDLER = 31;
    public static final int GET_AUTO_ORDERS_STATE_HANDLER = 32;
    public static final int BIND_DEVICE_TOKEN_HANDLER = 33;
    public static final int GET_ORDER_MSG_BY_ID_HANDLER = 34;
    public static final int CREATE_ORDER_STATE_HANDLER = 35;
    public static final int GET_PAGE_UNEXPECTED_EXIT_HANDLER = 36;

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 正则验证：车牌
     */
    public static final String REGEX_CAR_NUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";

    /**
     * 正则表达式：验证手机号
     */
    //public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
    //public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[0135678]|18[0-9]|14[579])[0-9]{8}$";
    public static final String REGEX_MOBILE = "^1[3-9]\\d{9}$";
    //public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\\\d{8}$";
}
