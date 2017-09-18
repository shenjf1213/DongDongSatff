package com.dongdong.car.com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dongdong.car.entity.BindTokenRequest;
import com.dongdong.car.entity.PushRequest;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.NetUtil;
import com.google.gson.Gson;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/5/24.
 */

public class StaffApplication extends Application {

    private Intent orderIntent = new Intent();
    private static Context context; // 全局上下文对象
    private static Application mApplication;
    public static int mNetWorkState;

    public static synchronized Application getInstance() {
        return mApplication;
    }

    private SharedPreferences spf;
    private String devicetoken, userID, orderId, orderState, isDevicetoken;
    private String AppType = "1";
    private String UserType = "2";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.BIND_DEVICE_TOKEN_HANDLER:
                    BindTokenRequest bindTokenRequest = (BindTokenRequest) msg.obj;
                    if (bindTokenRequest.getIsSucess().equals("true")) {
                        SharedPreferences.Editor editor = context.getSharedPreferences("user_info", 0).edit();
                        editor.putString("device_token", "true");
                        editor.apply();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mApplication = this;
        initData();

        spf = mApplication.getSharedPreferences("user_info", 0);
        userID = spf.getString("uid", "");
        isDevicetoken = spf.getString("device_token", "");

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() { // 注册推送服务，每次调用register方法都会回调该接口
            @Override
            public void onSuccess(String deviceToken) { // 注册成功会返回device token
                Log.d("test", "动动美车服务端----deviceToken-----" + deviceToken.toString());
                if (!userID.equals("")) {
                    if (isDevicetoken.equals("")) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("devicetoken", deviceToken);
                        builder.add("userID", userID);
                        builder.add("appType", AppType);
                        builder.add("userType", UserType);
                        FormBody body = builder.build();
                        Request request = new Request.Builder().url(GlobalConsts.BIND_DEVICE_TOKEN_URL).post(body).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String bodyString = response.body().string();
                                Gson bindTokenGson = new Gson();
                                BindTokenRequest bindTokenRequest = bindTokenGson.fromJson(bodyString, BindTokenRequest.class);
                                Message bindTokenMsg = Message.obtain();
                                bindTokenMsg.what = GlobalConsts.BIND_DEVICE_TOKEN_HANDLER;
                                bindTokenMsg.obj = bindTokenRequest;
                                handler.sendMessage(bindTokenMsg);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

//        UmengMessageHandler umengMessageHandler = new UmengMessageHandler() { // 接收自定义的推送消息
//            @Override
//            public void dealWithCustomMessage(Context context, final UMessage uMessage) {
//                super.dealWithCustomMessage(context, uMessage);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("test", "接收到的自定义消息--------" + uMessage.custom.toString());
//                    }
//                });
//            }
//        };
//        mPushAgent.setMessageHandler(umengMessageHandler);

        /**
         * 友盟推送消息的点击方法
         */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                // {"MsgType":"Order","Data":{"OrderId":"B1873B86-8A18-4D70-A9A4-C3369D8B1F8F","OrderState":"3"}}
                Log.d("test", "接收友盟推送的消息-----" + uMessage.custom.toString());
                Gson pushGson = new Gson();
                PushRequest pushRequest = pushGson.fromJson(uMessage.custom, PushRequest.class);
                PushRequest.PushResult pushResult = pushRequest.getData();
                orderId = pushResult.getOrderId();
                orderState = pushResult.getOrderState();

                if ("2".equals(orderState)) { // 下订单后待接单
                    orderIntent.setAction("ORDER_TO_BE");
                    orderIntent.putExtra("ORDER_ID_TO_BE", orderId);
                    orderIntent.putExtra("ORDER_STATE_TO_BE", orderState);
                    StaffApplication.getContext().sendBroadcast(orderIntent);
                }

                if ("3".equals(orderState)) { // 已接收订单
                    orderIntent.setAction("ORDER_ACCEPT");
                    orderIntent.putExtra("ORDER_ID_ACCEPT", orderId);
                    orderIntent.putExtra("ORDER_STATE_ACCEPT", orderState);
                    StaffApplication.getContext().sendBroadcast(orderIntent);
                }

                if ("4".equals(orderState)) { // 订单进行中
                    orderIntent.setAction("ORDER_PROCESSING");
                    orderIntent.putExtra("ORDER_ID_PROCESSING", orderId);
                    orderIntent.putExtra("ORDER_STATE_PROCESSING", orderState);
                    StaffApplication.getContext().sendBroadcast(orderIntent);
                }

                if ("5".equals(orderState)) { // 订单已完成
                    orderIntent.setAction("ORDER_COMPLETED");
                    orderIntent.putExtra("ORDER_ID_COMPLETED", orderId);
                    orderIntent.putExtra("ORDER_STATE_COMPLETED", orderState);
                    StaffApplication.getContext().sendBroadcast(orderIntent);
                }
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public static Context getContext() {
        return context;
    }

    private void initData() {
        mNetWorkState = NetUtil.getNetWorkState(this);
    }
}
