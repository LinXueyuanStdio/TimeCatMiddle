package com.timecat.middle.block.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.identity.Attr
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.vh.DragRecordCardVH
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.item.BaseRecordItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.joda.time.DateTime

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/8/17
 * @description RoomRecord
 * @usage null
 */
class UnknownCard(
    record: RoomRecord,
    val context: Context
) : BaseRecordItem<UnknownCard.RecordCardVH>(record) {

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): UnknownCard.RecordCardVH {
        return RecordCardVH(view, adapter)
    }

    override fun getLayoutRes(): Int = R.layout.card_unknown

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: UnknownCard.RecordCardVH,
        position: Int,
        payloads: List<Any>
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        if (adapter is BaseAdapter) {
            adapter.bindViewHolderAnimation(holder)
        }
        holder.title.setText(R.string.im_unknown_msg)
        holder.content.text = DateTime(record.updateTime).toString("更新于 MM 月 dd 日 HH:mm")
        holder.frontView.setShakelessClickListener {
            context.showDialog {
                title(text = "未知类型")
                message(text = record.toJson())
                positiveButton(R.string.ok)
                negativeButton(R.string.cancel)
            }
        }
    }

    inner class RecordCardVH(
        v: View,
        adapter: FlexibleAdapter<*>
    ) : DragRecordCardVH(v, adapter) {
        var title: TextView = v.findViewById(R.id.title)
        var content: TextView = v.findViewById(R.id.state)
        var mState: TextView = v.findViewById(R.id.delay)

        init {
            avatar = v.findViewById(R.id.avatar)

            title.setTextColor(Attr.getPrimaryTextColor(context))
            content.setTextColor(Attr.getSecondaryTextColor(context))
        }
    }

    init {
        isDraggable = false
    }
}