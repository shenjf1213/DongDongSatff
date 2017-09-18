package com.dongdong.car.view;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongdong.car.R;

/**
 * Created by 沈 on 2017/6/4.
 */

public class ShowNearOrderPW extends PopupWindow implements View.OnClickListener {

    private Context context;
    private LayoutInflater inflater;
    private View nearOrderView;
    private LinearLayout nearOrdersPwLl;
    private RelativeLayout nearOrdersPwRl1, nearOrdersPwRl2;
    private Intent nearIntent = new Intent();

    public ShowNearOrderPW(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        nearOrderView = inflater.inflate(R.layout.near_order_style_layout, null);
        initView();
        initListener();
        nearOrdersPwRl1.setSelected(true);
        this.setContentView(nearOrderView);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(android.R.style.Animation_InputMethod);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.update();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        nearOrdersPwLl = (LinearLayout) nearOrderView.findViewById(R.id.near_orders_pw_ll);
        nearOrdersPwRl1 = (RelativeLayout) nearOrderView.findViewById(R.id.near_orders_pw_rl1);
        nearOrdersPwRl2 = (RelativeLayout) nearOrderView.findViewById(R.id.near_orders_pw_rl2);
    }

    /**
     * 监听
     */
    private void initListener() {
        nearOrdersPwRl1.setOnClickListener(this);
        nearOrdersPwRl2.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.near_orders_pw_rl1:
                nearOrdersClick(view);
                nearIntent.setAction("HOME_WASHING");
                context.sendBroadcast(nearIntent);
                break;

            case R.id.near_orders_pw_rl2:
                nearOrdersClick(view);
                nearIntent.setAction("MAKE_WASHING");
                context.sendBroadcast(nearIntent);
                break;
        }
    }

    /**
     * 点击循环判断
     *
     * @param view
     */
    private void nearOrdersClick(View view) {
        int nearOrdersCount = nearOrdersPwLl.getChildCount();
        for (int i = 0; i < nearOrdersCount; i++) {
            if (view == nearOrdersPwLl.getChildAt(i)) {
                nearOrdersPwLl.getChildAt(i).setSelected(true);
            } else {
                nearOrdersPwLl.getChildAt(i).setSelected(false);
            }
        }
    }

    /**
     * 位于父控件mainListActionLl下方显示poupuwindow
     */
    public void showPricePopupWindow(TextView tv) {
        showAsDropDown(tv);
    }
}
