package com.dongdong.car.entity.orderRecord;

import java.util.List;

/**
 * Created by 沈 on 2017/6/20.
 */

public class OrderRecordResult {

    private List<OrderRecordList> staffAppList;
    private String pageSize;
    private String count;

    public List<OrderRecordList> getStaffAppList() {
        return staffAppList;
    }

    public void setStaffAppList(List<OrderRecordList> staffAppList) {
        this.staffAppList = staffAppList;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderRecordResult{" +
                "staffAppList=" + staffAppList +
                ", pageSize='" + pageSize + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
