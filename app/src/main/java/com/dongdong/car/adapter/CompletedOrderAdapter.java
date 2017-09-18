package com.dongdong.car.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.dongdong.car.R;
import com.dongdong.car.entity.orderRecord.OrderRecordList;
import com.dongdong.car.ui.orderRecord.OrderRecordDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沈 on 2017/6/20.
 */

public class CompletedOrderAdapter extends BaseRecyclerAdapter<CompletedOrderAdapter.orderRecordViewHolder> {

    private Context context;
    private View adapterView;
    private LayoutInflater inflater;
    private List<OrderRecordList> orderRecordLists = new ArrayList<>();

    public CompletedOrderAdapter(Context context, List<OrderRecordList> orderRecordLists) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.orderRecordLists = orderRecordLists;
    }

    public void setData(List<OrderRecordList> orderRecordLists) {
        this.orderRecordLists = orderRecordLists;
        notifyDataSetChanged();
    }

    @Override
    public orderRecordViewHolder getViewHolder(View view) {
        return new orderRecordViewHolder(view, false);
    }

    @Override
    public orderRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        adapterView = inflater.inflate(R.layout.order_item_adapter_layout, parent, false);
        final orderRecordViewHolder viewHolder = new orderRecordViewHolder(adapterView, true);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // 点击监听
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                OrderRecordList orderRecordList = orderRecordLists.get(position);
                Intent intent = new Intent(v.getContext(), OrderRecordDetailActivity.class);
                intent.putExtra("ORDER_RECORD_ID", orderRecordList.getId());
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(orderRecordViewHolder holder, final int position, boolean isItem) {
        orderRecordViewHolder viewHolder = holder;
        if (TextUtils.equals("0", orderRecordLists.get(position).getOrderType())) {
            viewHolder.orderType.setText("上门洗车");
        } else if (TextUtils.equals("1", orderRecordLists.get(position).getOrderType())) {
            viewHolder.orderType.setText("预约洗车");
        }
        viewHolder.orderNumber.setText(orderRecordLists.get(position).getOrderNumber());
        viewHolder.orderDate.setText(orderRecordLists.get(position).getOrderTime());
        viewHolder.orderAddress.setText(orderRecordLists.get(position).getAddress());
        viewHolder.orderCarPlate.setText(orderRecordLists.get(position).getCarNumber());
        viewHolder.orderCarType.setText(orderRecordLists.get(position).getCarModel());
        viewHolder.orderCarColor.setText(orderRecordLists.get(position).getCarColor());
    }

    @Override
    public int getAdapterItemCount() {
        return orderRecordLists == null ? 0 : orderRecordLists.size();
    }

    public class orderRecordViewHolder extends RecyclerView.ViewHolder {

        TextView orderType; // 清洗类型
        TextView orderNumber; // 订单编号
        TextView orderDate; // 订单时间
        TextView orderAddress; // 订单地址
        TextView orderCarPlate; // 车牌
        TextView orderCarType; // 车型
        TextView orderCarColor; // 车辆颜色

        public orderRecordViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                adapterView = itemView;
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
