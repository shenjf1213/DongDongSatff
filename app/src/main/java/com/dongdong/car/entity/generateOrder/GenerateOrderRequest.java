package com.dongdong.car.entity.generateOrder;

import java.util.List;

/**
 * Created by 沈 on 2017/7/5.
 */

public class GenerateOrderRequest {

    private String isSucess;
    private GenerateOrderResult data;
    private String msg;

    public String getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(String isSucess) {
        this.isSucess = isSucess;
    }

    public GenerateOrderResult getData() {
        return data;
    }

    public void setData(GenerateOrderResult data) {
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
        return "GenerateOrderRequest{" +
                "isSucess='" + isSucess + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class GenerateOrderResult {

        private GenerateOrderInfo orderInfo;
        private GenerateOrderUserInfo userInfo;
        private String longitude;
        private String latitude;
        private String address;
        private String maxLat;
        private String id;
        private String remark;

        public GenerateOrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(GenerateOrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public GenerateOrderUserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(GenerateOrderUserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMaxLat() {
            return maxLat;
        }

        public void setMaxLat(String maxLat) {
            this.maxLat = maxLat;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "GenerateOrderResult{" +
                    "orderInfo=" + orderInfo +
                    ", userInfo=" + userInfo +
                    ", longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", address='" + address + '\'' +
                    ", maxLat='" + maxLat + '\'' +
                    ", id='" + id + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }

        public class GenerateOrderUserInfo {

            private String id;
            private String orderNumber;
            private String longitude;
            private String latitude;
            private String address;
            private String carNumber;
            private String orderType;
            private String doorTime;
            private String orderTime;
            private String appointmenttime;
            private String orderTotal;
            private String orderState;
            private List<GenerateOrderItemList> orderItemList;
            private List<GenerateOrderPictureList> orderPictureList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderNumber() {
                return orderNumber;
            }

            public void setOrderNumber(String orderNumber) {
                this.orderNumber = orderNumber;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getDoorTime() {
                return doorTime;
            }

            public void setDoorTime(String doorTime) {
                this.doorTime = doorTime;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getAppointmenttime() {
                return appointmenttime;
            }

            public void setAppointmenttime(String appointmenttime) {
                this.appointmenttime = appointmenttime;
            }

            public String getOrderTotal() {
                return orderTotal;
            }

            public void setOrderTotal(String orderTotal) {
                this.orderTotal = orderTotal;
            }

            public String getOrderState() {
                return orderState;
            }

            public void setOrderState(String orderState) {
                this.orderState = orderState;
            }

            public List<GenerateOrderItemList> getOrderItemList() {
                return orderItemList;
            }

            public void setOrderItemList(List<GenerateOrderItemList> orderItemList) {
                this.orderItemList = orderItemList;
            }

            public List<GenerateOrderPictureList> getOrderPictureList() {
                return orderPictureList;
            }

            public void setOrderPictureList(List<GenerateOrderPictureList> orderPictureList) {
                this.orderPictureList = orderPictureList;
            }

            public class GenerateOrderPictureList {
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
                    return "GenerateOrderPictureList{" +
                            "itemName='" + itemName + '\'' +
                            ", itemPrice='" + itemPrice + '\'' +
                            '}';
                }
            }

            public class GenerateOrderItemList {
                private String imgeFile;

                public String getImgeFile() {
                    return imgeFile;
                }

                public void setImgeFile(String imgeFile) {
                    this.imgeFile = imgeFile;
                }

                @Override
                public String toString() {
                    return "GenerateOrderItemList{" +
                            "imgeFile='" + imgeFile + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "GenerateOrderUserInfo{" +
                        "id='" + id + '\'' +
                        ", orderNumber='" + orderNumber + '\'' +
                        ", longitude='" + longitude + '\'' +
                        ", latitude='" + latitude + '\'' +
                        ", address='" + address + '\'' +
                        ", carNumber='" + carNumber + '\'' +
                        ", orderType='" + orderType + '\'' +
                        ", doorTime='" + doorTime + '\'' +
                        ", orderTime='" + orderTime + '\'' +
                        ", appointmenttime='" + appointmenttime + '\'' +
                        ", orderTotal='" + orderTotal + '\'' +
                        ", orderState='" + orderState + '\'' +
                        ", orderItemList=" + orderItemList +
                        ", orderPictureList=" + orderPictureList +
                        '}';
            }
        }

        public class GenerateOrderInfo {
            private String id;
            private boolean enabled;
            private String mobilePhone;
            private String userName;
            private String displayName;
            private String sex;
            private String carNumber;
            private String carModel;
            private String carColor;
            private String headPicture;
            private String remark;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getMobilePhone() {
                return mobilePhone;
            }

            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public String getCarModel() {
                return carModel;
            }

            public void setCarModel(String carModel) {
                this.carModel = carModel;
            }

            public String getCarColor() {
                return carColor;
            }

            public void setCarColor(String carColor) {
                this.carColor = carColor;
            }

            public String getHeadPicture() {
                return headPicture;
            }

            public void setHeadPicture(String headPicture) {
                this.headPicture = headPicture;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            @Override
            public String toString() {
                return "GenerateOrderInfo{" +
                        "id='" + id + '\'' +
                        ", enabled=" + enabled +
                        ", mobilePhone='" + mobilePhone + '\'' +
                        ", userName='" + userName + '\'' +
                        ", displayName='" + displayName + '\'' +
                        ", sex='" + sex + '\'' +
                        ", carNumber='" + carNumber + '\'' +
                        ", carModel='" + carModel + '\'' +
                        ", carColor='" + carColor + '\'' +
                        ", headPicture='" + headPicture + '\'' +
                        ", remark='" + remark + '\'' +
                        '}';
            }
        }
    }
}
