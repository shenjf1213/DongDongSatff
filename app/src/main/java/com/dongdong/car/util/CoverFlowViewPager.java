package com.dongdong.car.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dongdong.car.R;
import com.dongdong.car.adapter.CoverFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沈 on 2017/6/6.
 */

public class CoverFlowViewPager extends RelativeLayout implements OnPageSelectListener {

    private CoverFlowAdapter mAdapter; // 图片展示的适配器
    private ViewPager mViewPager; // 用于左右滚动
    private List<View> mViewList = new ArrayList<>(); // 需要显示的视图集合
    private Context context;
    private OnPageSelectListener listener;
    private FrameLayout layout;

    public CoverFlowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.widget_cover_flow, this);
        mViewPager = (ViewPager) findViewById(R.id.vp_cover_flow);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        // 构造适配器，传入数据源
        mAdapter = new CoverFlowAdapter(mViewList, getContext());
        // 设置选中的回调
        mAdapter.setOnPageSelectListener(this);
        // 设置适配器
        mViewPager.setAdapter(mAdapter);
        // 设置滑动的监听，因为adapter实现了滑动回调的接口，所以这里直接设置adapter
        mViewPager.addOnPageChangeListener(mAdapter);
        // 限定预加载的页面个数
        mViewPager.setOffscreenPageLimit(5);

        // 设置触摸事件的分发
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 传递给ViewPager 进行滑动处理
                return mViewPager.dispatchTouchEvent(event);
            }
        });
    }

    @Override
    public void select(int position) {
        if (listener != null) {
            listener.select(position);
        }
    }

    /**
     * 当将某一个作为最中央时的回调
     *
     * @param listener
     */
    public void setOnPageSelectListener(OnPageSelectListener listener) {
        this.listener = listener;
    }

    /**
     * 设置显示的数据，进行一层封装
     *
     * @param lists
     */
    public void setViewList(List<View> lists) {
        if (lists == null) {
            return;
        }
        mViewList.clear();
        for (View view : lists) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeAllViewsInLayout();
            }
            // 设置padding值，默认缩小
            layout = new FrameLayout(getContext());
            layout.setPadding(CoverFlowAdapter.sWidthPadding, CoverFlowAdapter.sHeightPadding, CoverFlowAdapter.sWidthPadding, CoverFlowAdapter.sHeightPadding);
            layout.addView(view);
            mViewList.add(layout);
        }
        // 刷新数据
        mAdapter.notifyDataSetChanged();
    }
}
