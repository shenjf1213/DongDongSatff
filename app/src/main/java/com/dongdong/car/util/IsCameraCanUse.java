package com.dongdong.car.util;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by 沈 on 2017/5/3.
 */

public class IsCameraCanUse {

    public static String SDPATH = Environment.getExternalStorageDirectory() + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);

    public IsCameraCanUse() {

    }

    /**
     * 判断当前手机的摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 判断SD卡是否存在
     */
    public static boolean isSDCard(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            return true;
        } else {
            Toast.makeText(context, "SD卡不存在", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
