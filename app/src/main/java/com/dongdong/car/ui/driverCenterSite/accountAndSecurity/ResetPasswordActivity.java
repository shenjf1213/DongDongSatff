package com.dongdong.car.ui.driverCenterSite.accountAndSecurity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.resetPassword.ResetPassRequest;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/8.
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView resetPasswordBack; // 返回
    private EditText oldPasswordEt; // 旧密码输入框
    private ImageView oldPasswordShow; // 显示旧密码
    private EditText newPasswordEt; // 新密码输入框
    private ImageView newPasswordShow; // 显示新密码
    private EditText confirmPasswordEt; // 确认密码输入框
    private ImageView confirmPasswordShow; // 显示确认密码
    private Button resetPasswordBtn; // 确认修改

    private String oldPassword, newPassword, confirmPassword;
    private boolean show = false;
    private SharedPreferences spf;
    private String id;

    private DialogByOneButton dialog1;
    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.RESET_PASSWORD_HANDLER:
                    hideDialogByProgress();
                    ResetPassRequest resetPassRequest = (ResetPassRequest) msg.obj;
                    String resetPassStr = resetPassRequest.getMsg();
                    if (resetPassStr.equals("设置密码")) {
                        dialog1 = new DialogByOneButton(ResetPasswordActivity.this, "提示", "密码修改成功", "确定");
                        dialog1.show();
                        dialog1.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog1.dismiss();
                                ResetPasswordActivity.this.finish();
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
        setContentView(R.layout.activity_reset_pasword_layout);

        dialogByProgress = new DialogByProgress(ResetPasswordActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        spf = ResetPasswordActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        initView();
        initListener();

    }

    /**
     * 初始化
     */
    private void initView() {
        resetPasswordBack = (ImageView) findViewById(R.id.reset_password_back); // 返回
        oldPasswordEt = (EditText) findViewById(R.id.old_password_et); // 旧密码输入框
        oldPasswordShow = (ImageView) findViewById(R.id.old_password_show); // 显示旧密码
        newPasswordEt = (EditText) findViewById(R.id.new_password_et); // 新密码输入框
        newPasswordShow = (ImageView) findViewById(R.id.new_password_show); // 显示新密码
        confirmPasswordEt = (EditText) findViewById(R.id.confirm_password_et); // 确认密码输入框
        confirmPasswordShow = (ImageView) findViewById(R.id.confirm_password_show); // 显示确认密码
        resetPasswordBtn = (Button) findViewById(R.id.reset_password_btn); // 确认修改
    }

    /**
     * 监听
     */
    private void initListener() {
        resetPasswordBack.setOnClickListener(this);
        oldPasswordShow.setOnClickListener(this);
        newPasswordShow.setOnClickListener(this);
        confirmPasswordShow.setOnClickListener(this);
        resetPasswordBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_password_back: // 返回
                ResetPasswordActivity.this.finish();
                break;

            case R.id.old_password_show: // 显示旧密码
                showOrHidePassWord(oldPasswordShow, oldPasswordEt);
                break;

            case R.id.new_password_show: // 显示新密码
                showOrHidePassWord(newPasswordShow, newPasswordEt);
                break;

            case R.id.confirm_password_show: // 显示需要确认的新密码
                showOrHidePassWord(confirmPasswordShow, confirmPasswordEt);
                break;

            case R.id.reset_password_btn: // 确认修改密码
                oldPassword = oldPasswordEt.getText().toString().trim();
                newPassword = newPasswordEt.getText().toString().trim();
                confirmPassword = confirmPasswordEt.getText().toString().trim();
                if ("".equals(oldPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(newPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (JudgeOldPassword() == true) {
                    Toast.makeText(ResetPasswordActivity.this, "旧密码与新密码不能一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (JudgePassword() == false) {
                    Toast.makeText(ResetPasswordActivity.this, "新密码与确认密码请保持一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("id", id);
                    builder.add("oldPwd", oldPassword);
                    builder.add("newPwd", newPassword);
                    builder.add("crmPwd", confirmPassword);
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.RESET_PASSWORD_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Gson resetPassGson = new Gson();
                            ResetPassRequest resetPassRequest = resetPassGson.fromJson(bodyString, ResetPassRequest.class);
                            Message resetPassMsg = Message.obtain();
                            resetPassMsg.what = GlobalConsts.RESET_PASSWORD_HANDLER;
                            resetPassMsg.obj = resetPassRequest;
                            handler.sendMessage(resetPassMsg);
                        }
                    });
                }
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
     * 判断旧密码与新密码是否一致
     */
    public boolean JudgeOldPassword() {
        oldPassword = oldPasswordEt.getText().toString().trim();
        newPassword = newPasswordEt.getText().toString().trim();
        return newPassword.equals(oldPassword);
    }

    /**
     * 判断新密码与确认密码是否相等
     */
    public boolean JudgePassword() {
        newPassword = newPasswordEt.getText().toString().trim();
        confirmPassword = confirmPasswordEt.getText().toString().trim();
        return newPassword.equals(confirmPassword);
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
