package com.dongdong.car.ui.nearOrder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.dongdong.car.R;
import com.dongdong.car.adapter.NearOrderAdapter;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.nearOrder.NearOrderList;
import com.dongdong.car.entity.nearOrder.NearOrderRequest;
import com.dongdong.car.entity.nearOrder.NearOrderResult;
import com.dongdong.car.ui.navigationMap.OrderDetailActivity;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.view.CustomFooterView;
import com.dongdong.car.view.ShowNearOrderPW;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/4.
 */

public class NearOrderActivity extends BaseActivity implements View.OnClickListener {

    private String staffLongitude, staffLatitude, staffAddress; // 接收司机端的经纬度
    private String ordering = ""; // 0为距离的升序
    private String orderType = ""; // 订单类型 (0为上门洗车，1为预约洗车)
    private int pageIndex = 1;
    private String tag = "";
    private ImageView nearOrdersBack; // 返回
    private LinearLayout nearOrdersLL; // 父布局
    private TextView nearOrdersTv1; // 默认
    private TextView nearOrdersTv2; // 距离
    private TextView nearOrdersTv3; // 复选框停靠的控件线
    private TextView nearOrdersStyleTv; // 订单类型
    private LinearLayout nearOrdersLl3;
    private ShowNearOrderPW showNearOrderPW;
    private ImageView nearOrdersStyleIv; // 订单类型的三角图片
    private RotateAnimation LeftRotate, RightRotate; // 三角的动画旋转

    private LinearLayout noDataInfoLl; //没有数据时显示的图
    private RecyclerView nearOrdersRV; // 数据显示的布局
    private List<NearOrderList> nearOrderDataList = new ArrayList<>();
    private XRefreshView nearOrderXV; // 加载刷新控件
    private NearOrderAdapter nearOrderAdapter;
    private int upOrDown; // 判断动作
    public static long lastRefreshTime; // 下拉刷新结束的时间

    private NearOrderBroadcastReceiver nearOrderReceiver = new NearOrderBroadcastReceiver(); // 广播

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_NEAR_ORDER_LIST_HANDLER:
                    NearOrderRequest nearOrderRequest = (NearOrderRequest) msg.obj;
                    hideDialogByProgress();
                    String nearOrderStr = nearOrderRequest.getMsg();
                    if (TextUtils.equals("附近订单", nearOrderStr)) {
                        NearOrderResult nearOrderResult = nearOrderRequest.getData();
                        noDataInfoLl.setVisibility(View.GONE);
                        nearOrderXV.setVisibility(View.VISIBLE);

                        if (upOrDown == 1) { // 上拉刷新
                            nearOrderDataList = nearOrderResult.getStaffOrderList();
                            nearOrderAdapter.setData(nearOrderDataList);
                            nearOrderXV.stopRefresh(); // 停止刷新
                        } else if (upOrDown == 2) { // 下拉加载
                            nearOrderDataList = nearOrderResult.getStaffOrderList();
                            nearOrderAdapter.setData(nearOrderDataList);
                            nearOrderXV.stopLoadMore();
                        }

                        nearOrderAdapter.setOnItemClickListener(new NearOrderAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(int position) {
                                Intent intent = new Intent(NearOrderActivity.this, OrderDetailActivity.class);
                                intent.putExtra("ORDER_ID", nearOrderDataList.get(position).getOrderId()); // 订单的id
                                intent.putExtra("STAFF_ADDRESS", staffAddress); // 获取服务人员的地址
                                intent.putExtra("STAFF_LONGITUDE", staffLongitude); // 获取服务人员的经度
                                intent.putExtra("STAFF_LATITUDE", staffLatitude); // 获取服务人员的纬度
                                intent.putExtra("MAX_LAX", nearOrderDataList.get(position).getMaxLax()); // 获取相隔的距离
                                startActivity(intent);
                            }
                        });
                    } else if (TextUtils.equals("附近订单无数据", nearOrderStr)) {
                        noDataInfoLl.setVisibility(View.VISIBLE);
                        nearOrderXV.setVisibility(View.GONE);
                    } else {
                        noDataInfoLl.setVisibility(View.VISIBLE);
                        nearOrderXV.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_orders_layout);

        dialogByProgress = new DialogByProgress(NearOrderActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        staffLongitude = intent.getStringExtra("STAFF_LONGITUDE"); // 接收服务人员的经度
        staffLatitude = intent.getStringExtra("STAFF_LATITUDE"); // 接收服务人员的纬度
        staffAddress = intent.getStringExtra("STAFF_ADDRESS"); // 接收服务人员的地址

        initView();
        initRefresh();
        initListener();
        initDate(pageIndex);
    }

    /**
     * 注册广播
     */
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter nearOrderFilter = new IntentFilter();
        nearOrderFilter.addAction("HOME_WASHING");
        nearOrderFilter.addAction("MAKE_WASHING");
        NearOrderActivity.this.registerReceiver(nearOrderReceiver, nearOrderFilter);
    }

    /**
     * 注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NearOrderActivity.this.unregisterReceiver(nearOrderReceiver);
    }

    /**
     * 初始化
     */
    private void initView() {
        nearOrdersBack = (ImageView) findViewById(R.id.near_orders_back); // 返回
        nearOrdersLL = (LinearLayout) findViewById(R.id.near_orders_ll);
        nearOrdersTv1 = (TextView) findViewById(R.id.near_orders_tv1); // 默认
        nearOrdersTv2 = (TextView) findViewById(R.id.near_orders_tv2); // 距离
        nearOrdersTv3 = (TextView) findViewById(R.id.near_orders_tv3);
        nearOrdersLl3 = (LinearLayout) findViewById(R.id.near_orders_ll3);
        nearOrdersStyleTv = (TextView) findViewById(R.id.near_orders_style_tv); // 订单类型
        nearOrdersStyleIv = (ImageView) findViewById(R.id.near_orders_style_iv); // 订单类型的三角图片
        noDataInfoLl = (LinearLayout) findViewById(R.id.no_data_info_ll); // 无数据时显示
        nearOrderXV = (XRefreshView) findViewById(R.id.near_order_XV); // 刷新控件
        nearOrdersRV = (RecyclerView) findViewById(R.id.near_orders_RecyclerView);
    }

    /**
     * 初始化加载
     */
    private void initRefresh() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(NearOrderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        nearOrdersRV.setLayoutManager(layoutManager);
        nearOrderAdapter = new NearOrderAdapter(NearOrderActivity.this, nearOrderDataList);
        nearOrdersRV.setAdapter(nearOrderAdapter);

        nearOrderXV.setPullRefreshEnable(true);
        nearOrderXV.setPullLoadEnable(true);
        nearOrderXV.restoreLastRefreshTime(lastRefreshTime);
        nearOrderXV.setAutoRefresh(true);
        nearOrderXV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override // 上拉刷新
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                requestData();
                upOrDown = 1;
                pageIndex = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDate(pageIndex);
                        lastRefreshTime = nearOrderXV.getLastRefreshTime();//设置最后一次的刷新时间
                    }
                }, 500);
            }

            @Override // 下拉加载
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                upOrDown = 2;
                pageIndex = ++pageIndex;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDate(pageIndex);
                    }
                }, 500);
            }
        });
        requestData();
    }

    private void requestData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nearOrderAdapter.getCustomLoadMoreView() == null) {
                    nearOrderAdapter.setCustomLoadMoreView(new CustomFooterView(NearOrderActivity.this));
                }
                nearOrderAdapter.setData(nearOrderDataList);
            }
        }, 1000);
    }

    /**
     * 监听
     */
    private void initListener() {
        nearOrdersBack.setOnClickListener(this);
        nearOrdersTv1.setOnClickListener(this);
        nearOrdersTv2.setOnClickListener(this);
        nearOrdersLl3.setOnClickListener(this);
    }

    /**
     * 设置参数
     *
     * @param pageIndex
     */
    private void initDate(int pageIndex) {
        tag = "1";
        gteData(tag, pageIndex);
    }

    /**
     * 根据不同的标签获取数据
     *
     * @param tag
     * @param pageIndex
     */
    private void gteData(String tag, int pageIndex) {
        if (tag.equals("1")) { // 附近订单的类型为默认
            ordering = "";
            orderType = "";
            this.tag = "";
            getDataByService(ordering, pageIndex, orderType); // 根据不同的类型加载服务器数据并赋值
        } else if (tag.equals("2")) { // 附近订单的类型为距离
            ordering = "2";
            orderType = "";
            this.tag = "";
            getDataByService(ordering, pageIndex, orderType); // 根据不同的类型加载服务器数据并赋值
        } else if (tag.equals("3")) { // 附近订单的类型为上门洗车
            ordering = "2";
            orderType = "0";
            this.tag = "";
            getDataByService(ordering, pageIndex, orderType); // 根据不同的类型加载服务器数据并赋值
        } else if (tag.equals("4")) { // 附近订单的类型为预约洗车
            ordering = "2";
            orderType = "1";
            this.tag = "";
            getDataByService(ordering, pageIndex, orderType); // 根据不同的类型加载服务器数据并赋值
        }
    }

    /**
     * 根据不同的类型加载服务器数据并赋值
     *
     * @param ordering
     * @param pageIndex
     * @param orderType
     */
    private void getDataByService(String ordering, int pageIndex, String orderType) {
        dialogByProgress.show();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("longitude", staffLongitude);
        builder.add("latitude", staffLatitude);
        builder.add("ordering", ordering);
        builder.add("pageIndex", String.valueOf(pageIndex));
        builder.add("orderType", orderType);
        FormBody body = builder.build();
        Request request = new Request.Builder().url(GlobalConsts.GET_NEAR_ORDER_LIST_URL).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "获取附近的订单信息----" + bodyString.toString());
                Gson nearOrderGson = new Gson();
                NearOrderRequest nearOrderRequest = nearOrderGson.fromJson(bodyString, NearOrderRequest.class);
                Message nearOrderMsg = Message.obtain();
                nearOrderMsg.what = GlobalConsts.GET_NEAR_ORDER_LIST_HANDLER;
                nearOrderMsg.obj = nearOrderRequest;
                handler.sendMessage(nearOrderMsg);
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
            case R.id.near_orders_back:
                NearOrderActivity.this.finish();
                break;

            case R.id.near_orders_tv1: // 默认
                tag = "1";
                nearOrdersTv1.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.orange));
                nearOrdersTv2.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.black));
                hidePW();
                gteData(tag, pageIndex);
                break;

            case R.id.near_orders_tv2: // 距离
                tag = "2";
                nearOrdersTv1.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.black));
                nearOrdersTv2.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.orange));
                hidePW();
                gteData(tag, pageIndex);
                break;

            case R.id.near_orders_ll3: // 订单类型的复选控件
                nearOrdersTv1.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.black));
                nearOrdersTv2.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.black));
                nearOrdersStyleTv.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.orange));
                nearOrdersStyleIv.setImageResource(R.mipmap.near_orders_iv3);
                if (showNearOrderPW == null) {
                    showNearOrderPW = new ShowNearOrderPW(NearOrderActivity.this);
                }
                showNearOrderPW.showPricePopupWindow(nearOrdersTv3);
                showSingerBtnLeftAnim();
                nearOrdersStyleIv.startAnimation(LeftRotate);
                showNearOrderPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        showSingerBtnRightAnim();
                        nearOrdersStyleIv.startAnimation(RightRotate);
                    }
                });
                break;
        }
    }

    /**
     * 附近订单的广播接受者
     */
    private class NearOrderBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "HOME_WASHING": // 上门洗车
                    tag = "3";
                    gteData(tag, pageIndex);
                    if (showNearOrderPW != null && showNearOrderPW.isShowing()) {
                        showNearOrderPW.dismiss();
                    }
                    break;

                case "MAKE_WASHING": // 预约洗车
                    tag = "4";
                    gteData(tag, pageIndex);
                    if (showNearOrderPW != null && showNearOrderPW.isShowing()) {
                        showNearOrderPW.dismiss();
                    }
                    break;
            }
        }
    }

    /**
     * 动画旋转文字隐藏
     */
    private void showSingerBtnLeftAnim() {
        LeftRotate = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LeftRotate.setDuration(400);
        LeftRotate.setFillAfter(true);
    }

    /**
     * 动画旋转文字显示
     */
    private void showSingerBtnRightAnim() {
        RightRotate = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RightRotate.setDuration(400);
        RightRotate.setFillAfter(true);
    }

    /**
     * 隐藏复选弹窗
     */
    private void hidePW() {
        nearOrdersStyleTv.setTextColor(ContextCompat.getColor(NearOrderActivity.this, R.color.black));
        nearOrdersStyleIv.setImageResource(R.mipmap.near_orders_iv1);
        if (showNearOrderPW != null && showNearOrderPW.isShowing()) {
            showNearOrderPW.dismiss();
        }
    }

    public void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
