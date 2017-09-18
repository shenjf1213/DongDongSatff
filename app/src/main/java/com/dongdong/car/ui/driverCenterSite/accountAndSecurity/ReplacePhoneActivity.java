package com.dongdong.car.ui.driverCenterSite.accountAndSecurity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/8.
 */

public class ReplacePhoneActivity extends BaseActivity implements View.OnClickListener {

    private ImageView replaceBack;
    private TextView replacePhoneTv;
    private EditText replacePhoneEt;
    private Button replacePhoneCode;
    private EditText replaceCodeEt;
    private Button replacePhoneBtn;

    private TimeCount time; // 点击获取验证码后显示60秒倒计时
    private String replaceCodeString;
    private String replacePhoneString;

    private DialogByProgress dialogByProgress;
    private String id;
    private String oldPhoneString;
    private String code = "";

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_phone_layout);

        dialogByProgress = new DialogByProgress(ReplacePhoneActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        time = new TimeCount(60000, 1000); // 构造CountDownTimer对象

        Intent intent = getIntent();
        oldPhoneString = intent.getStringExtra("PHONE");
        id = intent.getStringExtra("ID");

        initView();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        replaceBack = (ImageView) findViewById(R.id.replace_back);
        replacePhoneTv = (TextView) findViewById(R.id.replace_phone_tv);
        replacePhoneEt = (EditText) findViewById(R.id.replace_phone_et);
        replacePhoneCode = (Button) findViewById(R.id.replace_phone_code);
        replaceCodeEt = (EditText) findViewById(R.id.replace_code_et);
        replacePhoneBtn = (Button) findViewById(R.id.replace_phone_btn);

        if (!oldPhoneString.equals("") && oldPhoneString.length() == 11) {
            replacePhoneTv.setText(oldPhoneString.substring(0, 3) + "****" + oldPhoneString.substring(7, 11));
        } else {
            replacePhoneTv.setText(oldPhoneString.substring(0, 3) + "****" + oldPhoneString.substring(7, oldPhoneString.length()));
        }
    }

    /**
     * 监听
     */
    private void initListener() {
        replaceBack.setOnClickListener(this);
        replacePhoneCode.setOnClickListener(this);
        replacePhoneBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replace_back:
                ReplacePhoneActivity.this.finish();
                break;

            case R.id.replace_phone_code: // 获取验证码
                replacePhoneString = replacePhoneEt.getText().toString().trim();
                if (replacePhoneString.equals("")) {
                    Toast.makeText(ReplacePhoneActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    replacePhoneCode.setClickable(true);
                    replacePhoneCode.setBackgroundResource(R.drawable.background_shape);
                    return;
                } else if (isRegisterMobileNO() == false && replacePhoneString.equals(oldPhoneString)) {
                    Toast.makeText(ReplacePhoneActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    replacePhoneCode.setClickable(true);
                    replacePhoneCode.setBackgroundResource(R.drawable.background_shape);
                    return;
                } else {
                    time.start();
                    Request request = new Request.Builder().url(GlobalConsts.REGISTER_CODE_URL + replacePhoneString).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            code = bodyString.substring(1, bodyString.length() - 1);
                        }
                    });
                }
                break;

            case R.id.replace_phone_btn: // 确认修改验证码
                replaceCodeString = replaceCodeEt.getText().toString().trim();
                if (code.equals("")) {
                    Toast.makeText(ReplacePhoneActivity.this, "请点击获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (replaceCodeString.equals("")) {
                    Toast.makeText(ReplacePhoneActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!replaceCodeString.equals(code)) {
                    Toast.makeText(ReplacePhoneActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent1 = new Intent(ReplacePhoneActivity.this, SafetyVerificationActivity.class);
                    intent1.putExtra("ID", id);
                    intent1.putExtra("OLD_PHONE", replacePhoneString);
                    startActivity(intent1);
                    ReplacePhoneActivity.this.finish();
                }
                break;
        }
    }


    /**
     * 时间倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); // 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() { // 计时完毕时触发
            replacePhoneCode.setText("重新验证");
            replacePhoneCode.setClickable(true);
            replacePhoneCode.setTextColor(ContextCompat.getColor(ReplacePhoneActivity.this, R.color.white));
            replacePhoneCode.setBackgroundResource(R.drawable.background_shape);
        }

        @Override
        public void onTick(long millisUntilFinished) { // 计时过程显示
            replacePhoneCode.setClickable(false);
            replacePhoneCode.setTextColor(ContextCompat.getColor(ReplacePhoneActivity.this, R.color.black));
            replacePhoneCode.setBackgroundResource(R.drawable.background_un_shape);
            replacePhoneCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    /**
     * 正则验证手机号
     */
    public boolean isRegisterMobileNO() {
        Pattern patternRegisterMobile = Pattern.compile(GlobalConsts.REGEX_MOBILE, Pattern.CASE_INSENSITIVE);
        Matcher matcherRegisterMobile = patternRegisterMobile.matcher(replacePhoneString);
        return matcherRegisterMobile.matches();
    }
}