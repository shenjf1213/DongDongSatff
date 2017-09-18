package com.dongdong.car.ui.driverCenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.DialogByTwoButton;
import com.dongdong.car.util.GlobalConsts;

/**
 * Created by 沈 on 2017/6/7.
 */

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView customerServiceBack;
    private RelativeLayout customerServiceRl1, customerServiceRl2, customerServiceRl3;
    private Intent callPhoneIntent;
    private String callPhoneString;

    private DialogByTwoButton dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_layout);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(CustomerServiceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerServiceActivity.this, new String[]{Manifest.permission.CALL_PHONE}, GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        customerServiceBack = (ImageView) findViewById(R.id.customer_service_back);
        customerServiceRl1 = (RelativeLayout) findViewById(R.id.customer_service_rl1);
        customerServiceRl2 = (RelativeLayout) findViewById(R.id.customer_service_rl2);
        customerServiceRl3 = (RelativeLayout) findViewById(R.id.customer_service_rl3);
    }

    /**
     * 监听
     */
    private void initListener() {
        customerServiceBack.setOnClickListener(this);
        customerServiceRl1.setOnClickListener(this);
        customerServiceRl2.setOnClickListener(this);
        customerServiceRl3.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_service_back: // 返回
                CustomerServiceActivity.this.finish();
                break;

            case R.id.customer_service_rl1: // 管理员客服
                callPhoneString = GlobalConsts.CALL_SERVICE_PHONE_1;
                callPhone(callPhoneString);
                break;

            case R.id.customer_service_rl2: // 客服1
                callPhoneString = GlobalConsts.CALL_SERVICE_PHONE_2;
                callPhone(callPhoneString);
                break;

            case R.id.customer_service_rl3: // 客服2
                callPhoneString = GlobalConsts.CALL_SERVICE_PHONE_3;
                callPhone(callPhoneString);
                break;
        }
    }

    /**
     * 呼叫电话
     *
     * @param callPhoneString
     */
    private void callPhone(final String callPhoneString) {
        dialog = new DialogByTwoButton(CustomerServiceActivity.this, "提示", "是否呼叫客服电话：" + callPhoneString, "取消", "确定");
        dialog.show();
        dialog.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
            @Override
            public void doNegative() {
                dialog.dismiss();
            }

            @Override
            public void doPositive() {
                callPhoneIntent = new Intent();
                callPhoneIntent.setAction(Intent.ACTION_CALL);
                callPhoneIntent.setData(Uri.parse("tel:" + callPhoneString));
                startActivity(callPhoneIntent);
                dialog.dismiss();
            }
        });
    }
}
