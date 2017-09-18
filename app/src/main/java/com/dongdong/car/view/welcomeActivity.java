package com.dongdong.car.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dongdong.car.R;
import com.dongdong.car.adapter.AdapterViewPager;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.ui.driverCenter.LoginActivity;

import java.util.ArrayList;

/**
 * Created by 沈 on 2017/6/13.
 */

public class welcomeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private SharedPreferences spf;
    private ViewPager viewPager; // 定义ViewPager对象
    private ArrayList<View> views = new ArrayList<>(); // 定义一个ArrayList来存放View
    private static final int[] pics = {R.drawable.wel, R.drawable.wel, R.drawable.wel}; // 引导图片资源
    private AdapterViewPager vpAdapter; // 定义ViewPager适配器
    private LinearLayout linearLayout;
    private Button startBtn;

    private ImageView[] points; // 底部小点的图片
    private int currentIndex; // 记录当前选中位置

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        spf = welcomeActivity.this.getSharedPreferences("user_info", 0);
        String isFirstStr = spf.getString("isFirst", "");
        if (isFirstStr.equals("false")) { // 判断是否为第一次加载app
            startActivity(new Intent(welcomeActivity.this, FlashActivity.class));
            welcomeActivity.this.finish();
        } else {
            initView();
            initData();
        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new AdapterViewPager(views); // 实例化ViewPager适配器
        startBtn = (Button) findViewById(R.id.welcome_btn);
    }

    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(iv);
        }
        //设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);
        // 初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        linearLayout = (LinearLayout) findViewById(R.id.guide_point);
        points = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i); // 得到一个LinearLayout下面的每一个子元素
            points[i].setEnabled(true); // 默认都设为灰色
            points[i].setOnClickListener(this); // 给每个小点设置监听
            points[i].setTag(i); // 设置位置tag，方便取出与当前位置对应
        }
        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 通过点击事件来切换当前的页面
     */
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑到第三张时弹出开始按钮
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        setCurDot(position);
        if (position == 2) {
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spf = welcomeActivity.this.getSharedPreferences("user_info", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("isFirst", "false");
                    editor.commit();
                    startActivity(new Intent(welcomeActivity.this, LoginActivity.class));
                    welcomeActivity.this.finish();
                }
            });
        } else {
            startBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置当前页面的位置
     */
    public void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前页面显示的圆点类型
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length - 1 || currentIndex == position) {
            return;
        }
        points[position].setEnabled(false);
        points[currentIndex].setEnabled(true);
        currentIndex = position;
    }
}
