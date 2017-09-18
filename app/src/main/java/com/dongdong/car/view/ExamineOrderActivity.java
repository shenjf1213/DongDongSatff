package com.dongdong.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.ui.navigationMap.NavigationActivity;
import com.dongdong.car.util.CoverFlowViewPager;
import com.dongdong.car.util.GlobalConsts;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 沈 on 2017/6/6.
 */

public class ExamineOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView examineOrderBack; // 返回
    private TextView examineOrderNumber; // 订单编号
    private TextView examineOrderTime; // 订单时间
    private TextView examineOrderAddress; // 订单地址
    private TextView examineOrderName; // 客户姓名
    private TextView examineOrderPhone; // 客户电话
    private TextView examineOrderCarMsg; // 车辆信息
    private TextView examineOrderItems; // 清洗项目
    private TextView examineOrderTotal; // 清洗总价
    private TextView examineOrderRemarks; // 备注
    private TextView aboutCarPictureTv;
    private CircleImageView examineOrderPicture; // 车辆周围的照片集合
    private Button examineOrderStartOrderingBtn; // 开始接单
    private String LongitudeStr, LatitudeStr, staffLongitude, staffLatitude; // 接收服务人员的经纬度
    private String ordersId; // 获取订单id

    private List<String> aboutCarList;
    private CoverFlowViewPager examineOrderCover;
    private List<View> ExamineOrderPicViews = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_order_layout);

        initView();
        initListener();

        Intent intent = getIntent();
        ordersId = intent.getStringExtra("ID");
        examineOrderNumber.setText(intent.getStringExtra("Number"));
        examineOrderTime.setText(intent.getStringExtra("Time"));
        examineOrderAddress.setText(intent.getStringExtra("Address"));
        examineOrderName.setText(intent.getStringExtra("Name"));
        examineOrderPhone.setText(intent.getStringExtra("Phone"));
        examineOrderCarMsg.setText(intent.getStringExtra("CarMsg"));
        examineOrderTotal.setText(intent.getStringExtra("Total"));
        examineOrderRemarks.setText(intent.getStringExtra("Remarks"));
        examineOrderItems.setText(intent.getStringExtra("Items"));

        aboutCarList = intent.getStringArrayListExtra("aboutCarList");
        LongitudeStr = intent.getStringExtra("LONGITUDE");
        LatitudeStr = intent.getStringExtra("LATITUDE");
        staffLongitude = intent.getStringExtra("STAFF_LONGITUDE");
        staffLatitude = intent.getStringExtra("STAFF_LATITUDE");

        if (aboutCarList != null && !aboutCarList.isEmpty()) {
            aboutCarPictureTv.setVisibility(View.VISIBLE);
            examineOrderPicture.setVisibility(View.VISIBLE);
            Glide.with(ExamineOrderActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + aboutCarList.get(0)).into(examineOrderPicture);
            examineOrderPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutCarPictureTv.setVisibility(View.GONE);
                    examineOrderCover.setVisibility(View.VISIBLE);
                    for (int i = 0; i < aboutCarList.size(); i++) { // 初始化数据
                        ImageView img = new ImageView(ExamineOrderActivity.this);
                        Glide.with(ExamineOrderActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + aboutCarList.get(i)).into(img);
                        ExamineOrderPicViews.add(img);
                    }
                    examineOrderCover.setViewList(ExamineOrderPicViews); // 设置显示的数据
                }
            });
        } else {
            aboutCarPictureTv.setVisibility(View.GONE);
            examineOrderPicture.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        examineOrderBack = (ImageView) findViewById(R.id.examine_order_back); // 返回
        examineOrderNumber = (TextView) findViewById(R.id.examine_order_number); // 订单编号
        examineOrderTime = (TextView) findViewById(R.id.examine_order_time); // 订单时间
        examineOrderAddress = (TextView) findViewById(R.id.examine_order_address); // 订单地址
        examineOrderName = (TextView) findViewById(R.id.examine_order_name); // 客户姓名
        examineOrderPhone = (TextView) findViewById(R.id.examine_order_phone); // 客户电话
        examineOrderCarMsg = (TextView) findViewById(R.id.examine_order_car_msg); // 车辆信息
        examineOrderItems = (TextView) findViewById(R.id.examine_order_items); // 清洗项目
        examineOrderTotal = (TextView) findViewById(R.id.examine_order_total); // 清洗总价
        examineOrderRemarks = (TextView) findViewById(R.id.examine_order_remarks); // 备注
        aboutCarPictureTv = (TextView) findViewById(R.id.about_car_picture_tv);
        examineOrderPicture = (CircleImageView) findViewById(R.id.examine_order_picture); // 订单中车辆周围照片
        examineOrderCover = (CoverFlowViewPager) findViewById(R.id.examine_order_cover); // 图片轮播控件
        examineOrderStartOrderingBtn = (Button) findViewById(R.id.order_details_start_ordering_btn); // 确认
    }

    /**
     * 监听
     */
    private void initListener() {
        examineOrderBack.setOnClickListener(this);
        examineOrderPicture.setOnClickListener(this);
        examineOrderStartOrderingBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.examine_order_back:
                ExamineOrderActivity.this.finish();
                break;

            case R.id.order_details_start_ordering_btn:
                Intent intent = new Intent(ExamineOrderActivity.this, NavigationActivity.class);
                intent.putExtra("ORDER_ID", ordersId);
                intent.putExtra("STAFF_LATITUDE", staffLatitude); // 服务人员的纬度
                intent.putExtra("STAFF_LONGITUDE", staffLongitude); // 服务人员的经度
                intent.putExtra("LATITUDE", LatitudeStr);
                intent.putExtra("LONGITUDE", LongitudeStr);
                startActivity(intent);
                ExamineOrderActivity.this.finish();
                break;
        }
    }
}
