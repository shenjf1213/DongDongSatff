package com.dongdong.car.ui.driverCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;

/**
 * Created by 沈 on 2017/6/6.
 */

public class DriverInformationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView driverInformationBack; // 返回
    private TextView driverInformationName; // 姓名
    private TextView driverInformationSex; // 性别
    private String driverSex;
    private TextView driverInformationIdNumber; // 身份证号
    private TextView driverInformationEmergencyContact; // 紧急联系人
    private TextView driverInformationEmergencyContactPhone; // 紧急联系人电话

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information_layout);

        initView();
        initData();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        driverInformationBack = (ImageView) findViewById(R.id.driver_information_back); // 返回
        driverInformationName = (TextView) findViewById(R.id.driver_information_name); // 姓名
        driverInformationSex = (TextView) findViewById(R.id.driver_information_sex); // 性别
        driverInformationIdNumber = (TextView) findViewById(R.id.driver_information_id_number); // 身份证号
        driverInformationEmergencyContact = (TextView) findViewById(R.id.driver_information_emergency_contact); // 紧急联系人
        driverInformationEmergencyContactPhone = (TextView) findViewById(R.id.driver_information_emergency_contact_phone); // 紧急联系人电话
    }

    /**
     * 获取数据并赋值
     */
    private void initData() {
        Intent intent = getIntent();
        driverInformationName.setText(intent.getStringExtra("NAME"));
        driverInformationIdNumber.setText(intent.getStringExtra("ID"));
        driverInformationEmergencyContact.setText(intent.getStringExtra("C_NAME"));
        driverInformationEmergencyContactPhone.setText(intent.getStringExtra("C_PHONE"));
        driverSex = intent.getStringExtra("SEX");
        if (driverSex.equals("0")) {
            driverInformationSex.setText("男");
        } else if (driverSex.equals("1")) {
            driverInformationSex.setText("女");
        }
    }

    /**
     * 监听
     */
    private void initListener() {
        driverInformationBack.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_information_back:
                DriverInformationActivity.this.finish();
                break;
        }
    }
}
