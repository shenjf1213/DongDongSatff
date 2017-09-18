package com.dongdong.car.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.dongdong.car.R;
import com.dongdong.car.adapter.CompletedOrderAdapter;
import com.dongdong.car.entity.orderRecord.OrderRecordList;
import com.dongdong.car.entity.orderRecord.OrderRecordRequest;
import com.dongdong.car.entity.orderRecord.OrderRecordResult;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.view.CustomFooterView;
import com.google.gson.Gson;
import com.meiyou.jet.annotation.JFindView;
import com.meiyou.jet.process.Jet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/3.
 */

public class makingOrderFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private String staffId;
    private String orderType = "1";
    private int pageIndex = 1;
    private String orderRecordID;
    @JFindView(R.id.no_data_info_by_completed)
    private LinearLayout noDataInfoByMaking; //没有数据时显示的图
    @JFindView(R.id.completed_orders_XV)
    private XRefreshView makingOrdersXV; // 加载刷新控件
    @JFindView(R.id.completed_orders_RecyclerView)
    private RecyclerView makingOrderRV; // 数据显示的布局
    private List<OrderRecordList> OrderRecordDataList = new ArrayList<>();
    private CompletedOrderAdapter OrderRecordAdapter;
    private int upOrDown; // 判断动作
    private int mLoadCount = 0;
    public static long lastRefreshTime; // 下拉刷新结束的时间

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_RECORD_LIST_2_HANDLER:
                    OrderRecordRequest makingRequest = (OrderRecordRequest) msg.obj;
                    hideDialogByProgress();
                    String makingState = makingRequest.getIsSucess();
                    if (TextUtils.equals("true", makingState)) {
                        noDataInfoByMaking.setVisibility(View.GONE);
                        makingOrdersXV.setVisibility(View.VISIBLE);
                        OrderRecordResult makingResult = makingRequest.getData();

                        if (upOrDown == 1) {
                            OrderRecordDataList = makingResult.getStaffAppList();
                            OrderRecordAdapter.setData(OrderRecordDataList);
                            makingOrdersXV.stopRefresh();
                        } else if (upOrDown == 2) {
                            OrderRecordDataList = makingResult.getStaffAppList();
                            OrderRecordAdapter.setData(OrderRecordDataList);
                            makingOrdersXV.stopLoadMore();
                        }
                    } else {
                        noDataInfoByMaking.setVisibility(View.VISIBLE);
                        makingOrdersXV.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_completed_order_layout, container, false);
            Jet.bind(this, rootView);

            dialogByProgress = new DialogByProgress(makingOrderFragment.this.getActivity());
            dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

            SharedPreferences spf = makingOrderFragment.this.getActivity().getSharedPreferences("user_info", 0);
            staffId = spf.getString("uid", "");

            initRefresh();
        }
        return rootView;
    }

    /**
     * 获取数据
     */
    private void initRefresh() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(makingOrderFragment.this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        makingOrderRV.setLayoutManager(layoutManager);
        OrderRecordAdapter = new CompletedOrderAdapter(makingOrderFragment.this.getActivity(), OrderRecordDataList);
        makingOrderRV.setAdapter(OrderRecordAdapter);

        makingOrdersXV.setPullRefreshEnable(true); // 设置是否可以下拉刷新
        makingOrdersXV.setPullLoadEnable(true);  // 设置是否可以上拉加载
        makingOrdersXV.setAutoRefresh(true); // 设置时候可以自动刷新
        makingOrdersXV.restoreLastRefreshTime(lastRefreshTime); // 设置上次刷新的时间
        makingOrdersXV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                requestData();
                upOrDown = 1;
                pageIndex = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(pageIndex);
                        lastRefreshTime = makingOrdersXV.getLastRefreshTime();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                upOrDown = 2;
                pageIndex = ++pageIndex;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(pageIndex);
                    }
                }, 1000);
            }
        });
        requestData();
    }

    private void requestData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (OrderRecordAdapter.getCustomLoadMoreView() == null) {
                    OrderRecordAdapter.setCustomLoadMoreView(new CustomFooterView(makingOrderFragment.this.getActivity()));
                }
                OrderRecordAdapter.setData(OrderRecordDataList);
            }
        }, 1000);
    }

    /**
     * 加载数据（上拉刷新，下拉加载）
     *
     * @param pageIndex
     */
    private void getData(int pageIndex) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/StaffOrderTypeRecord?staffId=" + staffId + "&orderType=" + orderType + "&pageIndex=" + String.valueOf(pageIndex)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "员工的上门预约订单记录-----" + bodyString.toString());
                Gson makingGson = new Gson();
                OrderRecordRequest makingRequest = makingGson.fromJson(bodyString, OrderRecordRequest.class);
                Message makingMsg = Message.obtain();
                makingMsg.what = GlobalConsts.GET_ORDER_RECORD_LIST_2_HANDLER;
                makingMsg.obj = makingRequest;
                handler.sendMessage(makingMsg);
            }
        });
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
