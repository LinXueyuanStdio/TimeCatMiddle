package com.timecat.middle.block.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.vh.BaseRecordCardVH
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/21
 * @description null
 * @usage null
 */
class FooterCard(
    record: String
) : BaseItem<FooterCard.RecordCardVH>(record) {

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FooterCard.RecordCardVH {
        return RecordCardVH(view, adapter)
    }

    override fun getLayoutRes(): Int = R.layout.card_virtual_footer

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: FooterCard.RecordCardVH,
        position: Int,
        payloads: List<Any>
    ) {
        holder.title.setText(id)
    }

    inner class RecordCardVH(
        v: View,
        adapter: FlexibleAdapter<*>
    ) : BaseRecordCardVH(v, adapter) {
        var title: TextView = v.findViewById(R.id.title)

        init {
            title.setTextColor(Attr.getSecondaryTextColor(v.context))
        }
    }
}