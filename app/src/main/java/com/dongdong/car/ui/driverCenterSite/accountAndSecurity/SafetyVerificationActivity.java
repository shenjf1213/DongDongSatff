package com.dongdong.car.ui.driverCenterSite.accountAndSecurity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.changePhone.ChangePhoneRequest;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/8.
 */

class SafetyVerificationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView safetyVerificationBack; // 返回
    private EditText safetyVerificationPhoneEt; // 手机号输入框
    private Button safetyVerificationCode; // 点击获取验证码
    private EditText safetyVerificationCodeEt; // 验证码输入框
    private Button safetyVerificationBtn; // 确认修改手机号

    private boolean show = false;
    private TimeCount time; // 点击获取验证码后显示60秒倒计时
    private String safetyVerificationPhoneString;
    private String safetyVerificationCodeString;

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private String id;
    private String oldPhoneString; // 旧的手机号
    private String code = "";

    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.CHANGE_PHONE_HANDLER:
                    hideDialogByProgress();
                    ChangePhoneRequest changePhoneRequest = (ChangePhoneRequest) msg.obj;
                    String changePhoneStr = changePhoneRequest.getMsg();
                    if (TextUtils.equals("设置手机号", changePhoneStr)) {
                        dialog = new DialogByOneButton(SafetyVerificationActivity.this, "提示", "手机号码修改成功", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                                SafetyVerificationActivity.this.finish();
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_verification_layout);

        dialogByProgress = new DialogByProgress(SafetyVerificationActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        time = new TimeCount(60000, 1000); // 构造CountDownTimer对象

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        oldPhoneString = intent.getStringExtra("OLD_PHONE");

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        safetyVerificationBack = (ImageView) findViewById(R.id.safety_verification_back); // 返回
        safetyVerificationPhoneEt = (EditText) findViewById(R.id.safety_verification_phone_et); // 手机号输入框
        safetyVerificationCode = (Button) findViewById(R.id.safety_verification_code); // 点击获取验证码
        safetyVerificationCodeEt = (EditText) findViewById(R.id.safety_verification_code_et); // 验证码输入框
        safetyVerificationBtn = (Button) findViewById(R.id.safety_verification_btn); // 确认修改手机号
    }

    /**
     * 监听
     */
    private void initListener() {
        safetyVerificationBack.setOnClickListener(this);
        safetyVerificationCode.setOnClickListener(this);
        safetyVerificationBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safety_verification_back: // 返回
                SafetyVerificationActivity.this.finish();
                break;

            case R.id.safety_verification_code: // 获取验证码
                safetyVerificationPhoneString = safetyVerificationPhoneEt.getText().toString().trim();
                if (!TextUtils.isEmpty(safetyVerificationPhoneString)) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isRegisterMobileNO() == false) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    time.start();
                    Request request = new Request.Builder().url(GlobalConsts.REGISTER_CODE_URL + safetyVerificationPhoneString).build();
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

            case R.id.safety_verification_btn: // 确认修改
                safetyVerificationPhoneString = safetyVerificationPhoneEt.getText().toString().trim();
                safetyVerificationCodeString = safetyVerificationCodeEt.getText().toString().trim();
                if (!TextUtils.isEmpty(safetyVerificationPhoneString)) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isRegisterMobileNO() == false) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(SafetyVerificationActivity.this, "请点击获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!TextUtils.isEmpty(safetyVerificationCodeString)) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!TextUtils.equals(code, safetyVerificationCodeString)) {
                    Toast.makeText(SafetyVerificationActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("id", id);
                    builder.add("telephone", oldPhoneString);
                    builder.add("newTelephone", safetyVerificationPhoneString);
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.CHANGE_PHONE_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Gson changePhoneGson = new Gson();
                            ChangePhoneRequest changePhoneRequest = changePhoneGson.fromJson(bodyString, ChangePhoneRequest.class);
                            Message changePhoneMsg = Message.obtain();
                            changePhoneMsg.what = GlobalConsts.CHANGE_PHONE_HANDLER;
                            changePhoneMsg.obj = changePhoneRequest;
                            handler.sendMessage(changePhoneMsg);
                        }
                    });
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
            safetyVerificationCode.setText("重新验证");
            safetyVerificationCode.setClickable(true);
            safetyVerificationCode.setTextColor(ContextCompat.getColor(SafetyVerificationActivity.this, R.color.white));
            safetyVerificationCode.setBackgroundResource(R.drawable.background_shape);
        }

        @Override
        public void onTick(long millisUntilFinished) { // 计时过程显示
            safetyVerificationCode.setClickable(false);
            safetyVerificationCode.setTextColor(ContextCompat.getColor(SafetyVerificationActivity.this, R.color.black));
            safetyVerificationCode.setBackgroundResource(R.drawable.background_un_shape);
            safetyVerificationCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    /**
     * 正则验证手机号
     */
    public boolean isRegisterMobileNO() {
        Pattern patternRegisterMobile = Pattern.compile(GlobalConsts.REGEX_MOBILE, Pattern.CASE_INSENSITIVE);
        Matcher matcherRegisterMobile = patternRegisterMobile.matcher(safetyVerificationPhoneString);
        return matcherRegisterMobile.matches();
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
