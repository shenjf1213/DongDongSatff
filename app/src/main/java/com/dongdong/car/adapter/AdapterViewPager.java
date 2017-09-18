package com.dongdong.car.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author yangyu
 *         功能描述：ViewPager适配器，用来绑定数据和view
 */
public class AdapterViewPager extends PagerAdapter {

    //界面列表
    private ArrayList<View> views;

    public AdapterViewPager(ArrayList<View> views) {
        this.views = views;
    }

    /**
     * 获得当前界面数
     */
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View view, Object arg1) {
        return (view == arg1);
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
