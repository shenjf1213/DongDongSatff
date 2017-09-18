package com.dongdong.car.ui.myWallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.incomeMsg.IncomeRequest;
import com.dongdong.car.entity.incomeMsg.IncomeResult;
import com.dongdong.car.entity.withdrawMsg.WithdrawMsgRequest;
import com.dongdong.car.entity.withdrawMsg.WithdrawMsgResult;
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
 * Created by 沈 on 2017/6/13.
 */

public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener {

    @JFindView(R.id.transaction_record_back)
    @JFindViewOnClick(R.id.transaction_record_back)
    private ImageView transactionRecordBack; // 返回
    @JFindView(R.id.transaction_record_type_tv1)
    private TextView transactionRecordTypeTv1; // 显示交易记录的类型
    @JFindView(R.id.transaction_record_withdraw_ll3)
    private LinearLayout transactionRecordWithdrawLl3; // 提现的页面布局
    @JFindView(R.id.transaction_record_withdraw_money_1)
    private TextView transactionRecordWithdrawMoney1; // 出账金额
    @JFindView(R.id.transaction_record_withdraw_money_2)
    private TextView transactionRecordWithdrawMoney2; // 提现金额
    @JFindView(R.id.transaction_record_withdraw_money_3)
    private TextView transactionRecordWithdrawMoney3; // 手续费
    @JFindView(R.id.transaction_record_withdraw_state)
    private TextView transactionRecordWithdrawState; // 当前提现的状态
    @JFindView(R.id.transaction_record_withdraw_time1)
    private TextView transactionRecordWithdrawTime1; // 提现申请的日期
    @JFindView(R.id.transaction_record_withdraw_time2)
    private TextView transactionRecordWithdrawTime2; // 提现申请的时间
    @JFindView(R.id.transaction_record_withdraw_time3)
    private TextView transactionRecordWithdrawTime3; // 提现到账的日期
    @JFindView(R.id.transaction_record_withdraw_time4)
    private TextView transactionRecordWithdrawTime4; // 提现到账的时间
    @JFindView(R.id.transaction_record_withdraw_bank)
    private TextView transactionRecordWithdrawBank; // 提现的银行
    @JFindView(R.id.transaction_record_withdraw_bank_1)
    private TextView transactionRecordWithdrawBank1; // 提现的银行号
    @JFindView(R.id.transaction_record_withdraw_number)
    private TextView transactionRecordWithdrawNumber; // 提现的单号

    @JFindView(R.id.transaction_record_income_ll4)
    private LinearLayout transactionRecordIncomeLl4; // 收入的页面布局
    @JFindView(R.id.transaction_record_income_money_1)
    private TextView transactionRecordIncomeMoney1; // 收款金额
    @JFindView(R.id.transaction_record_income_state)
    private TextView transactionRecordIncomeState; // 收款的状态
    @JFindView(R.id.transaction_record_income_time_1)
    private TextView transactionRecordIncomeTime1; // 收款的日期
    @JFindView(R.id.transaction_record_income_time_2)
    private TextView transactionRecordIncomeTime2; // 收款的时间
    @JFindView(R.id.transaction_record_income_number_1)
    private TextView transactionRecordIncomeNumber1; // 交易单号
    @JFindView(R.id.transaction_record_income_number_2)
    private TextView transactionRecordIncomeNumber2; // 商户单号

    private String transactionType, transactionNumber;
    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_TRANSACTION_RECORD_WITHDRAW_HANDLER: // 提现
                    WithdrawMsgRequest withdrawRequest = (WithdrawMsgRequest) msg.obj;
                    hideDialogByProgress();
                    String withdrawString = withdrawRequest.getIsSucess();
                    if (withdrawString.equals("true")) {
                        WithdrawMsgResult withdrawResult = withdrawRequest.getData();
                        transactionRecordWithdrawMoney1.setText(withdrawResult.getPayMoney());
                        transactionRecordWithdrawMoney2.setText(withdrawResult.getCashTotal());
                        transactionRecordWithdrawMoney3.setText(withdrawResult.getChangeMoney());
                        transactionRecordWithdrawState.setText(withdrawResult.getApprovelState());
                        transactionRecordWithdrawTime1.setText(withdrawResult.getCashDateTime());
                        transactionRecordWithdrawTime3.setText(withdrawResult.getTranceDateTime());
                        transactionRecordWithdrawNumber.setText(withdrawResult.getTransNo());
                    }
                    break;

                case GlobalConsts.GET_TRANSACTION_RECORD_INCOME_HANDLER: // 收入
                    IncomeRequest incomeRequest = (IncomeRequest) msg.obj;
                    hideDialogByProgress();
                    String incomeString = incomeRequest.getIsSucess();
                    if (incomeString.equals("true")) {
                        IncomeResult incomeResult = incomeRequest.getData();
                        transactionRecordIncomeMoney1.setText(incomeResult.getPayTal());
                        transactionRecordIncomeState.setText(incomeResult.getTransState());
                        transactionRecordIncomeTime1.setText(incomeResult.getPayDateTime());
                        transactionRecordIncomeNumber1.setText(incomeResult.getTransNo());
                        transactionRecordIncomeNumber2.setText(incomeResult.getTransNo());
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record_layout);
        Jet.bind(this);

        dialogByProgress = new DialogByProgress(TransactionRecordActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        transactionType = intent.getStringExtra("TRANSACTION_TYPE"); // 交易类型
        transactionNumber = intent.getStringExtra("TRANSACTION_NUMBER"); // 交易单号

        if (transactionType.equals("提现")) {
            transactionRecordTypeTv1.setText("提现");
            transactionRecordWithdrawLl3.setVisibility(View.VISIBLE);
            transactionRecordIncomeLl4.setVisibility(View.GONE);
            getDateByWithdraw(transactionNumber); // 提现
        } else if (transactionType.equals("收入")) {
            transactionRecordTypeTv1.setText("收入");
            transactionRecordWithdrawLl3.setVisibility(View.GONE);
            transactionRecordIncomeLl4.setVisibility(View.VISIBLE);
            getDateByIncome(transactionNumber); // 收入
        }
    }

    /**
     * 提现
     *
     * @param transactionNumber
     */
    private void getDateByWithdraw(String transactionNumber) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_TRANSACTION_RECORD_WITHDRAW_URL + transactionNumber).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "提现的信息---" + bodyString.toString());
                Gson withdrawGson = new Gson();
                WithdrawMsgRequest withdrawRequest = withdrawGson.fromJson(bodyString, WithdrawMsgRequest.class);
                Message withdrawMsg = Message.obtain();
                withdrawMsg.what = GlobalConsts.GET_TRANSACTION_RECORD_WITHDRAW_HANDLER;
                withdrawMsg.obj = withdrawRequest;
                handler.sendMessage(withdrawMsg);
            }
        });
    }

    /**
     * 收入
     *
     * @param transactionNumber
     */
    private void getDateByIncome(String transactionNumber) {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_TRANSACTION_RECORD_INCOME_URL + transactionNumber).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "收入的信息---" + bodyString.toString());
                Gson incomeGson = new Gson();
                IncomeRequest incomeRequest = incomeGson.fromJson(bodyString, IncomeRequest.class);
                Message incomeMsg = Message.obtain();
                incomeMsg.what = GlobalConsts.GET_TRANSACTION_RECORD_INCOME_HANDLER;
                incomeMsg.obj = incomeRequest;
                handler.sendMessage(incomeMsg);
            }
        });
    }


    /**
     * 实现监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction_record_back:
                TransactionRecordActivity.this.finish();
                break;
        }
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
