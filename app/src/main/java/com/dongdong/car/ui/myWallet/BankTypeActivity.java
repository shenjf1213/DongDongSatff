package com.dongdong.car.ui.myWallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.meiyou.jet.annotation.JFindView;
import com.meiyou.jet.annotation.JFindViewOnClick;
import com.meiyou.jet.process.Jet;

/**
 * Created by 沈 on 2017/7/2.
 */

public class BankTypeActivity extends BaseActivity implements View.OnClickListener {

    @JFindView(R.id.bank_type_back)
    @JFindViewOnClick(R.id.bank_type_back)
    private ImageView bankTypeBank;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_type_layout);
        Jet.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_type_back:
                BankTypeActivity.this.finish();
                break;
        }
    }
}
