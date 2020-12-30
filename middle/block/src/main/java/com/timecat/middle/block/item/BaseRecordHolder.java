package com.timecat.middle.block.item;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.timecat.layout.ui.utils.ViewUtil;
import com.timecat.middle.block.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.ExpandableViewHolder;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/15
 * @description null
 * @usage null
 */
public class BaseRecordHolder extends ExpandableViewHolder {

    protected boolean swiped = false;
    protected Context mContext;
    protected View frontView;
    protected View rearLeftView;
    protected View rearRightView;

    /**
     * @author dlink
     * @email linxy59@mail2.sysu.edu.cn
     * @date 2018/8/16
     * @description 可有多种操作：多选，删除，存档，拖拽移动，长按排序
     * @usage null
     */
    public BaseRecordHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        frontView = view.findViewById(R.id.front_view);
        rearLeftView = view.findViewById(R.id.rear_left_view);
        rearRightView = view.findViewById(R.id.rear_right_view);
        mContext = view.getContext();
        frontView.setOnClickListener(this);
    }

    @Override
    protected void setDragHandleView(@NonNull View view) {
        if (mAdapter.isHandleDragEnabled()) {
            view.setVisibility(View.VISIBLE);
            super.setDragHandleView(view);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void toggleActivation() {
        super.toggleActivation();
        // Here we use a custom Animation inside the ItemView
//        mFlipView.flip(mAdapter.isSelected(getAdapterPosition()));
    }

    @Override
    public float getActivationElevation() {
        return ViewUtil.dp2px(10);
    }

    @Override
    protected boolean shouldActivateViewWhileSwiping() {
        return false;//default=false
    }

    @Override
    protected boolean shouldAddSelectionInActionMode() {
        return false;//default=false
    }

    @Override
    protected void expandView(int position) {
        super.expandView(position);
        mAdapter.invalidateItemDecorations(100);
    }

    @Override
    protected void collapseView(int position) {
        super.collapseView(position);
        mAdapter.invalidateItemDecorations(100);
    }

    @Override
    public View getFrontView() {
        return frontView;
    }

    @Override
    public View getRearLeftView() {
        return rearLeftView;
    }

    @Override
    public View getRearRightView() {
        return rearRightView;
    }

    @Override
    public void onItemReleased(int position) {
        swiped = (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE);
        super.onItemReleased(position);
    }
}