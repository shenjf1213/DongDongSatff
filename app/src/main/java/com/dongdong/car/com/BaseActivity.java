package com.dongdong.car.com;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.andview.refreshview.utils.LogUtils;
import com.dongdong.car.R;
import com.dongdong.car.receiver.NetBroadcastReceiver;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByTwoButton;
import com.dongdong.car.util.LogUtil;
import com.dongdong.car.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沈 on 2017/4/5.
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {

    private int PERMISSON_REQUESTCODE = 0x00099;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_APN_SETTINGS,
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
//                dialog1 = new DialogByOneButton(BaseActivity.this, "提示", "请为该应用开启相关权限", "确定");
//                dialog1.show();
//                dialog1.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
//                    @Override
//                    public void doPositive() {
//                        dialog1.dismiss();
//                    }
//                });
                isNeedCheck = false;
            }
        }
    }


    /**
     * 网络类型
     */
    private int netMobile;
    public static NetBroadcastReceiver.NetEvevt evevt;
    private DialogByTwoButton dialog;
    private DialogByOneButton dialog1;
    private Intent orderIntent = new Intent();
    private String orderId, orderState;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(ContextCompat.getColor(BaseActivity.this, R.color.orange));
        }

        LogUtil.d("test", getClass().getSimpleName()); // 查看各个继承BaseActivity是哪一个activity
        LogUtils.enableLog(false); // 刷新控件不打印日志
        evevt = this;
        inspectNet();
        if (isNetConnect() == false) {
            dialog = new DialogByTwoButton(BaseActivity.this, "提示", "网络连接失败，请检查您的网络是否连接", "取消", "确定");
            dialog.show();
            dialog.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                @Override
                public void doNegative() {
                    dialog.dismiss();
                }

                @Override
                public void doPositive() {
                    dialog.dismiss();
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                }
            });
        }
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect();
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == NetUtil.NETWORK_WIFI) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_MOBILE) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_NONE) {
            return false;
        }
        return false;
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    /**
     * 判断点击位置 实现输入框外的键盘隐藏效果
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
