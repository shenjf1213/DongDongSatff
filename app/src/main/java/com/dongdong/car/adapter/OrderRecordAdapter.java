package com.dongdong.car.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 沈 on 2017/6/3.
 */

public class OrderRecordAdapter extends FragmentPagerAdapter {

    private List<Fragment> orderRecordFragment;

    public OrderRecordAdapter(FragmentManager fm, List<Fragment> orderRecordFragment) {
        super(fm);
        this.orderRecordFragment = orderRecordFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return orderRecordFragment.get(position);
    }

    @Override
    public int getCount() {
        return orderRecordFragment.size();
    }
}
