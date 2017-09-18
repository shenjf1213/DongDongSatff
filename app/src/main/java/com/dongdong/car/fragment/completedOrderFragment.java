package com.dongdong.car.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class completedOrderFragment extends android.support.v4.app.Fragment {

    private View rootView;
    private String staffId;
    private String orderState = "5";
    private String orderRecordID; // 某一个订单的id
    @JFindView(R.id.no_data_info_by_completed)
    private LinearLayout noDataInfoByCompleted; //没有数据时显示的图
    @JFindView(R.id.completed_orders_XV)
    private XRefreshView completedOrdersXV; // 加载刷新控件
    @JFindView(R.id.completed_orders_RecyclerView)
    private RecyclerView completedOrdersRV; // 数据显示的布局
    private List<OrderRecordList> completedOrderDataList = new ArrayList<>();
    private CompletedOrderAdapter completedOrderAdapter;
    private int upOrDown; // 判断动作
    private int pageIndex = 1;
    private int mLoadCount = 0;
    public static long lastRefreshTime; // 下拉刷新结束的时间

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_RECORD_LIST_1_HANDLER:
                    OrderRecordRequest completedRequest = (OrderRecordRequest) msg.obj;
                    hideDialogByProgress();
                    String completedState = completedRequest.getIsSucess();
                    if (TextUtils.equals("true", completedState)) {
                        noDataInfoByCompleted.setVisibility(View.GONE);
                        completedOrdersXV.setVisibility(View.VISIBLE);
                        OrderRecordResult completedResult = completedRequest.getData();

                        if (upOrDown == 1) { // 下拉刷新
                            completedOrderDataList = completedResult.getStaffAppList();
                            completedOrderAdapter.setData(completedOrderDataList);
                            completedOrdersXV.stopRefresh();
                        } else if (upOrDown == 2) { // 上拉加载
                            completedOrderDataList = completedResult.getStaffAppList();
                            completedOrderAdapter.setData(completedOrderDataList);
                            completedOrdersXV.stopLoadMore();
                        }
                    } else if (TextUtils.equals("false", completedState)) {
                        noDataInfoByCompleted.setVisibility(View.VISIBLE);
                        completedOrdersXV.setVisibility(View.GONE);
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

            dialogByProgress = new DialogByProgress(completedOrderFragment.this.getActivity());
            dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

            SharedPreferences spf = completedOrderFragment.this.getActivity().getSharedPreferences("user_info", 0);
            staffId = spf.getString("uid", "");

            initRefresh();
        }
        return rootView;
    }

    /**
     * 刷新控件初始化
     */
    private void initRefresh() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(completedOrderFragment.this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        completedOrdersRV.setLayoutManager(layoutManager);
        completedOrderAdapter = new CompletedOrderAdapter(completedOrderFragment.this.getActivity(), completedOrderDataList);
        completedOrdersRV.setAdapter(completedOrderAdapter);

        completedOrdersXV.setPullRefreshEnable(true); // 设置是否可以下拉刷新
        completedOrdersXV.setPullLoadEnable(true);  // 设置是否可以上拉加载
        completedOrdersXV.setAutoRefresh(true); // 设置时候可以自动刷新
        completedOrdersXV.restoreLastRefreshTime(lastRefreshTime); // 设置上次刷新的时间
        completedOrdersXV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
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
                        lastRefreshTime = completedOrdersXV.getLastRefreshTime();
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
                if (completedOrderAdapter.getCustomLoadMoreView() == null) {
                    completedOrderAdapter.setCustomLoadMoreView(new CustomFooterView(completedOrderFragment.this.getActivity()));
                }
                completedOrderAdapter.setData(completedOrderDataList);
            }
        }, 1000);
    }

    /**
     * 加载或刷新数据
     *
     * @param pageIndex
     */
    private void getData(int pageIndex) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/StaffOrderRecord?staffId=" + staffId + "&orderState=" + orderState + "&pageIndex=" + String.valueOf(pageIndex)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                //Log.d("test", "已经完成的订单---------------" + bodyString.toString());
                Gson completedGson = new Gson();
                OrderRecordRequest completedRequest = completedGson.fromJson(bodyString, OrderRecordRequest.class);
                Message completedMsg = Message.obtain();
                completedMsg.what = GlobalConsts.GET_ORDER_RECORD_LIST_1_HANDLER;
                completedMsg.obj = completedRequest;
                handler.sendMessage(completedMsg);
            }
        });
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
