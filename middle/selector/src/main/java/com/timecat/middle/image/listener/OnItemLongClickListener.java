package com.timecat.middle.image.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/10
 * @description 长按事件
 * @usage null
 */
public interface OnItemLongClickListener {
    void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v);
}
