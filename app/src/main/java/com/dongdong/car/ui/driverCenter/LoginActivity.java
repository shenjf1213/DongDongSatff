package com.dongdong.car.ui.driverCenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdong.car.MainActivity;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.login.LoginRequest;
import com.dongdong.car.entity.login.LoginResult;
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
 * Created by 沈 on 2017/6/2.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText loginUserNameEt, loginPasswordEt;
    private ImageView loginBack, loginUserNameClear, loginPasswordShow;
    private TextView forgetPasswordTv;
    private Button loginBtn;

    private String userName; // 获取输入的手机号
    private String loginPassword; // 获取输入的密码
    private boolean show = false;

    private String id;
    private String type;
    private String name;
    private String phone;
    private String loginPas;

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.LOGIN_HANDLER:
                    LoginRequest loginRequest = (LoginRequest) msg.obj;
                    dialogByProgress.dismiss();
                    String loginState = String.valueOf(loginRequest.getIsSucess());
                    if (loginState.equals("true")) {
                        LoginResult loginResult = loginRequest.getData();
                        id = loginResult.getSatffID(); // 获取当前用户登录的id
                        type = loginResult.getStaffType(); // 获取当前用户登录的类型 0为员工，1为管理者
                        saveUserMsg(id, type); // 保存个人信息
                        if (type.equals("0")) { // 登录类型为员工
                            dialog = new DialogByOneButton(LoginActivity.this, "提示", "登录成功", "确定");
                            dialog.show();
                            dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                                @Override
                                public void doPositive() {
                                    dialog.dismiss();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                            });
                            break;
                        }
                    } else if (loginState.equals("false")) {
                        dialog = new DialogByOneButton(LoginActivity.this, "提示", "账号或密码错误，请检查后重新登录", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
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
        setContentView(R.layout.activity_login_layout);

        dialogByProgress = new DialogByProgress(LoginActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        loginBack = (ImageView) findViewById(R.id.login_back);
        loginUserNameEt = (EditText) findViewById(R.id.login_username_et);
        loginPasswordEt = (EditText) findViewById(R.id.login_password_et);
        loginUserNameClear = (ImageView) findViewById(R.id.login_username_clear);
        loginPasswordShow = (ImageView) findViewById(R.id.login_password_show);
        forgetPasswordTv = (TextView) findViewById(R.id.forget_password_tv);
        loginBtn = (Button) findViewById(R.id.login_btn);

        /**
         * 账号输入框动态监听
         */
        loginUserNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginUserNameClear.setVisibility(View.INVISIBLE);
                } else {
                    loginUserNameClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 密码输入框动态监听
         */
        loginPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    loginPasswordShow.setVisibility(View.INVISIBLE);
                } else {
                    loginPasswordShow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 监听
     */
    private void initListener() {
        loginBack.setOnClickListener(this);
        loginUserNameClear.setOnClickListener(this);
        loginPasswordShow.setOnClickListener(this);
        forgetPasswordTv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                LoginActivity.this.finish();
                break;

            case R.id.login_username_clear:
                loginUserNameEt.setText("");
                break;

            case R.id.login_password_show:
                doShow();
                break;

            case R.id.forget_password_tv:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;

            case R.id.login_btn:
                loginSubmit();
                break;
        }
    }

    /**
     * 点击登录
     */
    private void loginSubmit() {
        userName = loginUserNameEt.getText().toString().trim();
        loginPassword = loginPasswordEt.getText().toString().trim();
        if (userName.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (loginPassword.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            dialogByProgress.show();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userName", userName);
            builder.add("password", loginPassword);
            FormBody body = builder.build();
            Request request = new Request.Builder().url(GlobalConsts.LOGIN_URL).post(body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String bodyString = response.body().string();
                    Log.d("test", "登录----" + bodyString.toString());
                    Gson loginGson = new Gson();
                    LoginRequest loginRequest = loginGson.fromJson(bodyString, LoginRequest.class);
                    Message loginMsg = Message.obtain();
                    loginMsg.what = GlobalConsts.LOGIN_HANDLER;
                    loginMsg.obj = loginRequest;
                    handler.sendMessage(loginMsg);
                }
            });
        }
    }

    /**
     * 保存当前登录者的信息
     *
     * @param id
     * @param type
     */
    private void saveUserMsg(String id, String type) {
        SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("user_info", 0).edit();
        editor.putString("uid", id);
        editor.putString("type", type);
        editor.apply();
    }

    /**
     * 隐藏或者显示密码
     */
    private void doShow() {
        if (show) {
            // 隐藏密码
            loginPasswordShow.setImageResource(R.mipmap.btn_passno);
            loginPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show = false;
        } else {
            // 显示密码
            loginPasswordShow.setImageResource(R.mipmap.btn_pass);
            loginPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show = true;
        }
    }
}
