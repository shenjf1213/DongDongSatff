package com.dongdong.car.entity.toatlData;

/**
 * Created by 沈 on 2017/7/3.
 */

public class TotalRequest {

    private String isSucess;
    private TotalResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public TotalResult getData() {
        return data;
    }

    public void setData(TotalResult data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TotalRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class TotalResult {
        private String payTotal;

        public String getPayTotal() {
            return payTotal;
        }

        public void setPayTotal(String payTotal) {
            this.payTotal = payTotal;
        }

        @Override
        public String toString() {
            return "TotalResult{" +
                    "payTotal='" + payTotal + '\'' +
                    '}';
        }
    }
}
