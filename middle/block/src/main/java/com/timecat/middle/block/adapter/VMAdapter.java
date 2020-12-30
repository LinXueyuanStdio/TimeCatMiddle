package com.timecat.middle.block.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-03
 * @description null
 * @usage null
 */

public abstract class VMAdapter<T, VH extends VMHolder> extends RecyclerView.Adapter<VH> {
    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected VMAdapter.IClickListener mListener;
    protected List<T> mDataList;

    public VMAdapter(Context context) {
        this.mActivity = (Activity)context;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataList = new ArrayList();
    }

    public VMAdapter(Context context, List<T> list) {
        this.mActivity = (Activity)context;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        if (list != null && list.size() != 0) {
            this.mDataList = list;
        } else {
            this.mDataList = new ArrayList();
        }

    }

    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final T data = this.getItemData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VMAdapter.this.mListener != null) {
                    VMAdapter.this.mListener.onItemAction(position, data);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return VMAdapter.this.mListener != null ? VMAdapter.this.mListener.onItemLongAction(position, data) : false;
            }
        });
    }

    public T getItemData(int position) {
        return position < this.getItemCount() ? this.mDataList.get(position) : null;
    }

    public int getItemCount() {
        return this.mDataList.size();
    }

    public void refresh(List<T> list) {
        if (list != null && list.size() != 0) {
            this.mDataList.clear();
            this.mDataList.addAll(list);
        } else {
            this.mDataList.clear();
        }

        this.notifyDataSetChanged();
    }

    public void setClickListener(VMAdapter.IClickListener listener) {
        this.mListener = listener;
    }

    public interface IClickListener<T> {
        void onItemAction(int var1, T var2);

        boolean onItemLongAction(int var1, T var2);
    }
}
