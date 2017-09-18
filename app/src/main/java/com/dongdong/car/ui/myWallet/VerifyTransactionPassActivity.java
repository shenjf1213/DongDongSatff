package com.dongdong.car.ui.myWallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.verifyTransactionPas.verifyTransactionPasRequest;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;
import com.meiyou.jet.annotation.JFindView;
import com.meiyou.jet.annotation.JFindViewOnClick;
import com.meiyou.jet.process.Jet;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/30.
 */

public class VerifyTransactionPassActivity extends BaseActivity implements View.OnClickListener {

    private String id;
    private String userTotal, userBank, userBankCard;
    private String editPassStr = ""; // 输入的密码

    @JFindView(R.id.verify_transaction_pas_back)
    @JFindViewOnClick(R.id.verify_transaction_pas_back)
    private ImageView verifyTransactionPasBack;
    @JFindView(R.id.verify_transaction_pas_new_tv1)
    private TextView verifyTransactionPasNewTv1;
    @JFindView(R.id.verify_transaction_pas_new_tv2)
    private TextView verifyTransactionPasNewTv2;
    @JFindView(R.id.verify_transaction_pas_new_tv3)
    private TextView verifyTransactionPasNewTv3;
    @JFindView(R.id.verify_transaction_pas_new_tv4)
    private TextView verifyTransactionPasNewTv4;
    @JFindView(R.id.verify_transaction_pas_new_tv5)
    private TextView verifyTransactionPasNewTv5;
    @JFindView(R.id.verify_transaction_pas_new_tv6)
    private TextView verifyTransactionPasNewTv6;
    @JFindView(R.id.verify_transaction_pas_et)
    @JFindViewOnClick(R.id.verify_transaction_pas_et)
    private EditText verifyTransactionPasEt;
    @JFindView(R.id.verify_transaction_pas_btn)
    @JFindViewOnClick(R.id.verify_transaction_pas_btn)
    private Button verifyTransactionPasBtn;

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
                        Intent intent = new Intent(VerifyTransactionPassActivity.this, WithdrawActivity.class);
                        intent.putExtra("TOTAL_ASSETS", userTotal);
                        intent.putExtra("BANK_NAME", userBank);
                        intent.putExtra("BANK_CARD", userBankCard);
                        startActivity(intent);
                        VerifyTransactionPassActivity.this.finish();
                    } else {
                        dialog = new DialogByOneButton(VerifyTransactionPassActivity.this, "提示", "交易密码输入错误，请确认后重新输入", "确定");
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
        setContentView(R.layout.activity_verify_transaction_pas_layout);
        Jet.bind(this);

        dialogByProgress = new DialogByProgress(VerifyTransactionPassActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SharedPreferences spf = VerifyTransactionPassActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        Intent intent = getIntent();
        userTotal = intent.getStringExtra("TOTAL_ASSETS");
        userBank = intent.getStringExtra("BANK_NAME");
        userBankCard = intent.getStringExtra("BANK_CARD");

        verifyTransactionPasEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editPassStr = s.toString().trim();
                verifyPass(editPassStr);
            }
        });
    }

    /**
     * 获取输入的密码
     *
     * @param editPassStr
     */
    private void verifyPass(String editPassStr) {
        char[] verifyPassArr = editPassStr.toCharArray();
        verifyTransactionPasNewTv1.setText("");
        verifyTransactionPasNewTv2.setText("");
        verifyTransactionPasNewTv3.setText("");
        verifyTransactionPasNewTv4.setText("");
        verifyTransactionPasNewTv5.setText("");
        verifyTransactionPasNewTv6.setText("");
        for (int i = 0; i < verifyPassArr.length; i++) {
            if (i == 0) {
                verifyTransactionPasNewTv1.setText(String.valueOf(verifyPassArr[0]));
            } else if (i == 1) {
                verifyTransactionPasNewTv2.setText(String.valueOf(verifyPassArr[1]));
            } else if (i == 2) {
                verifyTransactionPasNewTv3.setText(String.valueOf(verifyPassArr[2]));
            } else if (i == 3) {
                verifyTransactionPasNewTv4.setText(String.valueOf(verifyPassArr[3]));
            } else if (i == 4) {
                verifyTransactionPasNewTv5.setText(String.valueOf(verifyPassArr[4]));
            } else if (i == 5) {
                verifyTransactionPasNewTv6.setText(String.valueOf(verifyPassArr[5]));
            }
        }
    }

    /**
     * 监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_transaction_pas_back: // 返回
                VerifyTransactionPassActivity.this.finish();
                break;

            case R.id.verify_transaction_pas_btn: // 确定
                if (editPassStr.equals("")) {
                    Toast.makeText(VerifyTransactionPassActivity.this, "请输入交易密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (editPassStr.length() < 6) {
                    Toast.makeText(VerifyTransactionPassActivity.this, "请输入六位数的交易密码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/IsStaffPayLogin?staffId=" + id + "&pwd=" + editPassStr).build();
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
                break;
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
