package com.dongdong.car.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.dongdong.car.R;
import com.dongdong.car.entity.transactionRecord.TransactionRecordResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 沈 on 2017/6/30.
 */

public class TransactionRecordAdapter extends BaseRecyclerAdapter<TransactionRecordAdapter.transactionRecordViewHolder> {

    private Context context;
    private View adapterView;
    private LayoutInflater inflater;
    private List<TransactionRecordResult> transactionRecordList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private String payTypeStr, transactionRecordTimes;

    public TransactionRecordAdapter(Context context, List<TransactionRecordResult> transactionRecordList) {
        super();
        this.context = context;
        this.transactionRecordList = transactionRecordList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setData(List<TransactionRecordResult> transactionRecordList) {
        this.transactionRecordList = transactionRecordList;
        notifyDataSetChanged();
    }

    @Override
    public transactionRecordViewHolder getViewHolder(View view) {
        return new transactionRecordViewHolder(view, false);
    }

    @Override
    public transactionRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        adapterView = inflater.inflate(R.layout.transaction_record_adapter_layout, parent, false);
        transactionRecordViewHolder viewHolder = new transactionRecordViewHolder(adapterView, true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(transactionRecordViewHolder holder, final int position, boolean isItem) {
        transactionRecordViewHolder viewHolder = holder;
        payTypeStr = transactionRecordList.get(position).getPayType(); // 获取支付类型
        transactionRecordTimes = transactionRecordList.get(position).getPayDateTime();
        String[] time = transactionRecordTimes.split("[T.]");
        if ("提现".equals(payTypeStr)) {
            viewHolder.transactionRecordTypeTv.setText("-");
            viewHolder.transactionRecordTypeTv.setTextColor(Color.parseColor("#000000"));
            viewHolder.transactionRecordMoney.setText(String.valueOf(transactionRecordList.get(position).getPayTal()));
            viewHolder.transactionRecordMoney.setTextColor(Color.parseColor("#000000"));
        } else if ("收入".equals(payTypeStr)) {
            viewHolder.transactionRecordTypeTv.setText("+");
            viewHolder.transactionRecordTypeTv.setTextColor(Color.parseColor("#ec6941"));
            viewHolder.transactionRecordMoney.setText(String.valueOf(transactionRecordList.get(position).getPayTal()));
            viewHolder.transactionRecordMoney.setTextColor(Color.parseColor("#ec6941"));
        }
        viewHolder.transactionRecordType.setText(transactionRecordList.get(position).getPayType());
        viewHolder.transactionRecordDate.setText(time[0]);
        viewHolder.transactionRecordTime.setText(time[1]);

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
        return transactionRecordList == null ? 0 : transactionRecordList.size();
    }

    public class transactionRecordViewHolder extends RecyclerView.ViewHolder {

        TextView transactionRecordType; // 交易类型
        TextView transactionRecordDate; // 交易日期
        TextView transactionRecordTime; // 交易时间
        TextView transactionRecordTypeTv; // 交易类型的加减号
        TextView transactionRecordMoney; // 交易金额

        public transactionRecordViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                transactionRecordType = (TextView) itemView.findViewById(R.id.transaction_record_type);
                transactionRecordDate = (TextView) itemView.findViewById(R.id.transaction_record_date);
                transactionRecordTime = (TextView) itemView.findViewById(R.id.transaction_record_time);
                transactionRecordTypeTv = (TextView) itemView.findViewById(R.id.transaction_record_type_tv);
                transactionRecordMoney = (TextView) itemView.findViewById(R.id.transaction_record_money);
            }
        }
    }
}
