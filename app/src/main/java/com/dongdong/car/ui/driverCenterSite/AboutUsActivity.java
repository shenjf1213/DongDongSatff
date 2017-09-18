package com.dongdong.car.ui.driverCenterSite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.ScreenUtils;

/**
 * Created by 沈 on 2017/6/7.
 */

public class AboutUsActivity extends BaseActivity {

    private ImageView aboutUsBack, aboutUsIv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_layout);

        aboutUsBack = (ImageView) findViewById(R.id.about_us_back);
        aboutUsIv = (ImageView) findViewById(R.id.about_us_iv);

        int width = ScreenUtils.getScreenWidth(AboutUsActivity.this);
        int height = ScreenUtils.getScreenHeight(AboutUsActivity.this);

        Glide.with(AboutUsActivity.this)
                .load(R.drawable.about_us)
                .override(width, height)
                .into(aboutUsIv);

        aboutUsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
    }
}

