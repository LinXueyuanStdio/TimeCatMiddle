package com.timecat.middle.block.adapter.vh

import android.view.View
import android.widget.ImageView
import com.timecat.middle.block.R
import eu.davidea.flexibleadapter.FlexibleAdapter

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
abstract class DragRecordCardVH(
    v: View,
    adapter: FlexibleAdapter<*>
) : AbsRecordCardVH(v, adapter) {
    var more: ImageView = v.findViewById(R.id.more)

    init {
        setDragHandleView(more)
    }
}