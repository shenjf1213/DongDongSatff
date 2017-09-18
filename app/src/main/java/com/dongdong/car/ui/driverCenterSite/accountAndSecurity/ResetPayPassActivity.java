package com.dongdong.car.ui.driverCenterSite.accountAndSecurity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.verifyTransactionPas.verifyTransactionPasRequest;
import com.dongdong.car.ui.myWallet.TransactionPasActivity;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/8.
 */

public class ResetPayPassActivity extends BaseActivity implements View.OnClickListener {

    private ImageView resetPayPasBack;
    private TextView resetPayPasTv1, resetPayPasTv2, resetPayPasTv3, resetPayPasTv4, resetPayPasTv5, resetPayPasTv6;
    private EditText resetPayPasEt;
    private Button resetPayPasBtn;
    private String payPassKey = "";
    private String oldPayPassStr = ""; // 旧的支付密码

    private SharedPreferences spf;
    private String id;
    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.VERIFY_TRANSACTION_PASS_HANDLER:
                    verifyTransactionPasRequest verifyTransactionPasRequest = (com.dongdong.car.entity.verifyTransactionPas.verifyTransactionPasRequest) msg.obj;
                    hideDialogByProgress();
                    String verifyTransactionPasState = verifyTransactionPasRequest.getIsSucess();
                    if (verifyTransactionPasState.equals("true")) {
                        startActivity(new Intent(ResetPayPassActivity.this, TransactionPasActivity.class));
                        ResetPayPassActivity.this.finish();
                    } else {
                        dialog = new DialogByOneButton(ResetPayPassActivity.this, "提示", "交易密码输入错误，请确认后重新输入", "确定");
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
        setContentView(R.layout.activity_reset_pay_pass_layout);

        dialogByProgress = new DialogByProgress(ResetPayPassActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        spf = ResetPayPassActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        initView();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        resetPayPasBack = (ImageView) findViewById(R.id.reset_pay_pas_back);
        resetPayPasTv1 = (TextView) findViewById(R.id.reset_pay_pas_tv1);
        resetPayPasTv2 = (TextView) findViewById(R.id.reset_pay_pas_tv2);
        resetPayPasTv3 = (TextView) findViewById(R.id.reset_pay_pas_tv3);
        resetPayPasTv4 = (TextView) findViewById(R.id.reset_pay_pas_tv4);
        resetPayPasTv5 = (TextView) findViewById(R.id.reset_pay_pas_tv5);
        resetPayPasTv6 = (TextView) findViewById(R.id.reset_pay_pas_tv6);
        resetPayPasBtn = (Button) findViewById(R.id.reset_pay_pas_btn);

        resetPayPasEt = (EditText) findViewById(R.id.reset_pay_pas_et);
        resetPayPasEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                payPassKey = s.toString().trim();
                setConKye(payPassKey);
            }
        });
    }

    /**
     * 动态获取数值
     *
     * @param payPassKey
     */
    private void setConKye(String payPassKey) {
        char[] payPassArr = payPassKey.toCharArray();
        resetPayPasTv1.setText("");
        resetPayPasTv2.setText("");
        resetPayPasTv3.setText("");
        resetPayPasTv4.setText("");
        resetPayPasTv5.setText("");
        resetPayPasTv6.setText("");
        for (int i = 0; i < payPassArr.length; i++) {
            if (i == 0) {
                resetPayPasTv1.setText(String.valueOf(payPassArr[0]));
            } else if (i == 1) {
                resetPayPasTv2.setText(String.valueOf(payPassArr[1]));
            } else if (i == 2) {
                resetPayPasTv3.setText(String.valueOf(payPassArr[2]));
            } else if (i == 3) {
                resetPayPasTv4.setText(String.valueOf(payPassArr[3]));
            } else if (i == 4) {
                resetPayPasTv5.setText(String.valueOf(payPassArr[4]));
            } else if (i == 5) {
                resetPayPasTv6.setText(String.valueOf(payPassArr[5]));
            }
        }
    }

    /**
     * 监听
     */
    private void initListener() {
        resetPayPasBack.setOnClickListener(this);
        resetPayPasBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_pay_pas_back: // 返回
                ResetPayPassActivity.this.finish();
                break;

            case R.id.reset_pay_pas_btn: // 下一步
                if (payPassKey.equals("")) {
                    Toast.makeText(ResetPayPassActivity.this, "请输入旧的支付密码，以验证身份", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    verifyTransactionPassword(); // 验证旧的交易密码
                }
                break;
        }
    }

    /**
     * 验证交易密码是否正确
     */
    private void verifyTransactionPassword() {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/IsStaffPayLogin?staffId=" + id + "&pwd=" + payPassKey).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson verifyTransactionPasGson = new Gson();
                verifyTransactionPasRequest verifyTransactionPasRequest = verifyTransactionPasGson.fromJson(bodyString, com.dongdong.car.entity.verifyTransactionPas.verifyTransactionPasRequest.class);
                Message verifyTransactionPasMsg = Message.obtain();
                verifyTransactionPasMsg.what = GlobalConsts.VERIFY_TRANSACTION_PASS_HANDLER;
                verifyTransactionPasMsg.obj = verifyTransactionPasRequest;
                handler.sendMessage(verifyTransactionPasMsg);
            }
        });
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
