package com.dongdong.car.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by 沈 on 2017/5/11.
 */

public class DataConversionByShen {

    private Context context;

    public DataConversionByShen(Context context) {
        this.context = context;
    }

    public DataConversionByShen() {

    }

    public final static String getTimeByHour(String str) {
        if (!str.equals("")) {
            if (str.equals("7点")) {
                str = "07";
            } else if (str.equals("8点")) {
                str = "08";
            } else if (str.equals("9点")) {
                str = "09";
            } else if (str.equals("10点")) {
                str = "10";
            } else if (str.equals("11点")) {
                str = "11";
            } else if (str.equals("12点")) {
                str = "12";
            } else if (str.equals("13点")) {
                str = "13";
            } else if (str.equals("14点")) {
                str = "14";
            } else if (str.equals("15点")) {
                str = "15";
            } else if (str.equals("16点")) {
                str = "16";
            } else if (str.equals("17点")) {
                str = "17";
            } else if (str.equals("18点")) {
                str = "18";
            } else if (str.equals("19点")) {
                str = "19";
            } else if (str.equals("20点")) {
                str = "20";
            }
            return new String(str);
        }
        return new String(str);
    }

    public final static String getTimeByMinute(String str) {
        if (!str.equals("")) {
            if (str.equals("00分")) {
                str = "00";
            } else if (str.equals("10分")) {
                str = "10";
            } else if (str.equals("20分")) {
                str = "20";
            } else if (str.equals("30分")) {
                str = "30";
            } else if (str.equals("40分")) {
                str = "40";
            } else if (str.equals("50分")) {
                str = "50";
            }
            return new String(str);
        }
        return new String(str);
    }

    public final static String getOrderStateMsg(String str) {
        if (!"".equals(str)) {
            if ("1".equals(str)) {
                str = "待支付";
            } else if ("2".equals(str)) {
                str = "待接单";
            } else if ("3".equals(str)) {
                str = "已接单";
            } else if ("5".equals(str)) {
                str = "已完成";
            } else if ("6".equals(str)) {
                str = "已取消";
            }
            return str;
        }
        return str;
    }

    public final static String getOrderStateByRecord(String str) {
        if (!"".equals(str)) {
            if ("1".equals(str)) {
                str = "订单已生成，待支付";
            } else if ("2".equals(str)) {
                str = "订单已支付，待接单";
            } else if ("3".equals(str)) {
                str = "订单已被接受，已安排人员前去洗车";
            } else if ("4".equals(str)) {
                str = "正在洗车";
            } else if ("5".equals(str)) {
                str = "车辆清洗完成";
            } else if ("6".equals(str)) {
                str = "订单取消";
            }
            return str;
        }
        return str;
    }

    /**
     * 正则判断车牌
     *
     * @param carNumber
     * @return
     */
    public final static boolean isCarNumberNO(String carNumber) {
        if (TextUtils.isEmpty(carNumber)) return false;
        else return carNumber.matches(GlobalConsts.REGEX_CAR_NUMBER);
    }

    /**
     * 获取本app的versionName
     */
    public final static String getVersionName(Context context, String versionName) {
        versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断用户手机是否安装微信客户端
     */
    public final static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager(); // 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);  // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 隐藏菊花
     *
     * @param context
     * @param dialogByProgress
     */
    public void hideDialogByProgress(Context context, DialogByProgress dialogByProgress) {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
