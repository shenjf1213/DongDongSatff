package com.dongdong.car;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.CancelOrderRequest;
import com.dongdong.car.entity.GetOrderMsgRequest;
import com.dongdong.car.entity.orderRecordDetail.DetailByOrderInfo;
import com.dongdong.car.entity.orderRecordDetail.DetailByUserInfo;
import com.dongdong.car.entity.orderRecordDetail.OrderItemList;
import com.dongdong.car.entity.orderRecordDetail.OrderPictureList;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailRequest;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailResult;
import com.dongdong.car.ui.driverCenter.DriverCenterActivity;
import com.dongdong.car.ui.navigationMap.NavigationActivity;
import com.dongdong.car.ui.nearOrder.NearOrderActivity;
import com.dongdong.car.ui.orderRecord.OrderRecordActivity;
import com.dongdong.car.util.DataConversionByShen;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.Downloader;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.ScreenUtils;
import com.dongdong.car.view.ExamineOrderActivity;
import com.dongdong.car.view.ReceivedOrderActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements AMapLocationListener, LocationSource, View.OnClickListener {

    private long mExitTime;
    private MapView carMapView;
    private AMap carMap;
    private AMapLocationClient carLocationClient;
    private AMapLocationClientOption carLocationOption; // 定位参数
    private OnLocationChangedListener carListener; // 定位监听器
    private StringBuffer addressBuffer = new StringBuffer();
    private double Latitude; // 获取纬度
    private double Longitude; // 获取经度

    private OrderAcceptBroadCastReceiver orderAcceptBroadCastReceiver;
    private LinearLayout driverGradLl; // 抢单以及附近订单的布局
    private RelativeLayout driverAddressRl; // 主页的地址信息
    private TextView driverCancelOrder; // 取消订单
    private ImageView driverCenterIv, driverMessageIv; // 员工中心及消息通知
    private TextView driverAddressTv; // 地址
    private Button driverGradSingle, driverNearbySingle; // 抢单及附近订单
    private View GradSingleView;
    private PopupWindow GradSinglePW; // 抢到弹窗
    private View CancelOrderView;
    private PopupWindow CancelOrderPW; // 取消订单弹窗
    private View receivedOrderView;
    private PopupWindow ReceivedOrderPW; // 被管理端派单后显示的订单弹窗
    private Button receivedOrderExamineBtn;
    private View onProgressOrderView;
    private PopupWindow onProgressOrderPW;
    private Button onProgressOrderBtn;

    private List<String> cancelTypeList = new ArrayList<>();
    private String cancelTypeString = ""; // 点击类型的字符串
    private CheckBox cancelCB1, cancelCB2, cancelCB3, cancelCB4;
    private EditText cancelEt;

    private String versionName, newVersionName, apkUrl; // 新旧版本号以及下载的apk的url
    private Downloader downloadAPK; // 版本下载器

    private String id, ordersId, gradSingleAddress, gradSingleDistance, gradSingleNumber, gradSingleTime, gradSingleName, gradSinglePhone, gradSingleCarMsg, gradSingleTotal, gradSingleRemarks, LongitudeString, LatitudeString, staffLongitude, staffLatitude;
    private String orderId, orderState;
    private List<OrderItemList> orderItemList = new ArrayList<>(); // 接收清洗项目的数据集合
    private List<String> list = new ArrayList<>();
    private String gradSingleItemString;
    private List<OrderPictureList> ExamineOrderPictureList = new ArrayList<>(); // 接收服务器提供的照片信息
    private List<String> list2 = new ArrayList<>();

    private DialogByOneButton dialog;
    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GRAB_ORDERS_HANDLER: // 抢单成功后页面跳转
                    OrderRecordDetailRequest examineOrderRequest = (OrderRecordDetailRequest) msg.obj;
                    hideDialogByProgress();
                    String examineOrderString = examineOrderRequest.getIsSucess();
                    if (examineOrderString.equals("true")) {
                        OrderRecordDetailResult examineOrderResult = examineOrderRequest.getData();
                        DetailByOrderInfo examineOrderInfo = examineOrderResult.getOrderInfo();
                        DetailByUserInfo examineUserInfo = examineOrderResult.getUserInfo();

                        ordersId = examineOrderInfo.getId(); // 获取订单id
                        staffLongitude = examineOrderResult.getLongitude(); // 获取服务人员的经度
                        staffLatitude = examineOrderResult.getLatitude(); // 获取服务人员的纬度
                        gradSingleNumber = examineOrderInfo.getOrderNumber();
                        gradSingleTime = examineOrderInfo.getOrderTime();
                        gradSingleName = examineUserInfo.getUserName();
                        gradSinglePhone = examineUserInfo.getMobilePhone();
                        gradSingleCarMsg = examineUserInfo.getCarNumber() + " " + examineUserInfo.getCarModel() + " " + examineUserInfo.getCarColor();
                        gradSingleTotal = examineOrderInfo.getOrderTotal() + "元";
                        gradSingleRemarks = examineUserInfo.getRemark();
                        LongitudeString = examineOrderResult.getLongitude(); // 订单的经度
                        LatitudeString = examineOrderResult.getLatitude(); // 订单的纬度
                        gradSingleAddress = examineOrderResult.getAddress();
                        gradSingleDistance = examineOrderResult.getMaxLat();
                        orderItemList = examineOrderInfo.getOrderItemList();
                        ExamineOrderPictureList = examineOrderInfo.getOrderPictureList();

                        if (orderItemList != null && !orderItemList.isEmpty()) {
                            for (int i = 0; i < orderItemList.size(); i++) {
                                list.add(orderItemList.get(i).getItemName());
                            }
                            String gradSingleItem = String.valueOf(list.subList(0, list.size()));
                            gradSingleItemString = gradSingleItem.substring(1, gradSingleItem.length() - 1);
                        }

                        if (ExamineOrderPictureList != null && !ExamineOrderPictureList.isEmpty()) {
                            for (int i = 0; i < ExamineOrderPictureList.size(); i++) {
                                list2.add(ExamineOrderPictureList.get(i).getImgeFile());
                            }
                        }

                        Intent gradSingleIntent = new Intent(MainActivity.this, ExamineOrderActivity.class);
                        gradSingleIntent.putExtra("ID", ordersId);
                        gradSingleIntent.putExtra("Number", gradSingleNumber);
                        gradSingleIntent.putExtra("Time", gradSingleTime);
                        gradSingleIntent.putExtra("Address", gradSingleAddress);
                        gradSingleIntent.putExtra("Name", gradSingleName);
                        gradSingleIntent.putExtra("Phone", gradSinglePhone);
                        gradSingleIntent.putExtra("CarMsg", gradSingleCarMsg);
                        gradSingleIntent.putExtra("Total", gradSingleTotal);
                        gradSingleIntent.putExtra("Remarks", gradSingleRemarks);
                        gradSingleIntent.putExtra("LONGITUDE", LongitudeString);
                        gradSingleIntent.putExtra("LATITUDE", LatitudeString);
                        gradSingleIntent.putExtra("STAFF_LONGITUDE", staffLongitude);
                        gradSingleIntent.putExtra("STAFF_LATITUDE", staffLatitude);
                        gradSingleIntent.putExtra("Items", gradSingleItemString);
                        gradSingleIntent.putStringArrayListExtra("aboutCarList", (ArrayList<String>) list2);
                        startActivity(gradSingleIntent);

                        // ShowGradSinglePW(gradSingleAddress, gradSingleDistance, gradSingleItemString);
                    } else {
                        dialog = new DialogByOneButton(MainActivity.this, "提示", "抱歉，您未抢到订单，继续加油吧！", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                            }
                        });
                    }
                    break;

                case GlobalConsts.GET_ORDER_MSG_BY_ID_HANDLER: // 根据服务人员id获取当时是否接单及订单信息
                    GetOrderMsgRequest getOrderMsgRequest = (GetOrderMsgRequest) msg.obj;
                    String getOrderMsgString = getOrderMsgRequest.getIsSucess();
                    if (getOrderMsgString.equals("true")) {
                        GetOrderMsgRequest.GetOrderMsgResult getOrderMsgResult = getOrderMsgRequest.getData();
                        orderId = getOrderMsgResult.getId();
                        orderState = getOrderMsgResult.getOrderState();
                        if (orderState.equals("1")) {
                            hideCancelOrder();
                        } else if (orderState.equals("2")) {
                            hideCancelOrder();
                        } else if (orderState.equals("3")) {
                            showCancelOrder();
                        } else if (orderState.equals("4")) {
                            showOnProgressOrder();
                        } else if (orderState.equals("5")) {
                            hideCancelOrder();
                        } else if (orderState.equals("6")) {
                            hideCancelOrder();
                        }
                    } else { // 服务人员还没有接单
                        hideCancelOrder();
                    }
                    break;

                case GlobalConsts.CANCEL_ORDER_HANDLER: // 取消订单
                    CancelOrderRequest cancelOrderRequest = (CancelOrderRequest) msg.obj;
                    hideDialogByProgress();
                    String cancelOrderState = cancelOrderRequest.getIsSucess();
                    if (cancelOrderState.equals("true")) {
                        if (CancelOrderPW != null && CancelOrderPW.isShowing()) {
                            CancelOrderPW.dismiss();
                            dialog = new DialogByOneButton(MainActivity.this, "提示", "订单取消成功！", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    hideCancelOrder();
                                }
                            });
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogByProgress = new DialogByProgress(MainActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SharedPreferences spf = MainActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");
        if (!id.equals("")) {
            getOrderMsgById(id); // 根据员工id获取当前是否接单
        }

        versionName = DataConversionByShen.getVersionName(MainActivity.this, versionName); // 获取本app当前的版本号

        initView();
        carMapView = (MapView) findViewById(R.id.car_map_view);
        carMapView.onCreate(savedInstanceState);
        if (carMap == null) {
            carMap = carMapView.getMap();
        }
        carMap.setLocationSource(this); // 设置定位监听
        carMap.setMyLocationEnabled(true);
        carMap.getUiSettings().setMyLocationButtonEnabled(true);
        carMap.getUiSettings().setZoomControlsEnabled(false);
        carMap.getUiSettings().setRotateGesturesEnabled(false); // 取消地图手势旋转
        carMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                carMap.moveCamera(CameraUpdateFactory.zoomTo(14));  // 设置缩放级别
            }
        });

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE); // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.interval(60000); // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        carMap.setMyLocationStyle(myLocationStyle);

        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        driverCenterIv = (ImageView) findViewById(R.id.driver_center_iv); // 员工中心
        driverMessageIv = (ImageView) findViewById(R.id.driver_message_iv); // 消息通知
        driverAddressTv = (TextView) findViewById(R.id.driver_address_tv); // 地址
        driverGradSingle = (Button) findViewById(R.id.driver_grad_single); // 抢单
        driverNearbySingle = (Button) findViewById(R.id.driver_nearby_single); // 附近订单
        driverGradLl = (LinearLayout) findViewById(R.id.driver_grad_ll); // 抢单以及附近订单的布局
        driverAddressRl = (RelativeLayout) findViewById(R.id.driver_address_rl);
        driverCancelOrder = (TextView) findViewById(R.id.driver_cancel_order); // 取消订单
    }

    /**
     * 监听
     */
    private void initListener() {
        driverCenterIv.setOnClickListener(this);
        driverMessageIv.setOnClickListener(this);
        driverGradSingle.setOnClickListener(this);
        driverNearbySingle.setOnClickListener(this);
        driverCancelOrder.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.driver_center_iv: // 个人中心
                Intent intent = new Intent(MainActivity.this, DriverCenterActivity.class);
                intent.putExtra("LATITUDE", String.valueOf(Latitude)); // 经度
                intent.putExtra("LONGITUDE", String.valueOf(Longitude)); // 纬度
                startActivity(intent);
                break;

            case R.id.driver_message_iv: // 消息中心
                // startActivity(new Intent(MainActivity.this, TakePicturesByAfterActivity.class));
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                break;

            case R.id.driver_grad_single: // 抢单模式
                getGradSingleData(); // 获取抢单数据
                break;

            case R.id.grad_single_cancel_btn: // 抢单模式的取消
                if (GradSinglePW != null && GradSinglePW.isShowing()) {
                    GradSinglePW.dismiss();
                }
                break;

            case R.id.grad_single_examine_btn: // 抢单模式的查看订单
                if (GradSinglePW != null && GradSinglePW.isShowing()) {
                    GradSinglePW.dismiss();
                }
                break;

            case R.id.driver_nearby_single: // 附近订单
                Intent nearOrderIntent = new Intent(MainActivity.this, NearOrderActivity.class);
                nearOrderIntent.putExtra("STAFF_LONGITUDE", String.valueOf(Longitude)); // 服务人员的经度
                nearOrderIntent.putExtra("STAFF_LATITUDE", String.valueOf(Latitude)); // 服务人员的纬度
                nearOrderIntent.putExtra("STAFF_ADDRESS", String.valueOf(addressBuffer)); // 服务人员的地址
                startActivity(nearOrderIntent);
                break;

            case R.id.driver_cancel_order: // 取消订单
                ShowCancelSinglePW(); // 取消订单的弹窗
                break;

            case R.id.cancel_cb_1:
                cancelTypeString = "1";
                cancelCB1.setChecked(true);
                cancelCB2.setChecked(false);
                cancelCB3.setChecked(false);
                cancelCB4.setChecked(false);
                break;

            case R.id.cancel_cb_2:
                cancelTypeString = "2";
                cancelCB1.setChecked(false);
                cancelCB2.setChecked(true);
                cancelCB3.setChecked(false);
                cancelCB4.setChecked(false);
                break;

            case R.id.cancel_cb_3:
                cancelTypeString = "3";
                cancelCB1.setChecked(false);
                cancelCB2.setChecked(false);
                cancelCB3.setChecked(true);
                cancelCB4.setChecked(false);
                break;

            case R.id.cancel_cb_4:
                cancelTypeString = "4";
                cancelCB1.setChecked(false);
                cancelCB2.setChecked(false);
                cancelCB3.setChecked(false);
                cancelCB4.setChecked(true);
                break;

            case R.id.cancel_order_btn_1: // 取消订单的取消按钮
                if (CancelOrderPW != null && CancelOrderPW.isShowing()) {
                    CancelOrderPW.dismiss();
                }
                break;

            case R.id.cancel_order_btn_2: // 取消订单的确定按钮
                if (TextUtils.isEmpty(cancelTypeString)) {
                    Toast.makeText(MainActivity.this, "请至少选择一项取消原因", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("cancelType", cancelTypeString);
                    builder.add("cancelDes", cancelEt.getText().toString().trim());
                    builder.add("staffId", id);
                    builder.add("orderId", orderId);
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.CANCEL_ORDER_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Gson cancelGson = new Gson();
                            CancelOrderRequest cancelOrderRequest = cancelGson.fromJson(bodyString, CancelOrderRequest.class);
                            Message cancelOrderMsg = Message.obtain();
                            cancelOrderMsg.what = GlobalConsts.CANCEL_ORDER_HANDLER;
                            cancelOrderMsg.obj = cancelOrderRequest;
                            handler.sendMessage(cancelOrderMsg);
                        }
                    });
                }
                // saveCancelType();
                break;

            case R.id.received_order_examine_btn: // 查看管理端派单后订单详情
                Intent receivedOrderIntent = new Intent(MainActivity.this, ReceivedOrderActivity.class);
                receivedOrderIntent.putExtra("ORDER_ID", orderId);
                receivedOrderIntent.putExtra("STAFF_LONGITUDE", String.valueOf(Latitude));
                receivedOrderIntent.putExtra("STAFF_LATITUDE", String.valueOf(Latitude));
                startActivity(receivedOrderIntent);
                ReceivedOrderPW.dismiss();
                break;

            case R.id.on_progress_order_btn: // 继续当前正在进行中的订单
                startActivity(new Intent(MainActivity.this, OrderRecordActivity.class));
                onProgressOrderPW.dismiss();
                break;
        }
    }

    /**
     * 通过接口调取抢单数据
     */
    private void getGradSingleData() {
        dialogByProgress.show();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("staffId", id);
        builder.add("longitude", String.valueOf(Longitude));
        builder.add("latitude", String.valueOf(Latitude));
        builder.add("address", String.valueOf(addressBuffer));
        final FormBody body = builder.build();
        Request request = new Request.Builder().url(GlobalConsts.GRAB_ORDERS_URL).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson examineOrderGson = new Gson();
                Message examineOrderMsg = Message.obtain();
                OrderRecordDetailRequest examineOrderRequest = examineOrderGson.fromJson(bodyString, OrderRecordDetailRequest.class);
                examineOrderMsg.what = GlobalConsts.GRAB_ORDERS_HANDLER;
                examineOrderMsg.obj = examineOrderRequest;
                handler.sendMessage(examineOrderMsg);
            }
        });
    }

    /**
     * 显示抢单模式的弹窗
     */
    private void ShowGradSinglePW(String gradSingleAddress, String gradSingleDistance, String gradSingleItemString) {
        GradSingleView = View.inflate(MainActivity.this, R.layout.grad_single_layout, null);
        int width = ScreenUtils.getScreenWidth(MainActivity.this) / 10 * 8;
        GradSinglePW = new PopupWindow(GradSingleView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        backgroundAlpha(0.8f);

        TextView gradSingleOrderAddress = (TextView) GradSingleView.findViewById(R.id.grad_single_order_address); // 订单位置
        TextView gradSingleOrderDistance = (TextView) GradSingleView.findViewById(R.id.grad_single_order_distance); // 订单距离
        TextView gradSingleOrderItems = (TextView) GradSingleView.findViewById(R.id.grad_single_order_items); // 清洗项目
        Button gradSingleCancelBtn = (Button) GradSingleView.findViewById(R.id.grad_single_cancel_btn); // 取消订单
        Button gradSingleExamineBtn = (Button) GradSingleView.findViewById(R.id.grad_single_examine_btn); // 查看订单

        gradSingleOrderAddress.setText(gradSingleAddress);
        gradSingleOrderDistance.setText(gradSingleDistance);
        gradSingleOrderItems.setText(gradSingleItemString);

        gradSingleCancelBtn.setOnClickListener(this);
        gradSingleExamineBtn.setOnClickListener(this);

        GradSinglePW.setFocusable(true);
        GradSinglePW.setOutsideTouchable(true);
        GradSinglePW.setBackgroundDrawable(new ColorDrawable(0x000000));
        GradSinglePW.setAnimationStyle(android.R.style.Animation_InputMethod);
        GradSinglePW.showAtLocation(GradSingleView, Gravity.CENTER, 0, 0);
        GradSinglePW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 显示取消订单的弹窗
     */
    private void ShowCancelSinglePW() {
        if (ReceivedOrderPW != null && ReceivedOrderPW.isShowing()) {
            ReceivedOrderPW.dismiss();
        }
        CancelOrderView = View.inflate(MainActivity.this, R.layout.cancel_order_layout, null);
        int width = ScreenUtils.getScreenWidth(MainActivity.this) / 10 * 8;
        CancelOrderPW = new PopupWindow(CancelOrderView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundAlpha(0.8f);

        cancelCB1 = (CheckBox) CancelOrderView.findViewById(R.id.cancel_cb_1);
        cancelCB2 = (CheckBox) CancelOrderView.findViewById(R.id.cancel_cb_2);
        cancelCB3 = (CheckBox) CancelOrderView.findViewById(R.id.cancel_cb_3);
        cancelCB4 = (CheckBox) CancelOrderView.findViewById(R.id.cancel_cb_4);
        cancelEt = (EditText) CancelOrderView.findViewById(R.id.cancel_et);
        Button cancelOrderBtn1 = (Button) CancelOrderView.findViewById(R.id.cancel_order_btn_1); // 取消
        Button cancelOrderBtn2 = (Button) CancelOrderView.findViewById(R.id.cancel_order_btn_2); // 确定

        cancelCB1.setOnClickListener(this);
        cancelCB2.setOnClickListener(this);
        cancelCB3.setOnClickListener(this);
        cancelCB4.setOnClickListener(this);
        cancelOrderBtn1.setOnClickListener(this);
        cancelOrderBtn2.setOnClickListener(this);

        CancelOrderPW.setFocusable(true);
        CancelOrderPW.setOutsideTouchable(true);
        CancelOrderPW.setBackgroundDrawable(new ColorDrawable(0x000000));
        CancelOrderPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        CancelOrderPW.showAtLocation(CancelOrderView, Gravity.CENTER, 0, 0);
        CancelOrderPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                cancelTypeList.clear();
                cancelTypeString = "";
            }
        });
    }

    /**
     *  设置添加屏幕的背景透明度 
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MainActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0  
        MainActivity.this.getWindow().setAttributes(lp);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (carMapView != null) {
            carMapView.onSaveInstanceState(outState);
        }
    }

    /**
     * 定位回调函数
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                Longitude = aMapLocation.getLongitude();//获取经度
                Latitude = aMapLocation.getLatitude();//获取纬度
                Log.d("test", "定位后的经纬度---" + Longitude + "---" + Latitude);
                carMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                carListener.onLocationChanged(aMapLocation); // 点击定位按钮 能够将地图的中心移动到定位点
                addressBuffer = new StringBuffer();
                addressBuffer.append(aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                driverAddressTv.setText(addressBuffer);
                // Log.d("test", "定位后的地址---" + addressBuffer.toString());
            } else {
                Log.e("test", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 激活定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        carListener = onLocationChangedListener;
        if (carLocationClient == null) {
            carLocationClient = new AMapLocationClient(this.getApplicationContext()); // 初始化AMapLocationClient，并绑定监听
            carLocationClient.setLocationListener(this);
            carLocationOption = new AMapLocationClientOption(); // 初始化定位参数
            carLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置定位精度
            carLocationOption.setMockEnable(true);  // 是否允许模拟位置
            carLocationOption.setNeedAddress(true);  // 是否返回地址信息
            carLocationOption.setOnceLocation(false); // 是否只定位一次
            carLocationOption.setWifiActiveScan(true); // 设置是否强制刷新WIFI，默认为强制刷新
            carLocationOption.setInterval(60000); // 定位时间间隔
            carLocationClient.setLocationOption(carLocationOption); // 给定位客户端对象设置定位参数
            carLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        if (carLocationClient != null) {
            carLocationClient.stopLocation();
            carLocationClient.onDestroy();
            carLocationClient = null;
            carListener = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        orderAcceptBroadCastReceiver = new OrderAcceptBroadCastReceiver();
        IntentFilter orderFilter = new IntentFilter();
        orderFilter.addAction("ORDER_TO_BE");
        orderFilter.addAction("ORDER_ACCEPT");
        orderFilter.addAction("ORDER_PROCESSING");
        orderFilter.addAction("ORDER_COMPLETED");
        MainActivity.this.registerReceiver(orderAcceptBroadCastReceiver, orderFilter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getOrderMsgById(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        carMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carMapView.onDestroy();
        if (null != carLocationClient) {
            carLocationClient.stopLocation();
            carLocationClient.onDestroy();
            carLocationClient = null;
        }
        MainActivity.this.unregisterReceiver(orderAcceptBroadCastReceiver);
    }

    /**
     * 保存取消的原因信息并排序
     * TODO 对集合中重复的数据进行筛选
     */
    private void saveCancelType() {
        for (int i = 0; i < cancelTypeList.size(); i++) {
            for (int j = cancelTypeList.size() - 1; j > i; j--) {
                if (cancelTypeList.get(i).equals(cancelTypeList.get(j))) {
                    cancelTypeList.remove(i);
                }
            }
        }
        Collections.sort(cancelTypeList, new MapComparator());
        for (int k = 0; k < cancelTypeList.size(); k++) {
            cancelTypeString += cancelTypeList.get(k) + ",";
        }
        String cancelTypeStr = String.valueOf(cancelTypeString.charAt(0));
        if (cancelTypeStr.equals("n")) {
            cancelTypeString = cancelTypeString.substring(4, cancelTypeString.length() - 1);
        } else {
            cancelTypeString = cancelTypeString.substring(0, cancelTypeString.length() - 1);
        }
    }

    /**
     * 根据员工id获取当前是否接单
     */
    private void getOrderMsgById(String id) {
        Request request = new Request.Builder().url(GlobalConsts.GET_ORDER_MSG_BY_ID_URL + id).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "当前员工是否接单以及订单状态----------------------------" + bodyString.toString());
                Gson getOrderMsgGson = new Gson();
                GetOrderMsgRequest getAutoOrdersRequest = getOrderMsgGson.fromJson(bodyString, GetOrderMsgRequest.class);
                Message getOrderMsg = Message.obtain();
                getOrderMsg.what = GlobalConsts.GET_ORDER_MSG_BY_ID_HANDLER;
                getOrderMsg.obj = getAutoOrdersRequest;
                handler.sendMessage(getOrderMsg);
            }
        });
    }

    private void showCancelOrder() {
        driverGradLl.setVisibility(View.GONE); // 抢单以及附近订单的布局
        driverCancelOrder.setVisibility(View.VISIBLE); // 取消订单
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ReceivedOrderPW != null && ReceivedOrderPW.isShowing()) {
                    ReceivedOrderPW.dismiss();
                    ReceivedOrderPW = null;
                }
                showReceivedOrderView();
            }
        }, 2000);
    }

    /**
     * 当管理端派单后提示服务端去接单并显示
     */
    private void showReceivedOrderView() {
        receivedOrderView = View.inflate(MainActivity.this, R.layout.received_order_layout, null);
        int width = ScreenUtils.getScreenWidth(MainActivity.this) / 10 * 8;
        ReceivedOrderPW = new PopupWindow(receivedOrderView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        backgroundAlpha(0.8f);

        receivedOrderExamineBtn = (Button) receivedOrderView.findViewById(R.id.received_order_examine_btn); // 查看订单
        receivedOrderExamineBtn.setOnClickListener(this);

        ReceivedOrderPW.setFocusable(true);
        ReceivedOrderPW.setOutsideTouchable(true);
        ReceivedOrderPW.setBackgroundDrawable(new ColorDrawable(0x000000));
        ReceivedOrderPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        ReceivedOrderPW.showAtLocation(receivedOrderView, Gravity.CENTER, 0, 0);
        ReceivedOrderPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void showOnProgressOrder() {
        driverGradLl.setVisibility(View.GONE); // 抢单以及附近订单的布局
        driverCancelOrder.setVisibility(View.GONE); // 取消订单
        driverAddressRl.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ReceivedOrderPW != null && ReceivedOrderPW.isShowing()) {
                    ReceivedOrderPW.dismiss();
                    ReceivedOrderPW = null;
                }
                showOnProgressOrderView();
            }
        }, 2000);
    }

    /**
     * 订单未完成时提醒司机去继续操作
     */
    private void showOnProgressOrderView() {
        onProgressOrderView = View.inflate(MainActivity.this, R.layout.on_progress_order_layout, null);
        int width = ScreenUtils.getScreenWidth(MainActivity.this) / 10 * 8;
        onProgressOrderPW = new PopupWindow(onProgressOrderView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        backgroundAlpha(0.8f);

        onProgressOrderBtn = (Button) onProgressOrderView.findViewById(R.id.on_progress_order_btn); // 查看订单
        onProgressOrderBtn.setOnClickListener(this);

        onProgressOrderPW.setFocusable(true);
        onProgressOrderPW.setOutsideTouchable(true);
        onProgressOrderPW.setBackgroundDrawable(new ColorDrawable(0x000000));
        onProgressOrderPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        onProgressOrderPW.showAtLocation(onProgressOrderView, Gravity.CENTER, 0, 0);
        onProgressOrderPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void hideCancelOrder() {
        driverAddressRl.setVisibility(View.VISIBLE);
        driverGradLl.setVisibility(View.VISIBLE); // 抢单以及附近订单的布局
        driverCancelOrder.setVisibility(View.GONE); // 取消订单
    }

    /**
     * 集合数字排序
     */
    private class MapComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }

    private class OrderAcceptBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "ORDER_TO_BE": // 下订单后待接单
                    ordersId = intent.getStringExtra("ORDER_ID_TO_BE");
                    orderState = intent.getStringExtra("ORDER_STATE_TO_BE");
                    hideCancelOrder();
                    break;

                case "ORDER_ACCEPT": // 已接收订单
                    ordersId = intent.getStringExtra("ORDER_ID_ACCEPT");
                    orderState = intent.getStringExtra("ORDER_STATE_ACCEPT");
                    showCancelOrder();
                    break;

                case "ORDER_PROCESSING": // 订单进行中
                    ordersId = intent.getStringExtra("ORDER_ID_PROCESSING");
                    orderState = intent.getStringExtra("ORDER_STATE_PROCESSING");
                    showOnProgressOrder();
                    break;

                case "ORDER_COMPLETED": // 订单已完成
                    ordersId = intent.getStringExtra("ORDER_ID_COMPLETED");
                    orderState = intent.getStringExtra("ORDER_STATE_COMPLETED");
                    hideCancelOrder();
                    break;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出动动美车应用", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MainActivity.this.finish();
            System.exit(0);
        }
    }
}
