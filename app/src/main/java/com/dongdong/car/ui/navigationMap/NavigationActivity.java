package com.dongdong.car.ui.navigationMap;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.ChangeOrderStateRequest;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.ScreenUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/15.
 */

public class NavigationActivity extends BaseActivity implements AMapNaviListener, View.OnClickListener, AMapNaviViewListener {

    private ImageView navigationBack, navigationFinish;
    private AMapNaviView carNaviView;
    private AMapNavi carNavi;
    private String LongitudeStr, LatitudeStr; // 订单的经纬度
    private String staffLongitude, staffLatitude; // 服务人员的经纬度
    private NaviLatLng carStartLatlng;
    private NaviLatLng carEndLatlng;
    private List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList;


    private View arrivalsView;
    private PopupWindow arrivalsPW;
    private String tag = "";
    private TextView navigationArrivalsMsg1, navigationArrivalsMsg2;
    private String ordersId; // 获取订单id

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.CREATE_ORDER_STATE_HANDLER: // 修改订单状态
                    ChangeOrderStateRequest changeOrderStateRequest = (ChangeOrderStateRequest) msg.obj;
                    if (dialogByProgress != null && dialogByProgress.isShowing()) {
                        dialogByProgress.dismiss();
                    }
                    String changeOrderStateString = changeOrderStateRequest.getMsg();
                    if (changeOrderStateString.equals("订单状态成功")) {
                        String changeOrderStateIng = changeOrderStateRequest.getData().getOrderSate();
                        if (changeOrderStateIng.equals("4")) { // 导航结束已开始工作
                            Intent intent = new Intent(NavigationActivity.this, TakePicturesActivity.class);
                            intent.putExtra("ORDER_ID", ordersId);
                            startActivity(intent);
                            NavigationActivity.this.finish();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_layout);

        dialogByProgress = new DialogByProgress(NavigationActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        carNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        carNaviView.onCreate(savedInstanceState);
        carNaviView.setAMapNaviViewListener(this);

        carNavi = AMapNavi.getInstance(getApplicationContext());
        carNavi.addAMapNaviListener(this);

        Intent intent = getIntent();
        ordersId = intent.getStringExtra("ORDER_ID");
        staffLatitude = intent.getStringExtra("STAFF_LATITUDE"); // 服务人员的纬度
        staffLongitude = intent.getStringExtra("STAFF_LONGITUDE"); // 服务人员的经度
        LatitudeStr = intent.getStringExtra("LATITUDE"); // 客户的纬度
        LongitudeStr = intent.getStringExtra("LONGITUDE"); // 客户的经度

        // carStartLatlng = new NaviLatLng(Double.parseDouble(staffLatitude), Double.parseDouble(staffLongitude));
        // carEndLatlng = new NaviLatLng(Double.parseDouble(LatitudeStr), Double.parseDouble(LongitudeStr));
        carStartLatlng = new NaviLatLng(39.773888, 116.363127);
        carEndLatlng = new NaviLatLng(39.773547, 116.330787);
        sList.add(carStartLatlng);
        eList.add(carEndLatlng);

        navigationBack = (ImageView) findViewById(R.id.navigation_back);
        navigationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity.this.finish();
            }
        });

        navigationFinish = (ImageView) findViewById(R.id.navigation_finish);
        navigationFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = "2";
                showArrivalsPW(tag);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        carNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carNaviView.onDestroy();
        carNavi.stopNavi();
        carNavi.destroy();
    }

    // 初始化失败
    @Override
    public void onInitNaviFailure() {

    }

    /**
     * 导航地图路线初始化成功
     * 计算骑行规划线路
     * 116.363127,39.773888 星光影视园
     * 116.330787,39.773547 地铁高米店北
     */
    @Override
    public void onInitNaviSuccess() {
        int strategy = 0;
        try {
            // 再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = carNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // carNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

        carNavi.calculateRideRoute(carStartLatlng, carEndLatlng);
    }

    // 开始导航回调
    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    // 当前位置回调
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    // 播报类型和播报文字回调
    @Override
    public void onGetNavigationText(int i, String s) {

    }

    // 结束模拟导航
    @Override
    public void onEndEmulatorNavi() {

    }

    /**
     * 到达目的地后回调函数
     */
    @Override
    public void onArriveDestination() {
        tag = "1";
        showArrivalsPW(tag);
    }

    /**
     * 步行,驾车,骑行路径规划成功后的回调函数
     */
    @Override
    public void onCalculateRouteSuccess() {
        carNavi.startNavi(NaviType.GPS);
        // carNavi.startNavi(NaviType.EMULATOR);
    }

    // 路线计算失败
    @Override
    public void onCalculateRouteFailure(int i) {

    }

    // 偏航后重新计算路线回调
    @Override
    public void onReCalculateRouteForYaw() {

    }

    // 拥堵后重新计算路线回调
    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    // 到达途径点
    @Override
    public void onArrivedWayPoint(int i) {

    }

    // GPS开关状态回调
    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    // 多路径算路成功回调
    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    /**
     * 到达目的地的弹窗
     *
     * @param tag
     */
    private void showArrivalsPW(String tag) {
        arrivalsView = View.inflate(NavigationActivity.this, R.layout.navigation_arrivals_layout, null);
        int width = ScreenUtils.getScreenWidth(NavigationActivity.this) / 10 * 8;
        arrivalsPW = new PopupWindow(arrivalsView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundAlpha(0.8f);

        navigationArrivalsMsg1 = (TextView) arrivalsView.findViewById(R.id.navigation_arrivals_msg_1);
        navigationArrivalsMsg2 = (TextView) arrivalsView.findViewById(R.id.navigation_arrivals_msg_2);
        Button navigationArrivalsBtn = (Button) arrivalsView.findViewById(R.id.navigation_arrivals_btn);

        navigationArrivalsBtn.setOnClickListener(this);

        if (tag.equals("1")) {
            navigationArrivalsMsg1.setVisibility(View.VISIBLE);
            navigationArrivalsMsg2.setVisibility(View.GONE);
        } else if (tag.equals("2")) {
            navigationArrivalsMsg1.setVisibility(View.GONE);
            navigationArrivalsMsg2.setVisibility(View.VISIBLE);
        }

        arrivalsPW.setFocusable(true);
        arrivalsPW.setOutsideTouchable(true);
        arrivalsPW.setBackgroundDrawable(new ColorDrawable(0x000000));
        arrivalsPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        arrivalsPW.showAtLocation(arrivalsView, Gravity.CENTER, 0, 0);
        arrivalsPW.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_arrivals_btn:
                dialogByProgress.show();
                changeOrderState(ordersId, "4");
                break;
        }
    }

    /**
     *  设置添加屏幕的背景透明度 
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = NavigationActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0  
        NavigationActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 防止个别手机出现导航页面白屏
     */
    @Override
    public Resources getResources() {
        return getBaseContext().getResources();
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    /**
     * 修改订单状态
     */
    private void changeOrderState(String ordersId, String orderState) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("orderState", orderState);
        builder.add("orderID", ordersId);
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
    }
}
