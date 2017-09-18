package com.dongdong.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongdong.car.R;

import java.util.List;

/**
 * Created by 沈 on 2017/6/9.
 */

public class NearOrderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private Context context;
    private List<String> nearOrderDataList;
    private LayoutInflater inflater;

    public NearOrderRecyclerAdapter(Context context, List<String> nearOrderDataList) {
        super();
        this.context = context;
        this.nearOrderDataList = nearOrderDataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.listitem_layout, null);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChildViewHolder ViewHolder = (ChildViewHolder) holder;
        ViewHolder.itemTv.setText(nearOrderDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return nearOrderDataList == null ? 0 : nearOrderDataList.size();
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView itemTv;

        public ChildViewHolder(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView;
        }
    }
}
