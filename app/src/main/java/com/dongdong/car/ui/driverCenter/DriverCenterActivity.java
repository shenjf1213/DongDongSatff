package com.dongdong.car.ui.driverCenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.autoOrders.AutoOrdersRequest;
import com.dongdong.car.entity.autoOrders.GetAutoOrdersRequest;
import com.dongdong.car.entity.startWork.WorkingRequest;
import com.dongdong.car.entity.startWork.WorkingResult;
import com.dongdong.car.entity.userInfo.UserInfoRequest;
import com.dongdong.car.entity.userInfo.UserInfoResult;
import com.dongdong.car.ui.driverCenterSite.DriverCenterSiteActivity;
import com.dongdong.car.ui.myWallet.MyWalletActivity;
import com.dongdong.car.ui.orderRecord.OrderRecordActivity;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.ScreenUtils;
import com.google.gson.Gson;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/5/27.
 */

public class DriverCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView driverCenterBack; // 返回
    private CircleImageView driverSetHead; // 头像
    private RelativeLayout driverCenterRl4; // 登录成功后的个人数据
    private RelativeLayout driverCenterRl6; // 订单记录
    private RelativeLayout driverCenterRl7; // 我的钱包
    private RelativeLayout driverCenterRl8; // 客服
    private RelativeLayout driverCenterRl9; // 设置
    private TextView driverName; // 姓名
    private TextView driverPhone; // 手机号
    private CheckBox driverGradeIv1, driverGradeIv2, driverGradeIv3, driverGradeIv4, driverGradeIv5; // 等级
    private String driverLevelStr; // 等级文字
    private TextView driverCenterOrderNumberTv; // 总单量
    private TextView driverCenterOrderTimeTv; // 总时长
    private TextView driverCenterEvaluationRateTv; // 好评率
    private RadioGroup driverCenterRg; // 自动接单复选框
    private RadioButton open, close; // 是否自动接单
    private View autoOrdersView;
    private PopupWindow autoOrdersPW; // 自动接单的弹窗
    private CheckBox autoOrdersCb1, autoOrdersCb2, autoOrdersCb3;
    private String autoOrderString = "";
    private FrameLayout driveCenterWorkType; // 开始接单或停止接单
    private ImageView orderTypeIv; // 显示开始工作或者停止工作的背景
    private TextView orderTypeTv; // 显示开始工作或者停止工作的文字
    private boolean isBeginning = false; // 是否开始工作
    private String LatitudeStr, LongitudeStr; // 接收司机的经纬度
    private String id = "";
    private String phone, userName, userSex, userId, userContactName, userContactPhone, userToatl, userBank, userBankCard;
    private String isWorkStr = "";
    private String isWork = "";
    private String isWorkingBySpf = "";
    private SharedPreferences spf;

    private DialogByOneButton dialog;
    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_USER_INFO_MESSAGE_HANDLER:
                    UserInfoRequest userInfoRequest = (UserInfoRequest) msg.obj;
                    hideDialogByProgress();
                    String userInfoState = userInfoRequest.getIsSucess();
                    if (userInfoState.equals("true")) {
                        UserInfoResult userInfoResult = userInfoRequest.getData();
                        Glide.with(getApplicationContext()).load(GlobalConsts.DONG_DONG_IMAGE_URL + userInfoResult.getHeadImage()).into(driverSetHead);
                        driverName.setText(userInfoResult.getDisplayUserName());
                        driverPhone.setText(userInfoResult.getMobilePhone());
                        driverCenterOrderNumberTv.setText(userInfoResult.getOrderDayNumber()); // 总单量
                        driverCenterOrderTimeTv.setText(userInfoResult.getOrderWorkTime()); // 总时长
                        driverCenterEvaluationRateTv.setText(userInfoResult.getFeedBackRate()); // 好评率

                        phone = userInfoResult.getMobilePhone();
                        userName = userInfoResult.getDisplayUserName();
                        userSex = userInfoResult.getSex();
                        userId = userInfoResult.getCardNo();
                        userContactName = userInfoResult.getContactUser();
                        userContactPhone = userInfoResult.getContactMobile();
                        userToatl = String.valueOf(userInfoResult.getPayTotal());
                        userBank = userInfoResult.getBankName();
                        userBankCard = userInfoResult.getBrankCard();
                        driverLevelStr = userInfoResult.getStaffLevel();

                        if (driverLevelStr != null && !TextUtils.isEmpty(driverLevelStr)) {
                            if (driverLevelStr.equals("1")) {
                                driverGradeIv1.setBackgroundResource(R.drawable.evaluation_c);
                            } else if (driverLevelStr.equals("2")) {
                                driverGradeIv1.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv2.setBackgroundResource(R.drawable.evaluation_c);
                            } else if (driverLevelStr.equals("3")) {
                                driverGradeIv1.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv2.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv3.setBackgroundResource(R.drawable.evaluation_c);
                            } else if (driverLevelStr.equals("4")) {
                                driverGradeIv1.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv2.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv4.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv5.setBackgroundResource(R.drawable.evaluation_c);
                            } else if (driverLevelStr.equals("5")) {
                                driverGradeIv1.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv2.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv3.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv4.setBackgroundResource(R.drawable.evaluation_c);
                                driverGradeIv5.setBackgroundResource(R.drawable.evaluation_c);
                            }
                        }
                    }
                    break;

                case GlobalConsts.IS_START_WORKING_HANGLER:
                    WorkingRequest workingRequest = (WorkingRequest) msg.obj;
                    hideDialogByProgress();
                    String workingStr = workingRequest.getMsg();
                    if (workingStr.equals("10010-成功提交")) {
                        WorkingResult workingResult = workingRequest.getData();
                        isWork = workingResult.getIsWork();
                        if (isWork.equals("1")) {
                            dialog = new DialogByOneButton(DriverCenterActivity.this, "提示", "已开始工作", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    saveWorkStateBySpf(isWork);
                                }
                            });
                        } else if (isWork.equals("0")) {
                            dialog = new DialogByOneButton(DriverCenterActivity.this, "提示", "已结束工作", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    saveWorkStateBySpf(isWork);
                                }
                            });
                        }
                    }
                    break;

                case GlobalConsts.AUTO_ORDERS_HANDLER:
                    AutoOrdersRequest autoOrdersRequest = (AutoOrdersRequest) msg.obj;
                    String autoOrdersState = autoOrdersRequest.getIsSucess();
                    if (autoOrdersState.equals("true")) {
                        if (autoOrdersPW != null && autoOrdersPW.isShowing()) {
                            autoOrdersPW.dismiss();
                        }
                        AutoOrdersRequest.AutoOrdersResult autoOrdersResult = autoOrdersRequest.getData();
                        String autoOrdersNumber = autoOrdersResult.getReceviceValue();
                        if (autoOrdersNumber.equals("0")) {
                            dialog = new DialogByOneButton(DriverCenterActivity.this, "提示", "已关闭自动接单", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    driverCenterRg.setBackgroundResource(R.drawable.radio_button_backgroud_un_shape);
                                }
                            });
                        } else {
                            dialog = new DialogByOneButton(DriverCenterActivity.this, "提示", "已开启自动接单", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    driverCenterRg.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
                                }
                            });
                        }
                    }
                    break;

                case GlobalConsts.GET_AUTO_ORDERS_STATE_HANDLER:
                    GetAutoOrdersRequest getAutoOrdersRequest = (GetAutoOrdersRequest) msg.obj;
                    String getAutoOrdersState = getAutoOrdersRequest.getIsSucess();
                    if (getAutoOrdersState.equals("true")) {
                        GetAutoOrdersRequest.GetAutoOrdersResult getAutoOrdersResult = getAutoOrdersRequest.getData();
                        String getAutoOrdersName = getAutoOrdersResult.getKeyName();
                        if (getAutoOrdersName.equals("关闭")) {
                            close.setChecked(true);
                            open.setChecked(false);
                            driverCenterRg.setBackgroundResource(R.drawable.radio_button_backgroud_un_shape);
                        } else {
                            close.setChecked(false);
                            open.setChecked(true);
                            driverCenterRg.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_center_layout);

        dialogByProgress = new DialogByProgress(DriverCenterActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        spf = DriverCenterActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        Intent intent = getIntent();
        LatitudeStr = intent.getStringExtra("LATITUDE");
        LongitudeStr = intent.getStringExtra("LONGITUDE");

        initView();
        initListener();
        initData();
    }

    /**
     * 获取个人中心数据
     */
    private void initData() {
        spf = DriverCenterActivity.this.getSharedPreferences("user_info", 0);
        isWorkingBySpf = spf.getString("isWorking", "");
        if (isWorkingBySpf.equals("0")) {
            orderTypeIv.setBackgroundResource(R.drawable.start_working); // 显示停止工作的背景
            orderTypeTv.setText("开始工作"); // 显示停止工作的文字
            isBeginning = false;
        } else if (isWorkingBySpf.equals("1")) {
            orderTypeIv.setBackgroundResource(R.drawable.stop_working); // 显示开始工作的背景
            orderTypeTv.setText("停止工作"); // 显示开始工作的文字
            isBeginning = true;
        }

        /**
         * 获取个人中心的数据
         */
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_USER_INFO_MESSAGE_URL + id).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "个人中心信息----" + bodyString.toString());
                Gson userInfoGson = new Gson();
                UserInfoRequest userInfoRequest = userInfoGson.fromJson(bodyString, UserInfoRequest.class);
                Message userInfoMsg = Message.obtain();
                userInfoMsg.what = GlobalConsts.GET_USER_INFO_MESSAGE_HANDLER;
                userInfoMsg.obj = userInfoRequest;
                handler.sendMessage(userInfoMsg);
            }
        });

        /**
         * 获取自动接单是否开启或关闭信息
         */
        Request request1 = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/GetRecevieFormInfo?staffId=" + id).build();
        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson getAutoOrdersGson = new Gson();
                GetAutoOrdersRequest getAutoOrdersRequest = getAutoOrdersGson.fromJson(bodyString, GetAutoOrdersRequest.class);
                Message getAutoOrdersMsg = Message.obtain();
                getAutoOrdersMsg.what = GlobalConsts.GET_AUTO_ORDERS_STATE_HANDLER;
                getAutoOrdersMsg.obj = getAutoOrdersRequest;
                handler.sendMessage(getAutoOrdersMsg);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        driverCenterBack = (ImageView) findViewById(R.id.driver_center_back); // 返回
        driverSetHead = (CircleImageView) findViewById(R.id.driver_set_head); // 头像
        driverCenterRl4 = (RelativeLayout) findViewById(R.id.driver_center_rl4); // 登录成功后的个人数据
        driverCenterRl6 = (RelativeLayout) findViewById(R.id.driver_center_rl6); // 订单记录
        driverCenterRl7 = (RelativeLayout) findViewById(R.id.driver_center_rl7); // 我的钱包
        driverCenterRl8 = (RelativeLayout) findViewById(R.id.driver_center_rl8); // 客服
        driverCenterRl9 = (RelativeLayout) findViewById(R.id.driver_center_rl9); // 设置
        driverName = (TextView) findViewById(R.id.driver_name); // 姓名
        driverPhone = (TextView) findViewById(R.id.driver_phone); // 手机号
        driverGradeIv1 = (CheckBox) findViewById(R.id.driver_grade_iv1); // 好评率
        driverGradeIv2 = (CheckBox) findViewById(R.id.driver_grade_iv2);
        driverGradeIv3 = (CheckBox) findViewById(R.id.driver_grade_iv3);
        driverGradeIv4 = (CheckBox) findViewById(R.id.driver_grade_iv4);
        driverGradeIv5 = (CheckBox) findViewById(R.id.driver_grade_iv5);
        driverCenterOrderNumberTv = (TextView) findViewById(R.id.driver_center_order_number_tv); // 总单量
        driverCenterOrderTimeTv = (TextView) findViewById(R.id.driver_center_order_time_tv); // 总时长
        driverCenterEvaluationRateTv = (TextView) findViewById(R.id.driver_center_evaluation_rate_tv); // 好评率
        driverCenterRg = (RadioGroup) findViewById(R.id.driver_center_rg); // 自动接单复选框
        open = (RadioButton) driverCenterRg.findViewById(R.id.open); // 是否自动接单
        close = (RadioButton) driverCenterRg.findViewById(R.id.close); // 是否自动接单
        driveCenterWorkType = (FrameLayout) findViewById(R.id.driver_center_work_type); // 开始接单或停止接单
        orderTypeIv = (ImageView) findViewById(R.id.order_type_iv); // 显示开始工作或者停止工作的背景
        orderTypeTv = (TextView) findViewById(R.id.order_type_tv); // 显示开始工作或者停止工作的文字
    }

    /**
     * 监听
     */
    private void initListener() {
        driverCenterBack.setOnClickListener(this);
        driverSetHead.setOnClickListener(this);
        driverCenterRl4.setOnClickListener(this); // 登录成功后的个人数据
        driverCenterRl6.setOnClickListener(this); // 订单记录
        driverCenterRl7.setOnClickListener(this); // 我的钱包
        driverCenterRl8.setOnClickListener(this); // 客服
        driverCenterRl9.setOnClickListener(this); // 设置
        open.setOnClickListener(this); // 是否自动接单
        close.setOnClickListener(this); // 是否自动接单
        driveCenterWorkType.setOnClickListener(this); // 开始接单或停止接单
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_center_back:
                DriverCenterActivity.this.finish();
                break;

            case R.id.driver_set_head: // 设置头像
                Intent intent = new Intent(DriverCenterActivity.this, DriverInformationActivity.class);
                intent.putExtra("NAME", userName);
                intent.putExtra("SEX", userSex);
                intent.putExtra("ID", userId);
                intent.putExtra("C_NAME", userContactName);
                intent.putExtra("C_PHONE", userContactPhone);
                startActivity(intent);
                break;

            case R.id.close: // 关闭自动接单
                autoOrderString = "0";
                saveAutoOrders(autoOrderString);
                break;

            case R.id.open: // 开启自动接单
                showAutoOrdersPW();
                break;

            case R.id.auto_orders_cb_1:
                autoOrdersCb2.setChecked(false);
                autoOrdersCb3.setChecked(false);
                autoOrderString = "1";
                break;

            case R.id.auto_orders_cb_2:
                autoOrdersCb1.setChecked(false);
                autoOrdersCb3.setChecked(false);
                autoOrderString = "2";
                break;

            case R.id.auto_orders_cb_3:
                autoOrdersCb1.setChecked(false);
                autoOrdersCb2.setChecked(false);
                autoOrderString = "3";
                break;

            case R.id.auto_orders_btn:
                if (autoOrdersCb1.isChecked() == false && autoOrdersCb2.isChecked() == false && autoOrdersCb3.isChecked() == false) {
                    Toast.makeText(DriverCenterActivity.this, "请选择一项优先条件", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveAutoOrders(autoOrderString);
                }
                break;

            case R.id.driver_center_rl6: // 订单记录
                startActivity(new Intent(DriverCenterActivity.this, OrderRecordActivity.class));
                break;

            case R.id.driver_center_rl7: // 我的钱包
                Intent intent2 = new Intent(DriverCenterActivity.this, MyWalletActivity.class);
                intent2.putExtra("TOTAL_ASSETS", userToatl); // 总资产
                intent2.putExtra("BANK_NAME", userBank); // 银行卡类型
                intent2.putExtra("BANK_CARD", userBankCard); // 银行卡号
                startActivity(intent2);
                break;

            case R.id.driver_center_rl8: // 客服
                startActivity(new Intent(DriverCenterActivity.this, CustomerServiceActivity.class));
                break;

            case R.id.driver_center_rl9: // 设置
                Intent intent1 = new Intent(DriverCenterActivity.this, DriverCenterSiteActivity.class);
                intent1.putExtra("PHONE", phone);
                startActivity(intent1);
                break;

            case R.id.driver_center_work_type: // 开始接单或停止接单
                if (isBeginning == false) {
                    isWorkStr = "1";
                    orderTypeIv.setBackgroundResource(R.drawable.stop_working); // 显示开始工作的背景
                    orderTypeTv.setText("停止工作"); // 显示开始工作的文字
                    isBeginning = true;
                    saveWorkState(isWorkStr);
                } else if (isBeginning == true) {
                    isWorkStr = "0";
                    orderTypeIv.setBackgroundResource(R.drawable.start_working); // 显示停止工作的背景
                    orderTypeTv.setText("开始工作"); // 显示停止工作的文字
                    isBeginning = false;
                    saveWorkState(isWorkStr);
                }
                break;
        }
    }

    /**
     * 自动接单的弹窗
     */
    private void showAutoOrdersPW() {
        autoOrdersView = View.inflate(DriverCenterActivity.this, R.layout.auto_orders_layout, null);
        int width = ScreenUtils.getScreenWidth(DriverCenterActivity.this) / 10 * 8;
        autoOrdersPW = new PopupWindow(autoOrdersView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundAlpha(0.8f);

        Button autoOrdersBtn = (Button) autoOrdersView.findViewById(R.id.auto_orders_btn);
        autoOrdersCb1 = (CheckBox) autoOrdersView.findViewById(R.id.auto_orders_cb_1);
        autoOrdersCb2 = (CheckBox) autoOrdersView.findViewById(R.id.auto_orders_cb_2);
        autoOrdersCb3 = (CheckBox) autoOrdersView.findViewById(R.id.auto_orders_cb_3);

        autoOrdersBtn.setOnClickListener(this);
        autoOrdersCb1.setOnClickListener(this);
        autoOrdersCb2.setOnClickListener(this);
        autoOrdersCb3.setOnClickListener(this);

        autoOrdersPW.setFocusable(true);
        autoOrdersPW.setOutsideTouchable(true);
        autoOrdersPW.setBackgroundDrawable(new ColorDrawable(0x000000));
        autoOrdersPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        autoOrdersPW.showAtLocation(autoOrdersView, Gravity.CENTER, 0, 0);
        autoOrdersPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (autoOrdersCb1.isChecked() == false && autoOrdersCb2.isChecked() == false && autoOrdersCb3.isChecked() == false) {
                    close.setChecked(true);
                    open.setChecked(false);
                    driverCenterRg.setBackgroundResource(R.drawable.radio_button_backgroud_un_shape);
                }
                backgroundAlpha(1f);
            }
        });

    }

    /**
     * 设置自动接单参数
     *
     * @param autoOrderString
     */
    private void saveAutoOrders(String autoOrderString) {
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/SetRecevieForm?staffId=" + id + "&receviceValue=" + autoOrderString).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson autoOrdersGson = new Gson();
                AutoOrdersRequest autoOrdersRequest = autoOrdersGson.fromJson(bodyString, AutoOrdersRequest.class);
                Message autoOrdersMsg = Message.obtain();
                autoOrdersMsg.what = GlobalConsts.AUTO_ORDERS_HANDLER;
                autoOrdersMsg.obj = autoOrdersRequest;
                handler.sendMessage(autoOrdersMsg);
            }
        });
    }

    /**
     * 设置工作状态
     *
     * @param isWorkStr
     */
    private void saveWorkState(String isWorkStr) {
        dialogByProgress.show();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("staffId", id);
        builder.add("isWork", isWorkStr);
        builder.add("longitude", LongitudeStr);
        builder.add("latitude", LatitudeStr);
        FormBody body = builder.build();
        Request request = new Request.Builder().url(GlobalConsts.IS_START_WORKING_URL).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "开始或结束工作----" + bodyString.toString());
                Gson workingGson = new Gson();
                WorkingRequest workingRequest = workingGson.fromJson(bodyString, WorkingRequest.class);
                Message workingMsg = Message.obtain();
                workingMsg.what = GlobalConsts.IS_START_WORKING_HANGLER;
                workingMsg.obj = workingRequest;
                handler.sendMessage(workingMsg);
            }
        });
    }

    /**
     * 偏好设置保存工作状态
     *
     * @param isWork
     */
    private void saveWorkStateBySpf(String isWork) {
        spf = DriverCenterActivity.this.getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("isWorking", isWork);
        editor.commit();
    }

    /**
     *  设置添加屏幕的背景透明度 
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = DriverCenterActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0  
        DriverCenterActivity.this.getWindow().setAttributes(lp);
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoOrdersPW != null && autoOrdersPW.isShowing()) {
            autoOrdersPW.dismiss();
        }
    }
}
