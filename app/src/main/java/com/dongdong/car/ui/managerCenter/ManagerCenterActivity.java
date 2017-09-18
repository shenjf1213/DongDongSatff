package com.dongdong.car.ui.managerCenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.ui.driverCenter.LoginActivity;
import com.dongdong.car.util.DialogByTwoButton;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 沈 on 2017/6/12.
 */

public class ManagerCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView managerCenterMessage; // 管理员消息
    private ImageView managerCenterBack; // 返回
    private CircleImageView managerCenterSetHead; // 头像
    private TextView managerCenterName; // 姓名
    private TextView managerCenterLevel; // 级别
    private TextView managerCenterId; // id号码
    private TextView managerCenterPhone; // 联系电话
    private TextView managerOrderList; // 订单列表
    private TextView managerPersonnelList; // 人员列表
    //private PtrClassicFrameLayout managerCenterRecyclerViewFrame; // 上拉下拉控件
    private RecyclerView managerCenterRecyclerView; // 数据列表

    //private RecyclerAdapterWithHF managerAdapter; // 上拉下拉适配器

    private DialogByTwoButton dialog2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_center_layout);

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        managerCenterMessage = (ImageView) findViewById(R.id.manager_center_message); // 管理员消息
        managerCenterBack = (ImageView) findViewById(R.id.manager_center_back); // 返回
        managerCenterSetHead = (CircleImageView) findViewById(R.id.manager_center_set_head); // 头像
        managerCenterName = (TextView) findViewById(R.id.manager_center_name); // 姓名
        managerCenterLevel = (TextView) findViewById(R.id.manager_center_level); // 级别
        managerCenterId = (TextView) findViewById(R.id.manager_center_id); // id号码
        managerCenterPhone = (TextView) findViewById(R.id.manager_center_phone); // 联系电话
        managerOrderList = (TextView) findViewById(R.id.manager_order_list); // 订单列表
        managerPersonnelList = (TextView) findViewById(R.id.manager_personnel_list); // 人员列表
        //managerCenterRecyclerViewFrame = (PtrClassicFrameLayout) findViewById(R.id.manager_center_recycler_view_frame); // 上拉下拉控件
        managerCenterRecyclerView = (RecyclerView) findViewById(R.id.manager_center_RecyclerView); // 数据列表
    }

    /**
     * 监听
     */
    private void initListener() {
        managerCenterMessage.setOnClickListener(this);
        managerCenterBack.setOnClickListener(this);
        managerOrderList.setOnClickListener(this);
        managerPersonnelList.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manager_center_message: // 消息中心

                break;

            case R.id.manager_center_back: // 返回
                dialog2 = new DialogByTwoButton(ManagerCenterActivity.this, "提示", "确定退出登录吗？", "取消", "确定");
                dialog2.show();
                dialog2.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                    @Override
                    public void doNegative() {
                        dialog2.dismiss();
                    }

                    @Override
                    public void doPositive() {
                        dialog2.dismiss();
                        SharedPreferences spf = ManagerCenterActivity.this.getSharedPreferences("user_info", 0);
                        if (spf != null) {
                            spf.edit().clear().commit();
                        }
                        startActivity(new Intent(ManagerCenterActivity.this, LoginActivity.class));
                        ManagerCenterActivity.this.finish();
                    }
                });
                break;

            case R.id.manager_order_list: // 订单列表
                managerOrderList.setTextColor(getResources().getColor(R.color.white));
                managerOrderList.setBackgroundResource(R.color.orange);
                managerPersonnelList.setTextColor(getResources().getColor(R.color.orange));
                managerPersonnelList.setBackgroundResource(R.color.background_color);
                break;

            case R.id.manager_personnel_list: // 人员列表
                managerOrderList.setTextColor(getResources().getColor(R.color.orange));
                managerOrderList.setBackgroundResource(R.color.background_color);
                managerPersonnelList.setTextColor(getResources().getColor(R.color.white));
                managerPersonnelList.setBackgroundResource(R.color.orange);
                break;
        }
    }
}
