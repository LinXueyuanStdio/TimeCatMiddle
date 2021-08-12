package com.timecat.middle.block.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.ColorUtils
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.vh.AbsRecordCardVH
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/21
 * @description null
 * @usage null
 */
open class SubTypeCard(
    val item: SubItem,
    val context: Context,
    val listener: Listener
) : BaseItem<SubTypeCard.TypeCardVH>(item.uuid) {
    class TypeCardVH(v: View, adapter: FlexibleAdapter<*>) : AbsRecordCardVH(v, adapter) {
        val title: TextView = v.findViewById(R.id.title)
        val mTimerState: TextView = v.findViewById(R.id.state)
        val mHint: View = v.findViewById(R.id.container_hint)
        val frontView: ConstraintLayout = v.findViewById(R.id.front_view)
        val divider: View = v.findViewById(R.id.divider)
        val subType: ImageView = v.findViewById(R.id.sub_type)
        val more: ImageView = v.findViewById(R.id.more)
        val delay: TextView = v.findViewById(R.id.delay)

        init {
            avatar = v.findViewById(R.id.avatar)
        }
    }

    interface Listener {
        fun loadFor(subItem: SubItem)
        fun more(subItem: SubItem)
    }

    override fun getLayoutRes(): Int = R.layout.card_virtual_sub_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): TypeCardVH {
        return TypeCardVH(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: TypeCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        if (adapter is BaseAdapter) {
            adapter.bindViewHolderAnimation(holder)
        }

        val selected = adapter.isSelected(position)
        val icon = item.icon
        val color = ColorUtils.randomColor()
        holder.bindSelected(selected, icon, color)
        if (item.isDir) {
            val drawable = Attr.tintDrawable(context, R.drawable.shape_rect_accent, color)
            holder.mHint.background = drawable
            holder.mHint.beVisible()
            holder.subType.beVisible()
        } else {
            holder.mHint.beGone()
            holder.subType.beGone()
        }

        holder.title.text = item.title
        holder.mTimerState.text = item.desc
        holder.delay.text = item.typeName
        holder.more.setShakelessClickListener {
            listener.more(item)
        }

        holder.frontView.setShakelessClickListener {
            listener.loadFor(item)
        }
    }
}