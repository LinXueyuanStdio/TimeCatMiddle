package com.timecat.middle.block.adapter.vh

import android.view.View
import com.timecat.middle.block.ext.bindSelected
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
abstract class AbsCardVH(v: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(v, adapter) {
    lateinit var avatar: FlipView

    override fun toggleActivation() {
        super.toggleActivation()
        val selected = mAdapter.isSelected(adapterPosition)
        avatar.flip(selected)
    }

    fun bindSelected(isSelected: Boolean, icon: String, color: Int) {
        avatar.bindSelected(isSelected, icon, color)
    }
}