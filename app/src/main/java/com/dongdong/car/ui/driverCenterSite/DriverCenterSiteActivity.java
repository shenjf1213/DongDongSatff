package com.dongdong.car.ui.driverCenterSite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.ui.driverCenter.LoginActivity;
import com.dongdong.car.ui.driverCenterSite.accountAndSecurity.AccountAndSecurityActivity;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.DialogByTwoButton;

import okhttp3.OkHttpClient;

/**
 * Created by 沈 on 2017/6/7.
 */

public class DriverCenterSiteActivity extends BaseActivity implements View.OnClickListener {

    private ImageView driverCenterSiteBack; // 返回
    private RelativeLayout siteAccountAndSecurityRl; // 账号与安全
    private RadioGroup driverCenterRg1; // 实时路况
    private RadioButton open1, close1;
    private RadioGroup driverCenterRg2; // 音效提示
    private RadioButton open2, close2;
    private RelativeLayout siteServiceModelRl; // 服务车型
    private RelativeLayout siteServiceAgreementRl; // 服务协议
    private RelativeLayout siteCheckUpdateRl; // 检查更新
    private ImageView siteCheckUpdateIv; // 有需要更新时显示红点
    private RelativeLayout siteAboutUsRl; // 关于我们
    private RelativeLayout siteVersionRl; // 版本号
    private Button driverSignOutBtn; // 退出登录
    private View signOutView;
    private PopupWindow signOutPW;
    private SharedPreferences siteSpf;
    private String checkedBySpf1 = "";
    private String checkedBySpf2 = "";
    private String phone;

    private DialogByProgress dialogByProgress;
    private DialogByTwoButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_center_site_layout);

        dialogByProgress = new DialogByProgress(DriverCenterSiteActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        phone = intent.getStringExtra("PHONE");

        initView();
        initListener();
        initDate();
    }

    /**
     * 获取用户设置并赋值显示
     */
    private void initDate() {
        siteSpf = DriverCenterSiteActivity.this.getSharedPreferences("user_info", 0);
        checkedBySpf1 = siteSpf.getString("checked1", "");
        checkedBySpf2 = siteSpf.getString("checked2", "");
        if (!checkedBySpf1.equals("")) {
            if (checkedBySpf1.equals("1")) {
                close1.setChecked(true);
            } else if (checkedBySpf1.equals("2")) {
                open1.setChecked(true);
                driverCenterRg1.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
            }
        }
        if (!checkedBySpf2.equals("")) {
            if (checkedBySpf2.equals("3")) {
                close2.setChecked(true);
            } else if (checkedBySpf2.equals("4")) {
                open2.setChecked(true);
                driverCenterRg2.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        driverCenterSiteBack = (ImageView) findViewById(R.id.driver_center_site_back); // 返回
        siteAccountAndSecurityRl = (RelativeLayout) findViewById(R.id.site_account_and_security_rl); // 账号与安全
        driverCenterRg1 = (RadioGroup) findViewById(R.id.driver_center_rg1); // 实时路况
        open1 = (RadioButton) findViewById(R.id.open1);
        close1 = (RadioButton) findViewById(R.id.close1);
        driverCenterRg2 = (RadioGroup) findViewById(R.id.driver_center_rg2); // 音效提示
        open2 = (RadioButton) findViewById(R.id.open2);
        close2 = (RadioButton) findViewById(R.id.close2);
        siteServiceModelRl = (RelativeLayout) findViewById(R.id.site_service_model_rl); // 服务车型
        siteServiceAgreementRl = (RelativeLayout) findViewById(R.id.site_service_agreement_rl); // 服务协议
        siteCheckUpdateRl = (RelativeLayout) findViewById(R.id.site_check_update_rl); // 检查更新
        siteCheckUpdateIv = (ImageView) findViewById(R.id.site_check_update_iv); // 有需要更新时显示红点
        siteAboutUsRl = (RelativeLayout) findViewById(R.id.site_about_us_rl); // 关于我们
        siteVersionRl = (RelativeLayout) findViewById(R.id.site_version_rl); // 版本号
        driverSignOutBtn = (Button) findViewById(R.id.driver_sign_out_btn); // 退出登录
    }

    /**
     * 监听
     */
    private void initListener() {
        driverCenterSiteBack.setOnClickListener(this);
        siteAccountAndSecurityRl.setOnClickListener(this);
        driverCenterRg1.setOnClickListener(this);
        open1.setOnClickListener(this);
        open2.setOnClickListener(this);
        close1.setOnClickListener(this);
        close2.setOnClickListener(this);
        driverCenterRg2.setOnClickListener(this);
        siteServiceModelRl.setOnClickListener(this);
        siteServiceAgreementRl.setOnClickListener(this);
        siteCheckUpdateRl.setOnClickListener(this);
        siteAboutUsRl.setOnClickListener(this);
        driverSignOutBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_center_site_back: // 返回
                DriverCenterSiteActivity.this.finish();
                break;

            case R.id.site_account_and_security_rl: // 账号与安全
                Intent intent = new Intent(DriverCenterSiteActivity.this, AccountAndSecurityActivity.class);
                intent.putExtra("PHONE", phone);
                startActivity(intent);
                break;

            case R.id.close1: // 实时路况关
                driverCenterRg1.setBackgroundResource(R.drawable.radio_button_backgroud_un_shape);
                checkedBySpf1 = "1";
                saveBySPF1(checkedBySpf1);
                break;

            case R.id.open1: // 实时路况开
                driverCenterRg1.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
                checkedBySpf1 = "2";
                saveBySPF1(checkedBySpf1);
                break;

            case R.id.close2: // 音效提示关
                driverCenterRg2.setBackgroundResource(R.drawable.radio_button_backgroud_un_shape);
                checkedBySpf2 = "3";
                saveBySPF2(checkedBySpf2);
                break;

            case R.id.open2: // 音效提示开
                driverCenterRg2.setBackgroundResource(R.drawable.radio_button_backgroud_shape);
                checkedBySpf2 = "4";
                saveBySPF2(checkedBySpf2);
                break;

            case R.id.site_service_model_rl: // 服务车型
                startActivity(new Intent(DriverCenterSiteActivity.this, ServiceModelActivity.class));
                break;

            case R.id.site_service_agreement_rl: // 服务协议
                startActivity(new Intent(DriverCenterSiteActivity.this, ServiceAgreementActivity.class));
                break;

            case R.id.site_check_update_rl: // 检查更新

                break;

            case R.id.site_about_us_rl: // 关于我们
                startActivity(new Intent(DriverCenterSiteActivity.this, AboutUsActivity.class));
                break;

            case R.id.driver_sign_out_btn: // 退出登录
                showSignOutPw();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void showSignOutPw() {
        signOutView = View.inflate(DriverCenterSiteActivity.this, R.layout.exit_login_pw, null);
        signOutPW = new PopupWindow(signOutView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        signOutPW.setFocusable(true);
        signOutPW.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        signOutPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        signOutPW.showAtLocation(signOutView, Gravity.BOTTOM, 0, 0);

        Button exitLoginBt = (Button) signOutView.findViewById(R.id.exit_login_bt);
        Button exitLoginCancelBt = (Button) signOutView.findViewById(R.id.exit_login_cancel_bt);

        exitLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutPW.dismiss();
                dialog = new DialogByTwoButton(DriverCenterSiteActivity.this,
                        "提示",
                        "您确定要退出登录吗？",
                        "取消",
                        "确定"
                );
                dialog.show();
                dialog.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                    @Override
                    public void doNegative() {
                        dialog.dismiss();
                    }

                    @Override
                    public void doPositive() {
                        dialog.dismiss();
                        SharedPreferences sdf = DriverCenterSiteActivity.this.getSharedPreferences("user_info", 0);
                        if (sdf != null) {
                            sdf.edit().clear().commit();
                        }
                        startActivity(new Intent(DriverCenterSiteActivity.this, LoginActivity.class));
                        DriverCenterSiteActivity.this.finish();
                    }
                });
            }
        });

        /**
         * 取消
         */
        exitLoginCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutPW.dismiss();
            }
        });


    }

    /**
     * 保存用户的实时路况的偏好设置
     *
     * @param checkedBySpf1
     */
    private void saveBySPF1(String checkedBySpf1) {
        siteSpf = DriverCenterSiteActivity.this.getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor1 = siteSpf.edit();
        editor1.putString("checked1", checkedBySpf1);
        editor1.commit();
    }

    /**
     * 保存用户的音效提醒的偏好设置
     *
     * @param checkedBySpf2
     */
    private void saveBySPF2(String checkedBySpf2) {
        siteSpf = DriverCenterSiteActivity.this.getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor2 = siteSpf.edit();
        editor2.putString("checked2", checkedBySpf2);
        editor2.commit();
    }
}
