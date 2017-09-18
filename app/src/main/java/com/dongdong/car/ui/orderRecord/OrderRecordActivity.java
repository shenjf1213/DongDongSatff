package com.dongdong.car.ui.orderRecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dongdong.car.R;
import com.dongdong.car.adapter.OrderRecordAdapter;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.fragment.completedOrderFragment;
import com.dongdong.car.fragment.makingOrderFragment;
import com.dongdong.car.fragment.processingOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沈 on 2017/6/2.
 */

public class OrderRecordActivity extends BaseActivity implements View.OnClickListener {

    private List<Fragment> orderRecordFragment;
    private OrderRecordAdapter orderRecordAdapter;
    private ImageView orderRecordBack;
    private RadioGroup orderRecordRg;
    private RadioButton completedOrderRb; // 已完成的订单
    private RadioButton makingOrderRb; // 预约订单
    private RadioButton processingOrderRb; // 进行中的订单
    private ViewPager orderRecordViewPager;

    private OrderRecordBroadcastReceiver orderRecordBroadcastReceiver = new OrderRecordBroadcastReceiver(); // 广播

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_record_layout);

        initView();
        initFragment();
        initListener();
        onFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("COMPLETE_ORDER");
        OrderRecordActivity.this.registerReceiver(orderRecordBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OrderRecordActivity.this.unregisterReceiver(orderRecordBroadcastReceiver);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        orderRecordBack = (ImageView) findViewById(R.id.order_record_back);
        orderRecordRg = (RadioGroup) findViewById(R.id.order_record_rg);
        processingOrderRb = (RadioButton) findViewById(R.id.processing_order_rb); // 进行中的订单
        makingOrderRb = (RadioButton) findViewById(R.id.making_order_rb); // 预约订单
        completedOrderRb = (RadioButton) findViewById(R.id.completed_order_rb); // 已完成的订单
        orderRecordViewPager = (ViewPager) findViewById(R.id.order_record_view_pager);
    }

    /**
     * 新建碎片布局
     */
    private void initFragment() {
        orderRecordFragment = new ArrayList<>();
        orderRecordFragment.add(new processingOrderFragment()); // 进行中的订单
        orderRecordFragment.add(new makingOrderFragment()); // 预约订单
        orderRecordFragment.add(new completedOrderFragment()); // 已完成订单
        orderRecordAdapter = new OrderRecordAdapter(OrderRecordActivity.this.getSupportFragmentManager(), orderRecordFragment);
        orderRecordViewPager.setAdapter(orderRecordAdapter);
    }

    /**
     * 监听
     */
    private void initListener() {
        orderRecordBack.setOnClickListener(this);
    }

    /**
     * 导航与页面联动
     */
    private void onFragment() {
        orderRecordRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.processing_order_rb: // 进行中的订单
                        orderRecordViewPager.setCurrentItem(0);
                        completedOrderRb.setBackgroundResource(R.color.background_color);
                        completedOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        makingOrderRb.setBackgroundResource(R.color.background_color);
                        makingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        processingOrderRb.setBackgroundResource(R.drawable.text_view_top_bg);
                        processingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.white));
                        break;

                    case R.id.making_order_rb: // 预约的订单
                        orderRecordViewPager.setCurrentItem(1);
                        completedOrderRb.setBackgroundResource(R.color.background_color);
                        completedOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        makingOrderRb.setBackgroundResource(R.drawable.text_view_top_bg);
                        makingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.white));
                        processingOrderRb.setBackgroundResource(R.color.background_color);
                        processingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        break;

                    case R.id.completed_order_rb: // 已完成的订单
                        orderRecordViewPager.setCurrentItem(2);
                        completedOrderRb.setBackgroundResource(R.drawable.text_view_top_bg);
                        completedOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.white));
                        makingOrderRb.setBackgroundResource(R.color.background_color);
                        makingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        processingOrderRb.setBackgroundResource(R.color.background_color);
                        processingOrderRb.setTextColor(ContextCompat.getColor(OrderRecordActivity.this, R.color.black));
                        break;
                }
            }
        });

        orderRecordViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        processingOrderRb.setChecked(true);
                        break;

                    case 1:
                        makingOrderRb.setChecked(true);
                        break;

                    case 2:
                        completedOrderRb.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_record_back:
                OrderRecordActivity.this.finish();
                break;
        }
    }

    private class OrderRecordBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "COMPLETE_ORDER":
                    orderRecordViewPager.setCurrentItem(2);
                    break;
            }
        }
    }
}
