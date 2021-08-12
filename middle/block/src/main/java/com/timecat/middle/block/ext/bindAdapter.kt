package com.timecat.middle.block.ext

import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.middle.block.R
import com.timecat.middle.block.base.FadeInUpItemAnimator
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/25
 * @description null
 * @usage null
 */
fun bindAdapter(
    mAdapter: BaseAdapter,
    mRecyclerView: RecyclerView,
    layoutManager: () -> RecyclerView.LayoutManager = { SmoothScrollLinearLayoutManager(mRecyclerView.context) }
) {
    mAdapter.expandItemsAtStartUp()
        .setAutoCollapseOnExpand(false)
        .setAutoScrollOnExpand(true)
        .setAnimateToLimit(Int.MAX_VALUE)
        .setNotifyChangeOfUnfilteredItems(true)
        .setAnimationOnForwardScrolling(false)
        .setAnimationOnReverseScrolling(false)
    mRecyclerView.setLayoutManager(layoutManager())
    mRecyclerView.setAdapter(mAdapter)
    mRecyclerView.setHasFixedSize(true)
    mRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(mRecyclerView.context, R.anim.layout_on_load)
    mRecyclerView.itemAnimator = FadeInUpItemAnimator(OvershootInterpolator(1f))
    mAdapter.setLongPressDragEnabled(false)
        .setHandleDragEnabled(false)
        .setSwipeEnabled(false)
}