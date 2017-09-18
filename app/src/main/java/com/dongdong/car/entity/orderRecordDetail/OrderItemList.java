package com.dongdong.car.entity.orderRecordDetail;

/**
 * Created by 沈 on 2017/6/27.
 */

public class OrderItemList {

    private String itemName;
    private String itemPrice;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "OrderItemList{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                '}';
    }
}
