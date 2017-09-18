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
 * Created by zhangyu on 16-8-25.
 */
public class DialogByOneButton extends Dialog {

    private Context context;
    private TextView positiveTv, messageTv, titleTv;
    private String positiveString, messageString, titleString;

    private DialogByOneButton.ClickListenerInterface clickListenerInterface;

    /**
     * 如果处理外部点击事件则传入相应的接口
     */
    public interface ClickListenerInterface {
        public void doPositive();
    }


    /**
     * @param context
     * @param title
     * @param message
     * @param positive
     */
    public DialogByOneButton(Context context, String title, String message, String positive) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.titleString = title;
        this.messageString = message;
        this.positiveString = positive;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_recommend_more);
        setCanceledOnTouchOutside(false);
        titleTv = (TextView) findViewById(R.id.tv_title);
        messageTv = (TextView) findViewById(R.id.tv_content);
        positiveTv = (TextView) findViewById(R.id.tv_do_txt);

        titleTv.setText(titleString);
        messageTv.setText(messageString);
        positiveTv.setText(positiveString);

        positiveTv.setOnClickListener(new clickListener());
    }

    public void setClicklistener(DialogByOneButton.ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_do_txt:
                    clickListenerInterface.doPositive();
                    break;
            }
        }
    }

    @Override
    public void show() {
        super.show();
        if (titleString != null) {
            titleTv.setText(titleString);
        }
        if (positiveString != null) {
            positiveTv.setText(positiveString);
        }
        messageTv.setText(messageString);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ScreenUtils.getScreenWidth(context) / 10 * 8;
        onWindowAttributesChanged(lp);
    }
}
