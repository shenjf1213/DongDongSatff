package com.dongdong.car.ui.myWallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.dongdong.car.R;
import com.dongdong.car.adapter.TransactionRecordAdapter;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.toatlData.TotalRequest;
import com.dongdong.car.entity.transactionRecord.TransactionRecordRequest;
import com.dongdong.car.entity.transactionRecord.TransactionRecordResult;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.DialogByTwoButton;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.view.CustomFooterView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/4.
 */

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {

    private String userTotal, userBank, userBankCard;
    private SharedPreferences walletSpf;

    private String isSaveTransactionPas, transactionPasStr, id;
    private ImageView myWalletBack; // 返回
    private TextView myWalletMoney; // 总资产
    private TextView myWalletWithdraw; // 提现
    private TextView myWalletBackCard; // 银行卡

    private LinearLayout transactionRecordLl; // 没有数据时显示的背景
    private XRefreshView transactionRecordXV; // 刷新控件
    private RecyclerView transactionRecordRV; // 数据列表
    private List<TransactionRecordResult> transactionRecordList = new ArrayList<>();
    private TransactionRecordAdapter transactionRecordAdapter;
    private int upOrDown; // 判断动作
    private int mLoadCount = 0;
    public static long lastRefreshTime; // 下拉刷新结束的时间

    private DialogByProgress dialogByProgress;
    private DialogByTwoButton dialog2;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.GET_TRANSACTION_RECORD_HANDLER: // TODO 一次加载全部数据，不做分页加载
                    TransactionRecordRequest transactionRecordRequest = (TransactionRecordRequest) msg.obj;
                    hideDialogByProgress();
                    String transactionRecordState = transactionRecordRequest.getIsSucess();
                    transactionRecordList = transactionRecordRequest.getData();
                    if (transactionRecordState.equals("true")) {
                        if (transactionRecordList.size() != 0) {
                            transactionRecordLl.setVisibility(View.GONE);
                            transactionRecordXV.setVisibility(View.VISIBLE);

                            if (upOrDown == 1) {
                                transactionRecordAdapter.setData(transactionRecordList);
                                transactionRecordXV.stopRefresh();
                            } else if (upOrDown == 2) {
                                transactionRecordAdapter.setData(transactionRecordList);
                                mLoadCount = transactionRecordList.size();
                                if (mLoadCount >= transactionRecordList.size()) {
                                    transactionRecordXV.setLoadComplete(true);
                                } else {
                                    transactionRecordXV.stopLoadMore();
                                }
                            }

                            transactionRecordAdapter.setOnItemClickListener(new TransactionRecordAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Intent intent = new Intent(MyWalletActivity.this, TransactionRecordActivity.class);
                                    intent.putExtra("TRANSACTION_TYPE", transactionRecordList.get(position).getPayType());
                                    intent.putExtra("TRANSACTION_NUMBER", transactionRecordList.get(position).getTransNo());
                                    startActivity(intent);
                                }
                            });
                        } else if (transactionRecordList.size() == 0) {
                            transactionRecordLl.setVisibility(View.VISIBLE);
                            transactionRecordXV.setVisibility(View.GONE);
                        }
                    }
                    break;

                case GlobalConsts.TOTAL_DATA_HANDLER:
                    TotalRequest totalRequest = (TotalRequest) msg.obj;
                    hideDialogByProgress();
                    String totalState = totalRequest.getIsSucess();
                    if (totalState.equals("true")) {
                        TotalRequest.TotalResult totalResult = totalRequest.getData();
                        myWalletMoney.setText(totalResult.getPayTotal());
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet_layout);

        dialogByProgress = new DialogByProgress(MyWalletActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        userTotal = intent.getStringExtra("TOTAL_ASSETS");
        userBank = intent.getStringExtra("BANK_NAME");
        userBankCard = intent.getStringExtra("BANK_CARD");

        walletSpf = MyWalletActivity.this.getSharedPreferences("user_info", 0);
        id = walletSpf.getString("uid", "");
        isSaveTransactionPas = walletSpf.getString("transactionPas", ""); // 是否设置了交易密码
        transactionPasStr = walletSpf.getString("transactionPasString", ""); // 交易密码

        initView();
        initListener();
        initData();
        initRefresh();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        myWalletBack = (ImageView) findViewById(R.id.my_wallet_back); // 返回
        myWalletMoney = (TextView) findViewById(R.id.my_wallet_money); // 总资产
        myWalletWithdraw = (TextView) findViewById(R.id.my_wallet_withdraw); // 提现
        myWalletBackCard = (TextView) findViewById(R.id.my_wallet_bank_card); // 银行卡
        transactionRecordLl = (LinearLayout) findViewById(R.id.transaction_record_ll);
        transactionRecordXV = (XRefreshView) findViewById(R.id.transaction_record_XV);
        transactionRecordRV = (RecyclerView) findViewById(R.id.transaction_record_RecyclerView); // 数据列表
    }

    /**
     * 监听
     */
    private void initListener() {
        myWalletBack.setOnClickListener(this);
        myWalletWithdraw.setOnClickListener(this);
        myWalletBackCard.setOnClickListener(this);
    }

    /**
     * 获取数据并赋值
     */
    private void initData() {
        myWalletMoney.setText(userTotal);
    }

    /**
     * 刷新控件设置
     */
    private void initRefresh() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyWalletActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        transactionRecordRV.setLayoutManager(layoutManager);
        transactionRecordAdapter = new TransactionRecordAdapter(MyWalletActivity.this, transactionRecordList);
        transactionRecordRV.setAdapter(transactionRecordAdapter);

        transactionRecordXV.setPullRefreshEnable(true); // 设置是否可以下拉刷新
        transactionRecordXV.setPullLoadEnable(true);  // 设置是否可以上拉加载
        transactionRecordXV.setAutoRefresh(true); // 设置可以自动刷新
        transactionRecordXV.restoreLastRefreshTime(lastRefreshTime); // 设置上次刷新的时间
        transactionRecordXV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                requestData();
                upOrDown = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        lastRefreshTime = transactionRecordXV.getLastRefreshTime();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                upOrDown = 2;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 500);
            }
        });
        requestData();
    }

    private void requestData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (transactionRecordAdapter.getCustomLoadMoreView() == null) {
                    transactionRecordAdapter.setCustomLoadMoreView(new CustomFooterView(MyWalletActivity.this));
                }
                transactionRecordAdapter.setData(transactionRecordList);
            }
        }, 1000);
    }

    /**
     * 从接口获取数据
     */
    private void getData() {
        dialogByProgress.show();
        Request request = new Request.Builder().url(GlobalConsts.GET_TRANSACTION_RECORD_URL + id).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "员工交易记录----" + bodyString.toString());
                Gson transactionRecordGson = new Gson();
                TransactionRecordRequest transactionRecordRequest = transactionRecordGson.fromJson(bodyString, TransactionRecordRequest.class);
                Message transactionRecordMsg = Message.obtain();
                transactionRecordMsg.what = GlobalConsts.GET_TRANSACTION_RECORD_HANDLER;
                transactionRecordMsg.obj = transactionRecordRequest;
                handler.sendMessage(transactionRecordMsg);
            }
        });
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_wallet_back:
                MyWalletActivity.this.finish();
                break;

            case R.id.my_wallet_withdraw: // 提现
                if (isSaveTransactionPas.equals("")) {
                    showDialogByWall();
                } else if (!isSaveTransactionPas.equals("")) { // 设置了交易密码
                    Intent intent = new Intent(MyWalletActivity.this, VerifyTransactionPassActivity.class);
                    intent.putExtra("TOTAL_ASSETS", userTotal);
                    intent.putExtra("BANK_NAME", userBank);
                    intent.putExtra("BANK_CARD", userBankCard);
                    startActivity(intent);
                }
                break;

            case R.id.my_wallet_bank_card: // 银行卡
                Intent intent1 = new Intent(MyWalletActivity.this, BankTypeActivity.class);
                intent1.putExtra("BANK_CARD", userBankCard);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        walletSpf = MyWalletActivity.this.getSharedPreferences("user_info", 0);
        isSaveTransactionPas = walletSpf.getString("transactionPas", "");
        transactionPasStr = walletSpf.getString("transactionPasString", "");
        getTotalData(); // 获取资产数据
    }

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

    /**
     * 如果没有设置支付密码弹窗提示
     */
    private void showDialogByWall() {
        dialog2 = new DialogByTwoButton(MyWalletActivity.this, "提示", "您当前还未设置支付密码，是否前往设置支付密码", "取消", "确定");
        dialog2.show();
        dialog2.setClicklistener(new DialogByTwoButton.ClickListenerInterface() {
            @Override
            public void doNegative() {
                dialog2.dismiss();
            }

            @Override
            public void doPositive() {
                dialog2.dismiss();
                startActivity(new Intent(MyWalletActivity.this, TransactionPasActivity.class));
            }
        });
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
