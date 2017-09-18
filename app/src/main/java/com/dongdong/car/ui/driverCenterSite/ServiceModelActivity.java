package com.dongdong.car.ui.driverCenterSite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.ScreenUtils;

/**
 * Created by 沈 on 2017/6/7.
 */

public class ServiceModelActivity extends BaseActivity {

    private ImageView serviceModelBack;
    private ImageView serviceModelIv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_model_layout);

        serviceModelBack = (ImageView) findViewById(R.id.service_model_back);
        serviceModelIv = (ImageView) findViewById(R.id.service_model_iv);

        int wihth = ScreenUtils.getScreenWidth(ServiceModelActivity.this);
        int heigth = ScreenUtils.getScreenHeight(ServiceModelActivity.this);

        Glide.with(ServiceModelActivity.this)
                .load(R.drawable.service_model)
                .override(wihth, heigth)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(serviceModelIv);

        serviceModelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceModelActivity.this.finish();
            }
        });

    }
}
