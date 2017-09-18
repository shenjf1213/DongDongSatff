package com.dongdong.car.ui.myWallet;

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
import com.dongdong.car.entity.setOrChangPayPassword.SetPayPassRequest;
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
 * Created by 沈 on 2017/6/5.
 */
public class TransactionPasActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences spf;
    private String id;
    private ImageView transactionPasBack;
    private TextView transactionPasTv; // 支付密码显示文字
    private TextView transactionPasNewTv1, transactionPasNewTv2, transactionPasNewTv3, transactionPasNewTv4, transactionPasNewTv5, transactionPasNewTv6;
    private TextView transactionPasConTv1, transactionPasConTv2, transactionPasConTv3, transactionPasConTv4, transactionPasConTv5, transactionPasConTv6;
    private EditText transactionPasEt1, transactionPasEt2;
    private String newKey = "";
    private String conKey = "";
    private Button transactionPasBtn;
    private DialogByOneButton dialog;

    private DialogByProgress dialogByProgress;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.SET_OR_CHANGE_PAY_PASSWORD_HANDLER:
                    SetPayPassRequest setPayPassRequest = (SetPayPassRequest) msg.obj;
                    hideDialogByProgress();
                    String setPayPassState = setPayPassRequest.getIsSucess();
                    if (setPayPassState.equals("true")) {
                        SharedPreferences.Editor editor = spf.edit();
                        editor.putString("transactionPas", "true");
                        editor.commit();
                        dialog = new DialogByOneButton(TransactionPasActivity.this, "提示", "交易密码设置成功", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                                TransactionPasActivity.this.finish();
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
        setContentView(R.layout.activity_transaction_pas_layout);

        dialogByProgress = new DialogByProgress(TransactionPasActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        spf = TransactionPasActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        transactionPasBack = (ImageView) findViewById(R.id.transaction_pas_back);
        transactionPasTv = (TextView) findViewById(R.id.transaction_pas_tv);
        transactionPasBtn = (Button) findViewById(R.id.transaction_pas_btn);
        transactionPasNewTv1 = (TextView) findViewById(R.id.transaction_pas_new_tv1);
        transactionPasNewTv2 = (TextView) findViewById(R.id.transaction_pas_new_tv2);
        transactionPasNewTv3 = (TextView) findViewById(R.id.transaction_pas_new_tv3);
        transactionPasNewTv4 = (TextView) findViewById(R.id.transaction_pas_new_tv4);
        transactionPasNewTv5 = (TextView) findViewById(R.id.transaction_pas_new_tv5);
        transactionPasNewTv6 = (TextView) findViewById(R.id.transaction_pas_new_tv6);

        transactionPasConTv1 = (TextView) findViewById(R.id.transaction_pas_con_tv1);
        transactionPasConTv2 = (TextView) findViewById(R.id.transaction_pas_con_tv2);
        transactionPasConTv3 = (TextView) findViewById(R.id.transaction_pas_con_tv3);
        transactionPasConTv4 = (TextView) findViewById(R.id.transaction_pas_con_tv4);
        transactionPasConTv5 = (TextView) findViewById(R.id.transaction_pas_con_tv5);
        transactionPasConTv6 = (TextView) findViewById(R.id.transaction_pas_con_tv6);

        transactionPasEt1 = (EditText) findViewById(R.id.transaction_pas_et1);
        transactionPasEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newKey = s.toString().trim();
                setNewKey(newKey);
            }
        });

        transactionPasEt2 = (EditText) findViewById(R.id.transaction_pas_et2);
        transactionPasEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                conKey = s.toString().trim();
                setConKye(conKey);
            }
        });
    }

    /**
     * 获取输入框的数值
     *
     * @param newKey
     */
    private void setNewKey(String newKey) {
        char[] newKeyArr = newKey.toCharArray();
        transactionPasNewTv1.setText("");
        transactionPasNewTv2.setText("");
        transactionPasNewTv3.setText("");
        transactionPasNewTv4.setText("");
        transactionPasNewTv5.setText("");
        transactionPasNewTv6.setText("");
        for (int i = 0; i < newKeyArr.length; i++) {
            if (i == 0) {
                transactionPasNewTv1.setText(String.valueOf(newKeyArr[0]));
            } else if (i == 1) {
                transactionPasNewTv2.setText(String.valueOf(newKeyArr[1]));
            } else if (i == 2) {
                transactionPasNewTv3.setText(String.valueOf(newKeyArr[2]));
            } else if (i == 3) {
                transactionPasNewTv4.setText(String.valueOf(newKeyArr[3]));
            } else if (i == 4) {
                transactionPasNewTv5.setText(String.valueOf(newKeyArr[4]));
            } else if (i == 5) {
                transactionPasNewTv6.setText(String.valueOf(newKeyArr[5]));
            }
        }
    }

    /**
     * 获取确认输入框的数值
     *
     * @param conKey
     */
    private void setConKye(String conKey) {
        char[] conKeyArr = conKey.toCharArray();
        transactionPasConTv1.setText("");
        transactionPasConTv2.setText("");
        transactionPasConTv3.setText("");
        transactionPasConTv4.setText("");
        transactionPasConTv5.setText("");
        transactionPasConTv6.setText("");
        for (int j = 0; j < conKeyArr.length; j++) {
            if (j == 0) {
                transactionPasConTv1.setText(String.valueOf(conKeyArr[0]));
            } else if (j == 1) {
                transactionPasConTv2.setText(String.valueOf(conKeyArr[1]));
            } else if (j == 2) {
                transactionPasConTv3.setText(String.valueOf(conKeyArr[2]));
            } else if (j == 3) {
                transactionPasConTv4.setText(String.valueOf(conKeyArr[3]));
            } else if (j == 4) {
                transactionPasConTv5.setText(String.valueOf(conKeyArr[4]));
            } else if (j == 5) {
                transactionPasConTv6.setText(String.valueOf(conKeyArr[5]));
            }
        }
    }

    /**
     * 监听
     */
    private void initListener() {
        transactionPasBack.setOnClickListener(this);
        transactionPasBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction_pas_back:
                TransactionPasActivity.this.finish();
                break;

            case R.id.transaction_pas_btn:
                if (newKey.equals("")) {
                    Toast.makeText(TransactionPasActivity.this, "请设置新的交易密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newKey.length() < 6) {
                    Toast.makeText(TransactionPasActivity.this, "为保证账户资产交易安全，请设置六位数的密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (conKey.equals("")) {
                    Toast.makeText(TransactionPasActivity.this, "请再次确认交易密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newKey.equals(conKey)) {
                    Toast.makeText(TransactionPasActivity.this, "交易密码与确认交易密码请保持一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialogByProgress.show();
                    Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/GetPayTotal?staffId=" + id + "&paypassword=" + conKey).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Gson setPayPassGson = new Gson();
                            SetPayPassRequest setPayPassRequest = setPayPassGson.fromJson(bodyString, SetPayPassRequest.class);
                            Message setPayPassMsg = Message.obtain();
                            setPayPassMsg.what = GlobalConsts.SET_OR_CHANGE_PAY_PASSWORD_HANDLER;
                            setPayPassMsg.obj = setPayPassRequest;
                            handler.sendMessage(setPayPassMsg);
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
