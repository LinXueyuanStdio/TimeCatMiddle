package com.timecat.middle.block.adapter.vh

import android.content.Context
import android.view.View
import com.timecat.layout.ui.layout.dp
import com.timecat.middle.block.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.ExpandableViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/20
 * @description null
 * @usage null
 */
open class BaseRecordCardVH(view: View, adapter: FlexibleAdapter<*>) : ExpandableViewHolder(view, adapter) {
    var mContext: Context = view.context
    val container: View = view.findViewById(R.id.front_view)

    final override fun setDragHandleView(view: View) {
        super.setDragHandleView(view)
    }

    override fun getActivationElevation(): Float {
        return 10.dp.toFloat()
    }

    override fun shouldActivateViewWhileSwiping(): Boolean {
        return false
    }

    override fun shouldAddSelectionInActionMode(): Boolean {
        return false
    }

    override fun expandView(position: Int) {
        super.expandView(position)
        this.mAdapter.invalidateItemDecorations(100L)
    }

    override fun collapseView(position: Int) {
        super.collapseView(position)
        this.mAdapter.invalidateItemDecorations(100L)
    }

    override fun getFrontView(): View = container
    override fun getRearLeftView(): View? = null
    override fun getRearRightView(): View? = null
}
