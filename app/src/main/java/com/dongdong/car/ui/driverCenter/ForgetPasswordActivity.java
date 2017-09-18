package com.dongdong.car.ui.driverCenter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;

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
 * Created by 沈 on 2017/5/12.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView forgetPasswordBack; // 返回
    private EditText forgetPasswordPhoneEt; // 手机号输入框
    private Button forgetPasswordCode; // 点击获取验证码
    private EditText forgetPasswordCodeEt; // 验证码输入框
    private EditText forgetPasswordNewEt; // 新密码输入框
    private EditText forgetPasswordConfirmEt; // 确认密码输入框
    private ImageView forgetPasswordConfirmShow; // 确认密码显示
    private ImageView forgetPasswordNewShow; // 新密码显示
    private Button forgetPasswordBtn; // 确认修改密码
    private String forgetPhoneStr, forgetNewPasswordStr, forgetConfirmPasswordStr, forgetCodeStr;
    private TimeCount time; // 点击获取验证码后显示60秒倒计时
    private String code = "";

    private boolean show = false;
    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_froget_password_layout);

        time = new TimeCount(60000, 1000); // 构造CountDownTimer对象

        dialogByProgress = new DialogByProgress(ForgetPasswordActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        forgetPasswordBack = (ImageView) findViewById(R.id.forget_password_back); // 返回
        forgetPasswordPhoneEt = (EditText) findViewById(R.id.forget_password_phone_et); // 手机号输入框
        forgetPasswordCode = (Button) findViewById(R.id.forget_password_code); // 点击获取验证码
        forgetPasswordCodeEt = (EditText) findViewById(R.id.forget_password_code_et); // 验证码输入框
        forgetPasswordNewEt = (EditText) findViewById(R.id.forget_password_new_et); // 新密码输入框
        forgetPasswordConfirmEt = (EditText) findViewById(R.id.forget_password_confirm_et); // 确认密码输入框
        forgetPasswordConfirmShow = (ImageView) findViewById(R.id.forget_password_confirm_show); // 确认密码显示
        forgetPasswordNewShow = (ImageView) findViewById(R.id.forget_password_new_show); // 新密码显示
        forgetPasswordBtn = (Button) findViewById(R.id.forget_password_btn); // 确认修改密码
    }

    /**
     * 监听
     */
    private void initListener() {
        forgetPasswordBack.setOnClickListener(this);
        forgetPasswordCode.setOnClickListener(this);
        forgetPasswordConfirmShow.setOnClickListener(this);
        forgetPasswordNewShow.setOnClickListener(this);
        forgetPasswordBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password_back:
                ForgetPasswordActivity.this.finish();
                break;

            case R.id.forget_password_code: // 点击获取验证码
                forgetPhoneStr = forgetPasswordPhoneEt.getText().toString().trim();
                if (forgetPhoneStr.equals("")) {
                    forgetPasswordCode.setClickable(true);
                    forgetPasswordCode.setBackgroundResource(R.drawable.background_shape);
                    Toast.makeText(ForgetPasswordActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isRegisterMobileNO() == false) {
                    forgetPasswordCode.setClickable(true);
                    forgetPasswordCode.setBackgroundResource(R.drawable.background_shape);
                    Toast.makeText(ForgetPasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    time.start();
                    Request request = new Request.Builder().url(GlobalConsts.REGISTER_CODE_URL + forgetPhoneStr).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            code = bodyString.substring(1, bodyString.length() - 1);
                            Log.d("test", "修改密码的验证码---" + code.toString());
                        }
                    });
                }
                break;

            case R.id.forget_password_new_show:
                showOrHidePassWord(forgetPasswordNewShow, forgetPasswordNewEt);
                break;

            case R.id.forget_password_confirm_show:
                showOrHidePassWord(forgetPasswordConfirmShow, forgetPasswordConfirmEt);
                break;

            case R.id.forget_password_btn:
                forgetPhoneStr = forgetPasswordPhoneEt.getText().toString().trim();
                forgetCodeStr = forgetPasswordCodeEt.getText().toString().trim();
                forgetNewPasswordStr = forgetPasswordNewEt.getText().toString().trim();
                forgetConfirmPasswordStr = forgetPasswordConfirmEt.getText().toString().trim();
                if (forgetPhoneStr.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (isRegisterMobileNO() == false) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (forgetCodeStr.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (code.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请点击获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!code.equals(forgetCodeStr)) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (forgetNewPasswordStr.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入新的密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (forgetConfirmPasswordStr.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (JudgePassword() == false) {
                    Toast.makeText(ForgetPasswordActivity.this, "新密码与确认密码请保持一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("telephone", forgetPhoneStr);
                    builder.add("code", forgetCodeStr);
                    builder.add("password", forgetConfirmPasswordStr);
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.FORGET_PASSWORD_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Log.d("test", "修改密码----" + bodyString.toString());

                        }
                    });
                }
                break;
        }

    }

    /**
     * 隐藏或显示密码
     *
     * @param iv
     * @param et
     */
    private void showOrHidePassWord(ImageView iv, EditText et) {
        if (show) {
            iv.setImageResource(R.mipmap.btn_passno);
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show = false;
        } else {
            iv.setImageResource(R.mipmap.btn_pass);
            et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show = true;
        }
    }

    /**
     * 正则验证手机号
     */
    public boolean isRegisterMobileNO() {
        Pattern patternRegisterMobile = Pattern.compile(GlobalConsts.REGEX_MOBILE, Pattern.CASE_INSENSITIVE);
        Matcher matcherRegisterMobile = patternRegisterMobile.matcher(forgetPhoneStr);
        return matcherRegisterMobile.matches();
    }

    /**
     * 判断新密码与确认密码是否相等
     */
    public boolean JudgePassword() {
        forgetNewPasswordStr = forgetPasswordNewEt.getText().toString().trim();
        forgetConfirmPasswordStr = forgetPasswordConfirmEt.getText().toString().trim();
        return forgetNewPasswordStr.equals(forgetConfirmPasswordStr);
    }

    /**
     * 倒计时的内部类
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); // 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            forgetPasswordCode.setText("重新验证");
            forgetPasswordCode.setClickable(true);
            forgetPasswordCode.setTextColor(ContextCompat.getColor(ForgetPasswordActivity.this, R.color.white));
            forgetPasswordCode.setBackgroundResource(R.drawable.background_shape);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            forgetPasswordCode.setClickable(false);
            forgetPasswordCode.setBackgroundResource(R.drawable.background_un_shape);
            forgetPasswordCode.setTextColor(ContextCompat.getColor(ForgetPasswordActivity.this, R.color.black));
            forgetPasswordCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
