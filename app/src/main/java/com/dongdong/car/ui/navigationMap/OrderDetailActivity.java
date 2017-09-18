package com.dongdong.car.ui.navigationMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.ChangeOrderStateRequest;
import com.dongdong.car.entity.generateOrder.GenerateOrderRequest;
import com.dongdong.car.entity.orderRecordDetail.DetailByOrderInfo;
import com.dongdong.car.entity.orderRecordDetail.DetailByUserInfo;
import com.dongdong.car.entity.orderRecordDetail.OrderItemList;
import com.dongdong.car.entity.orderRecordDetail.OrderPictureList;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailRequest;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailResult;
import com.dongdong.car.util.CoverFlowViewPager;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.OnPageSelectListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/21.
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private String orderID, userId, staffLongitude, staffLatitude, staffAddress, staffMaxLax; // 可供查询订单详情的订单编号及服务人员经纬度等
    private ImageView orderDetailsBack; // 返回
    private TextView orderDetailsNumber; // 订单编号
    private TextView orderDetailsTime; // 订单时间
    private TextView orderDetailsAddress; // 订单地址
    private TextView orderDetailsName; // 客户姓名
    private TextView orderDetailsPhone; // 客户电话
    private TextView orderDetailsCarMsg; // 车辆信息
    private TextView orderDetailsItems; // 清洗项目
    private TextView orderDetailsTotal; // 清洗总价
    private TextView orderDetailsRemarks; // 备注
    private TextView aboutCarPictureNear;
    private CircleImageView orderDetailsPicture; // 车辆周围的照片集合
    private Button startOrderingBtn; // 开始接单
    private String LongitudeStr, LatitudeStr; // 接收订单的经纬度

    private List<OrderItemList> orderItemList = new ArrayList<>(); // 接收订单详情中的清洗项目集合
    private List<String> list1 = new ArrayList<>();
    private String orderItemString;
    private List<OrderPictureList> orderPictureList = new ArrayList<>(); // 接收服务器提供的照片信息
    private List<String> list2 = new ArrayList<>();
    private List<View> carRoundPicViewList = new ArrayList<>();
    private CoverFlowViewPager orderDetailsCover; // 照片轮播控件

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_DETAIL_HANDLER: // 获取订单详情数据并赋值显示
                    OrderRecordDetailRequest detailRequest = (OrderRecordDetailRequest) msg.obj;
                    hideDialogByProgress();
                    String detailState = detailRequest.getIsSucess();
                    if ("true".equals(detailState)) {
                        OrderRecordDetailResult detailResult = detailRequest.getData();
                        DetailByOrderInfo detailByOrderInfo = detailResult.getOrderInfo();
                        DetailByUserInfo detailByUserInfo = detailResult.getUserInfo();

                        orderDetailsNumber.setText(detailByOrderInfo.getOrderNumber()); // 订单编号
                        orderDetailsTime.setText(detailByOrderInfo.getOrderTime()); // 订单时间
                        orderDetailsAddress.setText(detailByOrderInfo.getAddress()); // 订单地址
                        orderDetailsName.setText(detailByUserInfo.getUserName()); // 客户姓名
                        orderDetailsPhone.setText(detailByUserInfo.getMobilePhone()); // 客户电话
                        orderDetailsCarMsg.setText(detailByUserInfo.getCarNumber() + " " + detailByUserInfo.getCarModel() + " " + detailByUserInfo.getCarColor()); // 车辆信息
                        orderDetailsTotal.setText(detailByOrderInfo.getOrderTotal() + " 元"); // 清洗总价
                        LongitudeStr = detailResult.getLongitude(); // 获取客户的经度
                        LatitudeStr = detailResult.getLatitude(); // 获取客户的纬度
                        orderItemList = detailByOrderInfo.getOrderItemList(); // 获取清洗项目
                        orderPictureList = detailByOrderInfo.getOrderPictureList(); // 获取照片集合

                        if (detailByUserInfo.getRemark() != null && !detailByUserInfo.getRemark().isEmpty()) { // 备注
                            orderDetailsRemarks.setText(detailByUserInfo.getRemark());
                        } else {
                            orderDetailsRemarks.setText("无");
                        }

                        if (orderItemList != null && !orderItemList.isEmpty()) { // 清洗项目
                            for (int i = 0; i < orderItemList.size(); i++) {
                                list1.add(orderItemList.get(i).getItemName());
                            }
                            for (int j = 0; j < list1.size(); j++) {
                                orderItemString += list1.get(j) + "、";
                            }
                            orderDetailsItems.setText(orderItemString.substring(4, orderItemString.length() - 1));
                        }

                        if (orderPictureList != null && !orderPictureList.isEmpty()) { // 照片数据
                            for (int i = 0; i < orderPictureList.size(); i++) {
                                list2.add(orderPictureList.get(i).getImgeFile());
                            }
                            aboutCarPictureNear.setVisibility(View.VISIBLE);
                            orderDetailsPicture.setVisibility(View.VISIBLE);
                            Glide.with(OrderDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(orderDetailsPicture);
                            orderDetailsPicture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    aboutCarPictureNear.setVisibility(View.GONE);
                                    orderDetailsPicture.setVisibility(View.GONE);
                                    orderDetailsCover.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < list2.size(); i++) { // 初始化数据
                                        ImageView img = new ImageView(OrderDetailActivity.this);
                                        Glide.with(OrderDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(i)).into(img);
                                        carRoundPicViewList.add(img);
                                    }
                                    orderDetailsCover.setViewList(carRoundPicViewList); // 设置显示的数据
                                }
                            });
                        } else {
                            aboutCarPictureNear.setVisibility(View.GONE);
                            orderDetailsPicture.setVisibility(View.GONE);
                        }
                    }
                    break;

                case GlobalConsts.GENERATE_NEAR_ORDER_HANDLER: // TODO 开始接单 重复的接单信息
                    GenerateOrderRequest generateOrderRequest = (GenerateOrderRequest) msg.obj;
                    hideDialogByProgress();
                    String generateOrderState = generateOrderRequest.getIsSucess();
                    if (generateOrderState.equals("true")) {
                        dialog = new DialogByOneButton(OrderDetailActivity.this, "提示", "已成功接单", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                                Intent intent = new Intent(OrderDetailActivity.this, NavigationActivity.class);
                                intent.putExtra("ORDER_ID", orderID);
                                intent.putExtra("LONGITUDE", LongitudeStr); // 客户的经度
                                intent.putExtra("LATITUDE", LatitudeStr); // 客户的纬度
                                intent.putExtra("STAFF_LONGITUDE", staffLongitude); // 服务人员的经度
                                intent.putExtra("STAFF_LATITUDE", staffLatitude); // 服务人员的纬度
                                startActivity(intent);
                                OrderDetailActivity.this.finish();
                            }
                        });
                    } else {
                        dialog = new DialogByOneButton(OrderDetailActivity.this, "提示", "该订单已被其他服务人员接单，请在订单列表中刷新并获取最新的订单", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                            }
                        });
                    }
                    break;

                case GlobalConsts.CREATE_ORDER_STATE_HANDLER: // 修改订单状态
                    ChangeOrderStateRequest changeOrderStateRequest = (ChangeOrderStateRequest) msg.obj;
                    String changeOrderStateString = changeOrderStateRequest.getMsg();
                    if (changeOrderStateString.equals("订单状态成功")) {
                        String changeOrderStateIng = changeOrderStateRequest.getData().getOrderSate();
                        if (changeOrderStateIng.equals("3")) { // 已接单

                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_layout);

        dialogByProgress = new DialogByProgress(OrderDetailActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SharedPreferences spf = OrderDetailActivity.this.getSharedPreferences("user_info", 0);
        userId = spf.getString("uid", "");

        Intent intent = getIntent();
        orderID = intent.getStringExtra("ORDER_ID");
        staffLongitude = intent.getStringExtra("STAFF_LONGITUDE");
        staffLatitude = intent.getStringExtra("STAFF_LATITUDE");
        staffAddress = intent.getStringExtra("STAFF_ADDRESS");
        staffMaxLax = intent.getStringExtra("MAX_LAX");

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        orderDetailsBack = (ImageView) findViewById(R.id.order_details_back); // 返回
        orderDetailsNumber = (TextView) findViewById(R.id.order_details_number); // 订单编号
        orderDetailsTime = (TextView) findViewById(R.id.order_details_time); // 订单时间
        orderDetailsAddress = (TextView) findViewById(R.id.order_details_address); // 订单地址
        orderDetailsName = (TextView) findViewById(R.id.order_details_name); // 客户姓名
        orderDetailsPhone = (TextView) findViewById(R.id.order_details_phone); // 客户电话
        orderDetailsCarMsg = (TextView) findViewById(R.id.order_details_car_msg); // 车辆信息
        orderDetailsItems = (TextView) findViewById(R.id.order_details_items); // 清洗项目
        orderDetailsTotal = (TextView) findViewById(R.id.order_details_total); // 清洗总价
        orderDetailsRemarks = (TextView) findViewById(R.id.order_details_remarks); // 备注
        aboutCarPictureNear = (TextView) findViewById(R.id.about_car_picture_near);
        orderDetailsPicture = (CircleImageView) findViewById(R.id.order_details_picture); // 车辆周围的照片集合
        orderDetailsCover = (CoverFlowViewPager) findViewById(R.id.order_details_cover); // 照片轮播控件
        startOrderingBtn = (Button) findViewById(R.id.start_ordering_btn); // 开始接单
    }

    /**
     * 获取数据并赋值
     */
    private void initData() {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_ORDER_DETAIL_URL + orderID).build();
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
        orderDetailsBack.setOnClickListener(this);
        orderDetailsPicture.setOnClickListener(this);
        startOrderingBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_details_back: // 返回
                OrderDetailActivity.this.finish();
                break;

            case R.id.start_ordering_btn: // 开始接单
                dialogByProgress.show();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("orderId", orderID);
                builder.add("staffId", userId);
                builder.add("longitude", staffLongitude);
                builder.add("latitude", staffLatitude);
                builder.add("address", staffAddress);
                builder.add("maxLax", staffMaxLax);
                final FormBody body = builder.build();
                Request request = new Request.Builder().url(GlobalConsts.GENERATE_NEAR_ORDER_URL).post(body).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String bodyString = response.body().string();
                        Gson generateOrderGson = new Gson();
                        GenerateOrderRequest generateOrderRequest = generateOrderGson.fromJson(bodyString, GenerateOrderRequest.class);
                        Message generateOrderMsg = Message.obtain();
                        generateOrderMsg.what = GlobalConsts.GENERATE_NEAR_ORDER_HANDLER;
                        generateOrderMsg.obj = generateOrderRequest;
                        handler.sendMessage(generateOrderMsg);
                    }
                });
                break;
        }
    }

    /**
     * TODO 实现照片轮播状态
     */
    private void initPicture() {
        Log.d("test", "照片数据-----------" + list2.toString());
        if (list2.isEmpty()) {
            orderDetailsPicture.setVisibility(View.GONE);
        } else {
            orderDetailsPicture.setVisibility(View.VISIBLE);
            Glide.with(OrderDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(orderDetailsPicture);
            orderDetailsPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list2.size(); i++) { // 初始化数据
                        ImageView img = new ImageView(OrderDetailActivity.this);
                        Glide.with(OrderDetailActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(i)).into(img);
                        carRoundPicViewList.add(img);
                    }
                    orderDetailsCover.setViewList(carRoundPicViewList); // 设置显示的数据
                }
            });
        }

        // 设置滑动的监听，该监听为当前页面滑动到中央时的索引
        orderDetailsCover.setOnPageSelectListener(new OnPageSelectListener() {
            @Override
            public void select(int position) {

            }
        });

        // 点击事件
        for (int i = 0; i < carRoundPicViewList.size(); i++) {
            final int finalI = i;
            carRoundPicViewList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
