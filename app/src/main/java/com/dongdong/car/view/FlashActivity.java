package com.dongdong.car.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dongdong.car.MainActivity;
import com.dongdong.car.R;
import com.dongdong.car.ui.driverCenter.LoginActivity;
import com.dongdong.car.util.ScreenUtils;

/**
 * Created by 沈 on 2017/6/14.
 */

public class FlashActivity extends Activity implements View.OnClickListener {

    private int count = 3;
    private SharedPreferences spf;
    private ImageView flashIv; // 背景照片
    private RelativeLayout flashRl;
    private TextView flashTv2; // 倒计时文字
    private Handler handler = new Handler();
    private String id, type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(ContextCompat.getColor(FlashActivity.this, R.color.wel_status_color));
        }

        spf = FlashActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");
        type = spf.getString("type", "");

        initView();
        initData();
        initListener();

    }

    private void initData() {
        flashRl.getBackground().setAlpha(100); // 设置背景透明度 0~255透明度值 ，0为完全透明，255为不透明

        int width = ScreenUtils.getScreenWidth(FlashActivity.this);
        int height = ScreenUtils.getScreenHeight(FlashActivity.this);

        Glide.with(FlashActivity.this)
                .load(R.drawable.wel)
                .override(width, height)
                .into(flashIv);

        count = 3;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flashTv2.setText(String.valueOf(count));
                count = count - 1;
                if (count >= 1) {
                    handler.postDelayed(this, 850);
                }
            }
        }, 850);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityByFlash();
            }
        }, 3400);
    }

    private void startActivityByFlash() {
        if (!id.equals("")) {
            startActivity(new Intent(FlashActivity.this, MainActivity.class));
            FlashActivity.this.finish();
        } else {
            startActivity(new Intent(FlashActivity.this, LoginActivity.class));
            FlashActivity.this.finish();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        flashIv = (ImageView) findViewById(R.id.flash_iv);
        flashRl = (RelativeLayout) findViewById(R.id.flash_rl);
        flashTv2 = (TextView) findViewById(R.id.flash_tv2);
    }

    /**
     * 监听
     */
    private void initListener() {
        flashRl.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash_rl:
                handler.removeCallbacksAndMessages(null);
                startActivityByFlash();
                break;
        }
    }
}
