package com.dongdong.car.entity.nearOrder;

import java.util.List;

/**
 * Created by 沈 on 2017/6/22.
 */

public class NearOrderResult {

    private List<NearOrderList> staffOrderList;
    private String pageSize;
    private String count;

    public List<NearOrderList> getStaffOrderList() {
        return staffOrderList;
    }

    public void setStaffOrderList(List<NearOrderList> staffOrderList) {
        this.staffOrderList = staffOrderList;
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
        return "NearOrderResult{" +
                "staffOrderList=" + staffOrderList +
                ", pageSize='" + pageSize + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
