package com.dongdong.car.ui.driverCenterSite.accountAndSecurity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.ui.myWallet.TransactionPasActivity;
import com.dongdong.car.util.DialogByProgress;

import okhttp3.OkHttpClient;

/**
 * Created by 沈 on 2017/6/7.
 */

public class AccountAndSecurityActivity extends BaseActivity implements View.OnClickListener {

    private ImageView accountAndSecurityBack;
    private TextView accountAndSecurityPhoneTv;
    private RelativeLayout accountAndSecurityRl1, accountAndSecurityRl2, accountAndSecurityRl3;
    private SharedPreferences spf; // 获取是否已经设置了支付密码
    private String transactionPasStr = ""; // 获取支付密码的参数
    private String id, name, phone;

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_security_layout);

        dialogByProgress = new DialogByProgress(AccountAndSecurityActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        spf = AccountAndSecurityActivity.this.getSharedPreferences("user_info", 0);
        transactionPasStr = spf.getString("transactionPas", "");
        id = spf.getString("uid", "");

        Intent intent = getIntent();
        phone = intent.getStringExtra("PHONE");

        initView();
        initDate(phone);
        initListener();

    }

    /**
     * 获取数据并赋值
     *
     * @param phone
     */
    private void initDate(String phone) {
        if (phone.length() == 11) {
            accountAndSecurityPhoneTv.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
        } else {
            accountAndSecurityPhoneTv.setText(phone.substring(0, 3) + "****" + phone.substring(7, phone.length()));
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        accountAndSecurityBack = (ImageView) findViewById(R.id.account_and_security_back);
        accountAndSecurityPhoneTv = (TextView) findViewById(R.id.account_and_security_phone_tv);
        accountAndSecurityRl1 = (RelativeLayout) findViewById(R.id.account_and_security_rl1);
        accountAndSecurityRl2 = (RelativeLayout) findViewById(R.id.account_and_security_rl2);
        accountAndSecurityRl3 = (RelativeLayout) findViewById(R.id.account_and_security_rl3);
    }

    /**
     * 监听
     */
    private void initListener() {
        accountAndSecurityBack.setOnClickListener(this);
        accountAndSecurityRl1.setOnClickListener(this);
        accountAndSecurityRl2.setOnClickListener(this);
        accountAndSecurityRl3.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_and_security_back:
                AccountAndSecurityActivity.this.finish();
                break;

            case R.id.account_and_security_rl1: // 修改手机号
                Intent intent = new Intent(AccountAndSecurityActivity.this, ReplacePhoneActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("PHONE", phone);
                startActivity(intent);
                break;

            case R.id.account_and_security_rl2: // 修改登录密码
                startActivity(new Intent(AccountAndSecurityActivity.this, ResetPasswordActivity.class));
                break;

            case R.id.account_and_security_rl3: // 修改支付密码
                if (transactionPasStr.equals("")) { // 如果未设置支付密码，则先去设置交易密码
                    startActivity(new Intent(AccountAndSecurityActivity.this, TransactionPasActivity.class));
                } else { // 通过交易密码的验证
                    startActivity(new Intent(AccountAndSecurityActivity.this, ResetPayPassActivity.class));
                }
                break;
        }
    }
}
