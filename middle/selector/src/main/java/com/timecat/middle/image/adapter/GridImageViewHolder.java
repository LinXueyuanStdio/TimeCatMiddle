package com.timecat.middle.image.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.timecat.middle.image.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/10
 * @description null
 * @usage null
 */
class GridImageViewHolder extends RecyclerView.ViewHolder {

    ImageView mImg;
    ImageView mIvDel;
    TextView tvDuration;

    public GridImageViewHolder(View view) {
        super(view);
        mImg = view.findViewById(R.id.fiv);
        mIvDel = view.findViewById(R.id.iv_del);
        tvDuration = view.findViewById(R.id.tv_duration);
    }

    public static GridImageViewHolder of(LayoutInflater mInflater, @NotNull ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.middle_item_filter_image, viewGroup, false);
        return new GridImageViewHolder(view);
    }
}
