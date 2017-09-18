package com.dongdong.car.ui.myWallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.toatlData.TotalRequest;
import com.dongdong.car.entity.withdraw.WithdrawRequest;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.DialogByTwoButton;
import com.dongdong.car.util.GlobalConsts;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/5.
 */

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {

    private String id;
    private String userTotal, userBank, userBankCard;
    private ImageView withdrawBack;
    private ImageView withdrawDescription; // 提现说明
    private TextView withdrawBackTypeTv; // 到账银行
    private TextView withdrawBackCard; // 到账银行卡号
    private TextView withdrawBackTypeTv1; // 显示到账银行
    private TextView withdrawBackTypeTv2; // 手续费率
    private EditText withdrawMoneyEt; // 提现金额的输入框
    private String withdrawMoneyStr = ""; // 输入框中输入的金额
    private TextView withdrawMoneyTv; // 当前账号的全部金额
    private TextView allPresentTv; // 提现全部金额
    private TextView withdrawTv; // 提现按钮

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog1;
    private DialogByTwoButton dialog2;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.WITHDRAW_TOTAL_HANDLER:
                    WithdrawRequest withdrawRequest = (WithdrawRequest) msg.obj;
                    hideDialogByProgress();
                    String withdrawState = withdrawRequest.getIsSucess();
                    if (withdrawState.equals("true")) {
                        dialog1 = new DialogByOneButton(WithdrawActivity.this, "提示", "资产提现已提交申请，正在审核中，请您耐心等候", "确定");
                        dialog1.show();
                        dialog1.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog1.dismiss();
                                getTotalData(); // 获取资产余额
                            }
                        });
                    }
                    break;

                case GlobalConsts.TOTAL_DATA_HANDLER:
                    TotalRequest totalRequest = (TotalRequest) msg.obj;
                    hideDialogByProgress();
                    String totalState = totalRequest.getIsSucess();
                    if (totalState.equals("true")) {
                        TotalRequest.TotalResult totalResult = totalRequest.getData();
                        withdrawMoneyTv.setText(totalResult.getPayTotal());
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_layout);

        dialogByProgress = new DialogByProgress(WithdrawActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SharedPreferences spf = WithdrawActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        Intent intent = getIntent();
        userTotal = intent.getStringExtra("TOTAL_ASSETS");
        userBank = intent.getStringExtra("BANK_NAME");
        userBankCard = intent.getStringExtra("BANK_CARD");

        initView();
        initListener();
        initDate();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        withdrawBack = (ImageView) findViewById(R.id.withdraw_back);
        withdrawDescription = (ImageView) findViewById(R.id.withdraw_description); // 提现说明
        withdrawBackTypeTv = (TextView) findViewById(R.id.withdraw_back_type_tv); // 到账银行
        withdrawBackCard = (TextView) findViewById(R.id.withdraw_back_card); // 银行号
        withdrawBackTypeTv1 = (TextView) findViewById(R.id.withdraw_back_type_tv1); // 显示到账银行
        withdrawBackTypeTv2 = (TextView) findViewById(R.id.withdraw_back_type_tv2); // 手续费率
        withdrawMoneyTv = (TextView) findViewById(R.id.withdraw_money_tv); // 当前账号的全部金额
        allPresentTv = (TextView) findViewById(R.id.all_present_tv); // 提现全部金额
        withdrawTv = (TextView) findViewById(R.id.withdraw_tv); // 提现按钮

        withdrawMoneyEt = (EditText) findViewById(R.id.withdraw_money_et); // 提现金额的输入框
        SpannableString ss = new SpannableString("请输入需要提现的金额");
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        withdrawMoneyEt.setHint(new SpannableString(ss));
        withdrawMoneyEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        withdrawMoneyEt.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    /**
     * 监听
     */
    private void initListener() {
        withdrawBack.setOnClickListener(this);
        withdrawDescription.setOnClickListener(this);
        allPresentTv.setOnClickListener(this);
        withdrawTv.setOnClickListener(this);
    }

    /**
     * 获取数据并赋值
     */
    private void initDate() {
        withdrawBackTypeTv.setText(userBank);
        withdrawBackCard.setText(userBankCard.substring(userBankCard.length() - 4, userBankCard.length()));
        withdrawBackTypeTv1.setText(userBank);
        withdrawMoneyTv.setText(userTotal);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_back:
                WithdrawActivity.this.finish();
                break;

            case R.id.withdraw_description: // 提现说明

                break;

            case R.id.all_present_tv: // 提现所有
                withdrawMoneyStr = withdrawMoneyTv.getText().toString().trim();
                dialog2 = new DialogByTwoButton(WithdrawActivity.this, "提示", "确定要将钱包中的全部金额提现至绑定的银行卡吗？", "取消", "确定");
                dialog2.show();
                dialog2.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                    @Override
                    public void doNegative() {
                        dialog2.dismiss();
                    }

                    @Override
                    public void doPositive() {
                        dialog2.dismiss();
                        withdrawTotal(withdrawMoneyStr);
                    }
                });
                break;

            case R.id.withdraw_tv: // 提现
                withdrawMoneyStr = withdrawMoneyEt.getText().toString().trim();
                double m1 = Double.parseDouble(String.valueOf(withdrawMoneyStr)); // 提现的金额
                double m2 = Double.parseDouble(String.valueOf(withdrawMoneyTv.getText().toString().trim())); // 总资产
                if (withdrawMoneyStr.equals("")) {
                    Toast.makeText(WithdrawActivity.this, "请输入需要提现的金额", Toast.LENGTH_SHORT).show();
                    return;
                } else if (m1 > m2) {
                    Toast.makeText(WithdrawActivity.this, "提现金额不能大于总资产", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialog2 = new DialogByTwoButton(WithdrawActivity.this, "提示", "确定要将钱包中的余额提现至绑定的银行卡吗？", "取消", "确定");
                    dialog2.show();
                    dialog2.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
                        @Override
                        public void doNegative() {
                            dialog2.dismiss();
                        }

                        @Override
                        public void doPositive() {
                            dialog2.dismiss();
                            withdrawTotal(withdrawMoneyStr);
                        }
                    });
                }
                break;
        }
    }

    /**
     * 申请资产提现
     *
     * @param withdrawMoneyStr
     */
    private void withdrawTotal(String withdrawMoneyStr) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/ApproveRedpacket?staffId=" + id + "&money=" + withdrawMoneyStr).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "提现申请---" + bodyString.toString());
                Gson withdrawGson = new Gson();
                WithdrawRequest withdrawRequest = withdrawGson.fromJson(bodyString, WithdrawRequest.class);
                Message withdrawMsg = Message.obtain();
                withdrawMsg.what = GlobalConsts.WITHDRAW_TOTAL_HANDLER;
                withdrawMsg.obj = withdrawRequest;
                handler.sendMessage(withdrawMsg);
            }
        });
    }

    /**
     * 获取资产余额
     */
    private void getTotalData() {
        dialogByProgress.show();
        Request totalRequest = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/GetPayTotal?staffId=" + id).build();
        okHttpClient.newCall(totalRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Gson totalGson = new Gson();
                TotalRequest totalRequest = totalGson.fromJson(bodyString, TotalRequest.class);
                Message totalMsg = Message.obtain();
                totalMsg.what = GlobalConsts.TOTAL_DATA_HANDLER;
                totalMsg.obj = totalRequest;
                handler.sendMessage(totalMsg);
            }
        });
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
