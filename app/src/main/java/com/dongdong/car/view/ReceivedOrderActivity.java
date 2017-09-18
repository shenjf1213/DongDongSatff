package com.dongdong.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.ChangeOrderStateRequest;
import com.dongdong.car.entity.orderRecordDetail.DetailByOrderInfo;
import com.dongdong.car.entity.orderRecordDetail.DetailByUserInfo;
import com.dongdong.car.entity.orderRecordDetail.OrderItemList;
import com.dongdong.car.entity.orderRecordDetail.OrderPictureList;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailRequest;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailResult;
import com.dongdong.car.ui.navigationMap.NavigationActivity;
import com.dongdong.car.util.CoverFlowViewPager;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;
import com.meiyou.jet.annotation.JFindView;
import com.meiyou.jet.annotation.JFindViewOnClick;
import com.meiyou.jet.process.Jet;

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

public class ReceivedOrderActivity extends BaseActivity implements View.OnClickListener {

    private String orderId;
    @JFindView(R.id.received_order_number)
    private TextView receivedOrderNumber; // 订单编号
    @JFindView(R.id.received_order_time)
    private TextView receivedOrderTime; // 订单时间
    @JFindView(R.id.received_order_address)
    private TextView receivedOrderAddress; // 订单地址
    @JFindView(R.id.received_order_name)
    private TextView receivedOrderName; // 订单客户姓名
    @JFindView(R.id.received_order_phone)
    private TextView receivedOrderPhone; // 订单客户电话
    @JFindView(R.id.received_order_car_msg)
    private TextView receivedOrderCarMsg; // 订单车辆信息
    @JFindView(R.id.received_order_items)
    private TextView receivedOrderItems; // 清洗项目
    @JFindView(R.id.received_order_total)
    private TextView receivedOrderTotal; // 订单总价
    @JFindView(R.id.received_order_remarks)
    private TextView receivedOrderRemarks; // 备注
    @JFindView(R.id.received_order_tv)
    private TextView receivedOrderTv; // 显示车辆附近照片的文字
    @JFindView(R.id.received_order_picture)
    @JFindViewOnClick(R.id.received_order_picture)
    private CircleImageView receivedOrderPicture; // 车辆附近照片
    @JFindView(R.id.received_order_cover)
    @JFindViewOnClick(R.id.received_order_cover)
    private CoverFlowViewPager receivedOrderCover; // 照片轮播控件
    @JFindView(R.id.received_order_start_btn)
    @JFindViewOnClick(R.id.received_order_start_btn)
    private Button receivedOrderStartBtn; // 开始工作

    private String orderLongitude, orderLatitude, staffLongitude, staffLatitude;
    private List<OrderItemList> orderItemList = new ArrayList<>(); // 接收订单详情中的清洗项目集合
    private List<String> list1 = new ArrayList<>();
    private String orderItemString;
    private List<OrderPictureList> orderPictureList = new ArrayList<>(); // 接收服务器提供的照片信息
    private List<String> list2 = new ArrayList<>();
    private List<View> aboutCarViewList = new ArrayList<>();
    
    private DialogByProgress dialogByProgress;
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

                        orderLongitude = detailByOrderInfo.getLongitude();
                        orderLatitude = detailByOrderInfo.getLongitude();
                        receivedOrderNumber.setText(detailByOrderInfo.getOrderNumber());
                        receivedOrderTime.setText(detailByOrderInfo.getOrderTime());
                        receivedOrderAddress.setText(detailByOrderInfo.getAddress());
                        receivedOrderName.setText(detailByUserInfo.getUserName());
                        receivedOrderPhone.setText(detailByUserInfo.getMobilePhone());
                        receivedOrderCarMsg.setText(detailByUserInfo.getCarNumber() + " " + detailByUserInfo.getCarModel() + " " + detailByUserInfo.getCarColor());
                        receivedOrderTotal.setText(detailByOrderInfo.getOrderTotal() + " 元");
                        orderItemList = detailByOrderInfo.getOrderItemList();
                        orderPictureList = detailByOrderInfo.getOrderPictureList();

                        if (detailByUserInfo.getRemark() != null && !TextUtils.isEmpty(detailByUserInfo.getRemark())) { // 备注
                            receivedOrderRemarks.setText(detailByUserInfo.getRemark());
                        } else {
                            receivedOrderRemarks.setText("无");
                        }

                        if (orderItemList != null && !orderItemList.isEmpty()) { // 清洗项目
                            for (OrderItemList orderItemLists : orderItemList) {
                                list1.add(orderItemLists.getItemName());
                            }
                            for (String listString : list1) {
                                orderItemString += listString + "、";
                            }
                            receivedOrderItems.setText(orderItemString.substring(4, orderItemString.length() - 1));
                        }

                        if (orderPictureList != null && !orderPictureList.isEmpty()) { // 照片数据
                            for (OrderPictureList orderPictureLists : orderPictureList) {
                                list2.add(orderPictureLists.getImgeFile());
                            }
                            receivedOrderTv.setVisibility(View.VISIBLE);
                            receivedOrderPicture.setVisibility(View.VISIBLE);
                            Glide.with(ReceivedOrderActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(receivedOrderPicture);
                            receivedOrderPicture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    receivedOrderTv.setVisibility(View.GONE);
                                    receivedOrderPicture.setVisibility(View.GONE);
                                    receivedOrderCover.setVisibility(View.VISIBLE);
                                    for (String image : list2) { // 初始化数据
                                        ImageView imageView = new ImageView(ReceivedOrderActivity.this);
                                        Glide.with(ReceivedOrderActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + image).into(imageView);
                                        aboutCarViewList.add(imageView);
                                    }
                                    receivedOrderCover.setViewList(aboutCarViewList); // 设置显示的数据
                                }
                            });
                        } else {
                            receivedOrderTv.setVisibility(View.GONE);
                            receivedOrderPicture.setVisibility(View.GONE);
                        }
                    }
                    break;

                case GlobalConsts.CREATE_ORDER_STATE_HANDLER:
                    ChangeOrderStateRequest changeOrderStateRequest = (ChangeOrderStateRequest) msg.obj;
                    String changeOrderStateString = changeOrderStateRequest.getMsg();
                    if (changeOrderStateString.equals("订单状态成功")) {
                        String changeOrderStateIng = changeOrderStateRequest.getData().getOrderSate();
                        if (changeOrderStateIng.equals("4")) { // 已接单
                            Intent intent = new Intent(ReceivedOrderActivity.this, NavigationActivity.class);
                            intent.putExtra("ORDER_ID", orderId);
                            intent.putExtra("LONGITUDE", orderLongitude); // 客户的经度
                            intent.putExtra("LATITUDE", orderLatitude); // 客户的纬度
                            intent.putExtra("STAFF_LONGITUDE", staffLongitude); // 服务人员的经度
                            intent.putExtra("STAFF_LATITUDE", staffLatitude); // 服务人员的纬度
                            startActivity(intent);
                            ReceivedOrderActivity.this.finish();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_order_layout);
        Jet.bind(this);

        dialogByProgress = new DialogByProgress(ReceivedOrderActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("ORDER_ID");
        staffLongitude = intent.getStringExtra("STAFF_LONGITUDE");
        staffLatitude = intent.getStringExtra("STAFF_LATITUDE");

        if (!TextUtils.isEmpty(orderId)) {
            getDataByOrderId(orderId);
        }
    }

    /**
     * 根据订单id获取订单数据并显示赋值
     */
    private void getDataByOrderId(String orderId) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_ORDER_DETAIL_URL + orderId).build();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.received_order_start_btn: // 开始工作
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("orderState", "4");
                builder.add("orderID", orderId);
                final FormBody body = builder.build();
                Request request = new Request.Builder().url(GlobalConsts.CREATE_ORDER_STATE_URL).post(body).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String bodyString = response.body().string();
                        Gson changeOrderStateGson = new Gson();
                        ChangeOrderStateRequest changeOrderStateRequest = changeOrderStateGson.fromJson(bodyString, ChangeOrderStateRequest.class);
                        Message changeOrderStateMsg = Message.obtain();
                        changeOrderStateMsg.what = GlobalConsts.CREATE_ORDER_STATE_HANDLER;
                        changeOrderStateMsg.obj = changeOrderStateRequest;
                        handler.sendMessage(changeOrderStateMsg);
                    }
                });
                break;
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
