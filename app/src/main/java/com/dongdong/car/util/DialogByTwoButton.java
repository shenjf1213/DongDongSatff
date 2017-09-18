package com.dongdong.car.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dongdong.car.R;

/**
 * 提示信息dialog
 */
public class DialogByTwoButton extends Dialog {

    private Context context;
    private TextView tv_left, tv_right, tv_message, tv_title;
    private String leftText, rightText, message, title;
    private ClickListenerInterface clickListenerInterface;

    /**
     * 如果处理外部点击事件则传入相应的接口
     */
    public interface ClickListenerInterface {
        public void doNegative();

        public void doPositive();
    }

    /**
     * 设置文字与点击事件
     *
     * @param context
     * @param leftText
     * @param rightText
     */
    public DialogByTwoButton(Context context, String title, String message, String leftText, String rightText) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.message = message;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_two_button);
        setCanceledOnTouchOutside(false);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_left = (TextView) findViewById(R.id.tv_left_txt);
        tv_right = (TextView) findViewById(R.id.tv_right_txt);

        tv_title.setText(title);
        tv_message.setText(message);
        tv_left.setText(leftText);
        tv_right.setText(rightText);

        tv_left.setOnClickListener(new clickListener());
        tv_right.setOnClickListener(new clickListener());
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_right_txt:
                    clickListenerInterface.doPositive();
                    break;

                case R.id.tv_left_txt:
                    clickListenerInterface.doNegative();
                    break;
            }
        }
    }

    @Override
    public void show() {
        super.show();
        if (leftText != null && leftText != null) {
            tv_title.setText(title);
            tv_message.setText(message);
            tv_left.setText(leftText);
            tv_right.setText(rightText);
        }
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ScreenUtils.getScreenWidth(context) / 10 * 8;
        onWindowAttributesChanged(lp);
    }
}