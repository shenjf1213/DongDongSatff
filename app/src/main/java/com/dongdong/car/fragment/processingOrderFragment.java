package com.dongdong.car.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.entity.GetPageRequest;
import com.dongdong.car.entity.orderRecordDetail.DetailByOrderInfo;
import com.dongdong.car.entity.orderRecordDetail.DetailByUserInfo;
import com.dongdong.car.entity.orderRecordDetail.OrderItemList;
import com.dongdong.car.entity.orderRecordDetail.OrderPictureList;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailRequest;
import com.dongdong.car.entity.orderRecordDetail.OrderRecordDetailResult;
import com.dongdong.car.ui.navigationMap.OrderOnProgressActivity;
import com.dongdong.car.ui.navigationMap.TakePicturesActivity;
import com.dongdong.car.ui.navigationMap.TakePicturesByAfterActivity;
import com.dongdong.car.util.CoverFlowViewPager;
import com.dongdong.car.util.DialogByOneButton;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/3.
 */

public class processingOrderFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private View rootView;
    private String staffId, orderId;
    private String orderState = "4";

    @JFindView(R.id.processing_order_ll)
    private RelativeLayout processingOrderLl; // 无数据则显示的布局
    @JFindView(R.id.processing_order_sv)
    private ScrollView processingOrderSv; // 数据主布局
    @JFindView(R.id.processing_order_date)
    private TextView processingOrderDate; // 日期
    @JFindView(R.id.processing_order_time)
    private TextView processingOrderTime; // 时间
    @JFindView(R.id.processing_order_address)
    private TextView processingOrderAddress; // 订单地址
    @JFindView(R.id.processing_order_name)
    private TextView processingOrderName; // 客户姓名
    @JFindView(R.id.processing_order_phone)
    private TextView processingOrderPhone; // 客户联系方式
    @JFindView(R.id.processing_order_number_plate)
    private TextView processingOrderNumberPlate; // 客户车辆信息
    @JFindView(R.id.processing_order_item)
    private TextView processingOrderItem; // 清洗项目
    @JFindView(R.id.processing_order_remarks)
    private TextView processingOrderRemarks; // 备注
    @JFindView(R.id.processing_order_tv8)
    private TextView processingOrderTv; // 轮播显示的文字
    @JFindView(R.id.processing_order_picture)
    private CircleImageView processingOrderPicture; // 轮播显示的照片
    @JFindView(R.id.continuing_business_tv)
    @JFindViewOnClick(R.id.continuing_business_tv)
    private TextView continuingBusinessTv; // 继续当前订单的业务
    @JFindView(R.id.processing_order_cover)
    private CoverFlowViewPager processingOrderCover; // 照片轮播控件

    private List<View> aboutCarList = new ArrayList<>(); // 照片集合
    private List<OrderItemList> processingItemList = new ArrayList<>(); // 清洗的项目
    private List<String> list = new ArrayList<>();
    private String processingItemString;
    private List<OrderPictureList> processingCarPicList = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<GetPageRequest.GetPageResult> getPageResultList = new ArrayList<>();
    private String getPageState, pageIndex;
    private Intent intent;

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_RECORD_LIST_3_HANDLER:
                    OrderRecordDetailRequest processingRequest = (OrderRecordDetailRequest) msg.obj;
                    hideDialogByProgress();
                    String processingState = processingRequest.getIsSucess();
                    String processingString = processingRequest.getMsg();
                    if (processingState.equals("true")) {
                        if ("订单客户Info".equals(processingString)) {
                            OrderRecordDetailResult processingResult = processingRequest.getData();
                            DetailByOrderInfo processingOrderInfo = processingResult.getOrderInfo();
                            DetailByUserInfo processingUserInfo = processingResult.getUserInfo();
                            String processingOrderTimes = processingOrderInfo.getOrderTime();
                            String[] time = processingOrderTimes.split(" ");

                            orderId = processingOrderInfo.getId(); // 获取订单id
                            processingOrderDate.setText(time[0]);
                            processingOrderTime.setText(time[1]);
                            processingOrderAddress.setText(processingOrderInfo.getAddress());
                            processingOrderName.setText(processingUserInfo.getUserName());
                            processingOrderPhone.setText(processingUserInfo.getMobilePhone());
                            processingOrderNumberPlate.setText(processingUserInfo.getCarNumber() + "  " + processingUserInfo.getCarModel() + "  " + processingUserInfo.getCarColor());

                            processingCarPicList = processingOrderInfo.getOrderPictureList();
                            processingItemList = processingOrderInfo.getOrderItemList();

                            if (processingUserInfo.getRemark() != null && !processingUserInfo.getRemark().isEmpty()) {
                                processingOrderRemarks.setText(processingUserInfo.getRemark());
                            } else {
                                processingOrderRemarks.setText("无");
                            }

                            if (processingItemList != null && !processingItemList.isEmpty()) {
                                for (OrderItemList orderItemList : processingItemList) {
                                    list.add(orderItemList.getItemName());
                                }
                                processingItemString = String.valueOf(list.subList(0, list.size()));
                                processingOrderItem.setText(processingItemString.substring(1, processingItemString.length() - 1));
                            } else {
                                processingOrderItem.setText("");
                            }

                            if (processingCarPicList != null && !processingCarPicList.isEmpty()) {
                                for (OrderPictureList orderPictureList : processingCarPicList) {
                                    list2.add(orderPictureList.getImgeFile());
                                }
                                processingOrderTv.setVisibility(View.VISIBLE);
                                processingOrderPicture.setVisibility(View.VISIBLE);
                                continuingBusinessTv.setVisibility(View.VISIBLE);
                                Glide.with(processingOrderFragment.this.getActivity()).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(processingOrderPicture);
                                processingOrderPicture.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        processingOrderPicture.setVisibility(View.GONE);
                                        processingOrderCover.setVisibility(View.VISIBLE);
                                        for (String image : list2) {
                                            ImageView imageView = new ImageView(processingOrderFragment.this.getActivity());
                                            Glide.with(processingOrderFragment.this.getActivity()).load(GlobalConsts.DONG_DONG_IMAGE_URL + image).into(imageView);
                                            aboutCarList.add(imageView);
                                        }
                                        processingOrderCover.setViewList(aboutCarList);
                                    }
                                });
                            }
                        } else {
                            processingOrderLl.setVisibility(View.VISIBLE);
                            processingOrderSv.setVisibility(View.GONE);
                            continuingBusinessTv.setVisibility(View.GONE);
                        }
                    }
                    break;

                case GlobalConsts.GET_PAGE_UNEXPECTED_EXIT_HANDLER:
                    GetPageRequest getPageRequest = (GetPageRequest) msg.obj;
                    hideDialogByProgress();
                    getPageState = getPageRequest.getIsSucess();
                    if (TextUtils.equals("true", getPageState)) {
                        getPageResultList = getPageRequest.getData();
                        if (getPageResultList != null && !getPageResultList.isEmpty()) {
                            pageIndex = getPageResultList.get(0).getPageIndex();
                            if (TextUtils.equals("1", pageIndex)) {
                                intent = new Intent(processingOrderFragment.this.getActivity(), TakePicturesActivity.class);
                                intent.putExtra("ORDER_ID", orderId);
                                startActivity(intent);
                            } else if (TextUtils.equals("2", pageIndex)) {
                                intent = new Intent(processingOrderFragment.this.getActivity(), OrderOnProgressActivity.class);
                                intent.putExtra("ORDER_ID", orderId);
                                startActivity(intent);
                            } else if (TextUtils.equals("3", pageIndex)) {
                                intent = new Intent(processingOrderFragment.this.getActivity(), TakePicturesByAfterActivity.class);
                                intent.putExtra("ORDER_ID", orderId);
                                startActivity(intent);
                            }
                        }
                    } else {
                        dialog = new DialogByOneButton(processingOrderFragment.this.getActivity(), "提示", "订单信息错误，请稍后重试", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_processing_order_layout, null);
            Jet.bind(this, rootView); // TODO fragment中注入

            dialogByProgress = new DialogByProgress(processingOrderFragment.this.getActivity());
            dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

            SharedPreferences spf = processingOrderFragment.this.getActivity().getSharedPreferences("user_info", 0);
            staffId = spf.getString("uid", "");

            initData();
        }
        return rootView;
    }

    /**
     * 获取数据并赋值
     */
    private void initData() {
        // dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/GetOrderInfo?staffId=" + staffId + "&orderState=" + orderState).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "正在进行中的订单-----" + bodyString.toString());
                Gson processingGson = new Gson();
                OrderRecordDetailRequest processingRequest = processingGson.fromJson(bodyString, OrderRecordDetailRequest.class);
                Message processingMsg = Message.obtain();
                processingMsg.what = GlobalConsts.GET_ORDER_RECORD_LIST_3_HANDLER;
                processingMsg.obj = processingRequest;
                handler.sendMessage(processingMsg);
            }
        });
    }

    /**
     * 监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continuing_business_tv: // 继续当前订单业务
                dialogByProgress.show();
                Request request = new Request.Builder().url(GlobalConsts.GET_PAGE_UNEXPECTED_EXIT_URL + orderId).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String bodyString = response.body().string();
                        Log.d("test", "获取当前为完成的页面信息----------" + bodyString.toString());
                        Gson getPageGson = new Gson();
                        GetPageRequest getPageRequest = getPageGson.fromJson(bodyString, GetPageRequest.class);
                        Message getPageMsg = Message.obtain();
                        getPageMsg.what = GlobalConsts.GET_PAGE_UNEXPECTED_EXIT_HANDLER;
                        getPageMsg.obj = getPageRequest;
                        handler.sendMessage(getPageMsg);
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
