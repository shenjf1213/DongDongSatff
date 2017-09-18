package com.dongdong.car.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.NetUtil;


/**
 * Created by cheng on 2016/11/28.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvevt evevt = BaseActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if (evevt != null) {
                evevt.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);
    }
}
