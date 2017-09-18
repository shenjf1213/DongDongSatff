package com.dongdong.car.ui.navigationMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.DialogByTwoButton;
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
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/7/4.
 */

public class OrderOnProgressActivity extends BaseActivity implements View.OnClickListener {

    @JFindView(R.id.order_on_progress_back)
    @JFindViewOnClick(R.id.order_on_progress_back)
    private ImageView orderOnProgressBack; // 返回
    @JFindView(R.id.order_on_progress_date)
    private TextView orderOnProgressDate; // 订单日期
    @JFindView(R.id.order_on_progress_time)
    private TextView orderOnProgressTime; // 订单时间
    @JFindView(R.id.order_on_progress_address)
    private TextView orderOnProgressAddress; // 订单地址
    @JFindView(R.id.order_on_progress_name)
    private TextView orderOnProgressName; // 客户姓名
    @JFindView(R.id.order_on_progress_phone)
    private TextView orderOnProgressPhone; // 客户联系电话
    @JFindView(R.id.order_on_progress_number_plate)
    private TextView orderOnProgressNumberPlate; // 客户车辆信息
    @JFindView(R.id.order_on_progress_item)
    private TextView orderOnProgressItem; // 清洗项目
    @JFindView(R.id.order_on_progress_remarks)
    private TextView orderOnProgressRemarks; // 备注
    @JFindView(R.id.order_on_progress_btn)
    @JFindViewOnClick(R.id.order_on_progress_btn)
    private Button orderOnProgressBtn; // 清洗完成
    @JFindView(R.id.order_on_progress_tv7)
    private TextView orderOnProgressTv;
    @JFindView(R.id.order_on_progress_picture)
    private CircleImageView orderOnProgressPicture;
    @JFindView(R.id.order_on_progress_cover)
    private CoverFlowViewPager orderOnProgressCover; // 照片轮播控件
    private List<View> picturesList = new ArrayList<>();

    private List<OrderPictureList> processingPictureList = new ArrayList<>(); // 照片的数据集合
    private List<OrderItemList> processingItemList = new ArrayList<>(); // 清洗的项目
    private List<String> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private String processingItemString;

    private String staffId, orderId;
    private String orderState = "4";
    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog1;
    private DialogByTwoButton dialog2;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_ORDER_RECORD_LIST_3_HANDLER: // 获取正在进行中的订单赋值并显示
                    OrderRecordDetailRequest processingRequest = (OrderRecordDetailRequest) msg.obj;
                    hideDialogByProgress();
                    String processingState = processingRequest.getIsSucess();
                    if (processingState.equals("true")) {
                        OrderRecordDetailResult processingResult = processingRequest.getData();
                        DetailByOrderInfo processingOrderInfo = processingResult.getOrderInfo();
                        DetailByUserInfo processingUserInfo = processingResult.getUserInfo();
                        String processingOrderTimes = processingOrderInfo.getOrderTime();
                        String[] time = processingOrderTimes.split(" ");

                        orderId = processingOrderInfo.getId(); // 获取订单id
                        orderOnProgressDate.setText(time[0]);
                        orderOnProgressTime.setText(time[1]);
                        orderOnProgressAddress.setText(processingOrderInfo.getAddress());
                        orderOnProgressName.setText(processingUserInfo.getUserName());
                        orderOnProgressPhone.setText(processingUserInfo.getMobilePhone());
                        orderOnProgressNumberPlate.setText(processingUserInfo.getCarNumber() + "  " + processingUserInfo.getCarModel() + "  " + processingUserInfo.getCarColor());
                        processingItemList = processingOrderInfo.getOrderItemList();
                        processingPictureList = processingOrderInfo.getOrderPictureList();
                        if (TextUtils.isEmpty(processingUserInfo.getRemark())) {
                            orderOnProgressRemarks.setText("无");
                        } else {
                            orderOnProgressRemarks.setText(processingUserInfo.getRemark());
                        }

                        if (processingItemList != null && !processingItemList.isEmpty()) { // 清洗项目
                            for (OrderItemList orderItemList : processingItemList) {
                                list.add(orderItemList.getItemName());
                            }
                            processingItemString = String.valueOf(list.subList(0, list.size()));
                            orderOnProgressItem.setText(processingItemString.substring(1, processingItemString.length() - 1));
                        } else {
                            orderOnProgressItem.setText("");
                        }

                        if (processingPictureList != null && !processingPictureList.isEmpty()) { // 车辆周围的照片
                            for (OrderPictureList orderPictureList : processingPictureList) {
                                list2.add(orderPictureList.getImgeFile());
                            }
                            orderOnProgressTv.setVisibility(View.VISIBLE);
                            orderOnProgressPicture.setVisibility(View.VISIBLE);
                            Glide.with(OrderOnProgressActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + list2.get(0)).into(orderOnProgressPicture);
                            orderOnProgressPicture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    orderOnProgressPicture.setVisibility(View.GONE);
                                    orderOnProgressCover.setVisibility(View.VISIBLE);
                                    for (String pictures : list2) {
                                        ImageView imageView = new ImageView(OrderOnProgressActivity.this);
                                        Glide.with(OrderOnProgressActivity.this).load(GlobalConsts.DONG_DONG_IMAGE_URL + pictures).into(imageView);
                                        picturesList.add(imageView);
                                    }
                                    orderOnProgressCover.setViewList(picturesList);
                                }
                            });
                        } else {
                            orderOnProgressTv.setVisibility(View.GONE);
                            orderOnProgressPicture.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_on_progress_layout);
        Jet.bind(this);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("ORDER_ID");

        dialogByProgress = new DialogByProgress(OrderOnProgressActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SharedPreferences spf = OrderOnProgressActivity.this.getSharedPreferences("user_info", 0);
        staffId = spf.getString("uid", "");

        initDate();

    }

    /**
     * 获取正在进行中的订单参数并赋值
     */
    private void initDate() {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/GetOrderInfo?staffId=" + staffId + "&orderState=" + orderState).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson processingGson = new Gson();
                OrderRecordDetailRequest processingRequest = processingGson.fromJson(bodyString, OrderRecordDetailRequest.class);
                Message processingMsg = Message.obtain();
                processingMsg.what = GlobalConsts.GET_ORDER_RECORD_LIST_3_HANDLER;
                processingMsg.obj = processingRequest;
                handler.sendMessage(processingMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_on_progress_back:
                OrderOnProgressActivity.this.finish();
                break;

            case R.id.order_on_progress_btn: // 清洗完成
                dialog2 = new DialogByTwoButton(OrderOnProgressActivity.this, "提示", "确认车辆清洗完成吗？", "取消", "确定");
                dialog2.show();
                dialog2.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                    @Override
                    public void doNegative() {
                        dialog2.dismiss();
                    }

                    @Override
                    public void doPositive() {
                        dialog2.dismiss();
                        Intent intent = new Intent(OrderOnProgressActivity.this, TakePicturesByAfterActivity.class);
                        intent.putExtra("ORDER_ID", orderId);
                        startActivity(intent);
                        OrderOnProgressActivity.this.finish();
                    }
                });
                break;
        }
    }

    /**
     * 页面不可见时保存数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (orderId != null && !TextUtils.isEmpty(orderId)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RequestBody requestBody = new FormBody.Builder().add("orderId", orderId).add("pageIndex", "2").add("orderState", "4").build();
                        Request request = new Request.Builder().url(GlobalConsts.SAVE_PAGE_UNEXPECTED_EXIT_URL).post(requestBody).build();
                        okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }

}
