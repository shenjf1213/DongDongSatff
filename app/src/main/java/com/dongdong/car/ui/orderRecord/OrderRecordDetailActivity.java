package com.dongdong.car.ui.orderRecord;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.orderRecordDetail.DetailByOrderInfo;
import com.dongdong.car.entity.orderRecordDetail.DetailByUserInfo;
import com.dongdong.car.entity.orderRecordDetail.OrderItemList;
import com.dongdong.car.entity.orderRecordDetail.OrderPictureList;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailRequest;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailResult;
import com.dongdong.car.util.CoverFlowViewPager;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/21.
 */

public class OrderRecordDetailActivity extends BaseActivity implements View.OnClickListener {

    private String orderRecordID; // 可供查询订单详情的订单编号
    private ImageView orderRecordDetailsBack; // 返回
    private TextView orderRecordDetailsNumber; // 订单编号
    private TextView orderRecordDetailsTime; // 订单时间
    private TextView orderRecordDetailsAddress; // 订单地址
    private TextView orderRecordDetailsName; // 客户姓名
    private TextView orderRecordDetailsCarMsg; // 车辆信息
    private TextView orderRecordDetailsItems; // 清洗项目
    private TextView orderRecordDetailsTotal; // 清洗总价
    private TextView orderRecordDetailsRemarks; // 备注
    private TextView orderRecordDetailsTv;
    private CircleImageView orderRecordDetailsPicture; // 车辆周围的照片集合
    private List<OrderItemList> orderDetailItemLists = new ArrayList<>(); // 清洗项目的集合
    private List<String> list = new ArrayList<>();
    private List<OrderPictureList> orderDetailPictureLists = new ArrayList<>(); // 车辆周围照片集合
    private List<String> list2 = new ArrayList<>();
    private String orderDetailString;

    private List<View> aboutCarList = new ArrayList<>();
    private CoverFlowViewPager orderRecordDetailsCover; // 照片轮播控件

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_DETAIL_HANDLER:
                    OrderRecordDetailRequest detailRequest = (OrderRecordDetailRequest) msg.obj;
                    hideDialogByProgress();
                    String detailStr = detailRequest.getMsg();
                    if ("订单客户Info".equals(detailStr)) {
                        OrderRecordDetailResult detailResult = detailRequest.getData();
                        DetailByOrderInfo detailByOrderInfo = detailResult.getOrderInfo();
                        DetailByUserInfo detailByUserInfo = detailResult.getUserInfo();

                        orderRecordDetailsNumber.setText(detailByOrderInfo.getOrderNumber()); // 订单编号
                        orderRecordDetailsTime.setText(detailByOrderInfo.getOrderTime()); // 订单时间
                        orderRecordDetailsAddress.setText(detailByOrderInfo.getAddress()); // 订单地址
                        orderRecordDetailsName.setText(detailByUserInfo.getUserName()); // 客户姓名
                        orderRecordDetailsCarMsg.setText(detailByUserInfo.getCarNumber() + " " + detailByUserInfo.getCarModel() + " " + detailByUserInfo.getCarColor()); // 车辆信息
                        orderRecordDetailsTotal.setText(detailByOrderInfo.getOrderTotal() + " 元"); // 清洗总价
                        orderDetailItemLists = detailByOrderInfo.getOrderItemList();
                        orderDetailPictureLists = detailByOrderInfo.getOrderPictureList();

                        if (detailByUserInfo.getRemark() != null && !detailByUserInfo.getRemark().isEmpty()) { // 备注
                            orderRecordDetailsRemarks.setText(detailByUserInfo.getRemark());
                        } else {
                            orderRecordDetailsRemarks.setText("无");
                        }

                        if (orderDetailItemLists != null && !orderDetailItemLists.isEmpty()) {
                            for (int i = 0; i < orderDetailItemLists.size(); i++) {
                                list.add(orderDetailItemLists.get(i).getItemName());
                            }
                            orderDetailString = String.valueOf(list.subList(0, list.size()));
                            orderRecordDetailsItems.setText(orderDetailString.substring(1, orderDetailString.length() - 1));
                        } else {
                            orderRecordDetailsItems.setText(""); // 清洗项目
                        }

                        if (orderDetailPictureLists != null && !orderDetailPictureLists.isEmpty()) {
                            for (int i = 0; i < orderDetailPictureLists.size(); i++) {
                                list2.add(orderDetailPictureLists.get(i).getImgeFile());
                            }
                            orderRecordDetailsTv.setVisibility(View.VISIBLE);
                            orderRecordDetailsPicture.setVisibility(View.VISIBLE);
                            Glide.with(OrderRecordDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(orderRecordDetailsPicture);
                            orderRecordDetailsPicture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    orderRecordDetailsPicture.setVisibility(View.GONE);
                                    orderRecordDetailsCover.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < list2.size(); i++) { // 初始化数据
                                        ImageView img = new ImageView(OrderRecordDetailActivity.this);
                                        Glide.with(OrderRecordDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(i)).into(img);
                                        aboutCarList.add(img);
                                    }
                                    orderRecordDetailsCover.setViewList(aboutCarList); // 设置显示的数据
                                }
                            });
                        } else {
                            orderRecordDetailsTv.setVisibility(View.GONE);
                            orderRecordDetailsPicture.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_details_layout);

        dialogByProgress = new DialogByProgress(OrderRecordDetailActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        orderRecordID = intent.getStringExtra("ORDER_RECORD_ID");

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        orderRecordDetailsBack = (ImageView) findViewById(R.id.order_record_details_back); // 返回
        orderRecordDetailsNumber = (TextView) findViewById(R.id.order_record_details_number); // 订单编号
        orderRecordDetailsTime = (TextView) findViewById(R.id.order_record_details_time); // 订单时间
        orderRecordDetailsAddress = (TextView) findViewById(R.id.order_record_details_address); // 订单地址
        orderRecordDetailsName = (TextView) findViewById(R.id.order_record_details_name); // 客户姓名
        orderRecordDetailsCarMsg = (TextView) findViewById(R.id.order_record_details_car_msg); // 车辆信息
        orderRecordDetailsItems = (TextView) findViewById(R.id.order_record_details_items); // 清洗项目
        orderRecordDetailsTotal = (TextView) findViewById(R.id.order_record_details_total); // 清洗总价
        orderRecordDetailsRemarks = (TextView) findViewById(R.id.order_record_details_remarks); // 备注
        orderRecordDetailsTv = (TextView) findViewById(R.id.order_record_details_tv9);
        orderRecordDetailsPicture = (CircleImageView) findViewById(R.id.order_record_details_picture); // 车辆周围的照片集合
        orderRecordDetailsCover = (CoverFlowViewPager) findViewById(R.id.order_record_details_cover); // 照片轮播控件
    }

    /**
     * 获取数据并赋值
     */
    private void initData() {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_ORDER_DETAIL_URL + orderRecordID).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "获取订单详细信息-----" + bodyString.toString());
                Gson detailGson = new Gson();
                OrderRecordDetailRequest detailRequest = detailGson.fromJson(bodyString, OrderRecordDetailRequest.class);
                Message detailMsg = Message.obtain();
                detailMsg.what = GlobalConsts.GET_ORDER_DETAIL_HANDLER;
                detailMsg.obj = detailRequest;
                handler.sendMessage(detailMsg);
            }
        });
    }

    /**
     * 监听
     */
    private void initListener() {
        orderRecordDetailsBack.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_record_details_back:
                OrderRecordDetailActivity.this.finish();
                break;
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
