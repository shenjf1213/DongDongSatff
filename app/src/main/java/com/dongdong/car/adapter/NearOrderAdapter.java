package com.dongdong.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.dongdong.car.R;
import com.dongdong.car.entity.nearOrder.NearOrderList;

import java.util.List;

/**
 * Created by 沈 on 2017/6/22.
 */

public class NearOrderAdapter extends BaseRecyclerAdapter<NearOrderAdapter.nearOrderViewHolder> {

    private Context context;
    private List<NearOrderList> nearOrderLists;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;
    private String nearOrderTypeString;

    public NearOrderAdapter(Context context, List<NearOrderList> nearOrderDataList) {
        super();
        this.context = context;
        this.nearOrderLists = nearOrderDataList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setData(List<NearOrderList> nearOrderLists) {
        this.nearOrderLists = nearOrderLists;
        notifyDataSetChanged();
    }

    @Override
    public nearOrderViewHolder getViewHolder(View view) {
        return new nearOrderViewHolder(view, false);
    }

    @Override
    public nearOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View adapterView = inflater.inflate(R.layout.order_item_adapter_layout, parent, false);
        nearOrderViewHolder viewHolder = new nearOrderViewHolder(adapterView, true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(nearOrderViewHolder holder, final int position, boolean isItem) {
        nearOrderViewHolder viewHolder = holder;
        nearOrderTypeString = nearOrderLists.get(position).getOrderType();
        if ("0".equals(nearOrderTypeString)) {
            viewHolder.orderType.setText("上门洗车");
        } else if ("1".equals(nearOrderTypeString)) {
            viewHolder.orderType.setText("预约洗车");
        }
        viewHolder.orderNumber.setText(nearOrderLists.get(position).getOrderNumber());
        viewHolder.orderDate.setText(nearOrderLists.get(position).getOrderTime());
        viewHolder.orderAddress.setText(nearOrderLists.get(position).getAddress());
        viewHolder.orderCarPlate.setText(nearOrderLists.get(position).getCarNumber());
        viewHolder.orderCarType.setText(nearOrderLists.get(position).getCarModel());
        viewHolder.orderCarColor.setText(nearOrderLists.get(position).getCarColor());

        /**
         * 点击监听
         */
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return nearOrderLists == null ? 0 : nearOrderLists.size();
    }

    public class nearOrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderType; // 清洗类型
        TextView orderNumber; // 订单编号
        TextView orderDate; // 订单时间
        TextView orderAddress; // 订单地址
        TextView orderCarPlate; // 车牌
        TextView orderCarType; // 车型
        TextView orderCarColor; // 车辆颜色

        public nearOrderViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                orderType = (TextView) itemView.findViewById(R.id.order_type);
                orderNumber = (TextView) itemView.findViewById(R.id.order_number);
                orderDate = (TextView) itemView.findViewById(R.id.order_date);
                orderAddress = (TextView) itemView.findViewById(R.id.order_address);
                orderCarPlate = (TextView) itemView.findViewById(R.id.order_car_plate);
                orderCarType = (TextView) itemView.findViewById(R.id.order_car_type);
                orderCarColor = (TextView) itemView.findViewById(R.id.order_car_color);
            }
        }
    }
}
