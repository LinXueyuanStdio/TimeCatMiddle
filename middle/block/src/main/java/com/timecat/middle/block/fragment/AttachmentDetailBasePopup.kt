package com.timecat.middle.block.fragment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.middle.block.R
import razerdp.basepopup.BasePopupWindow

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class AttachmentDetailBasePopup(context: Context) : BasePopupWindow(
    context, MATCH_PARENT, WRAP_CONTENT, true
) {

    constructor(
        context: Context,
        items: MutableList<Pair<String, String>>
    ) : this(context) {
        adapter = Adapter(items)
        delayInit()
    }

    private lateinit var adapter: Adapter
    override fun onCreateContentView(): View {
        val view: View = createPopupById(R.layout.view_base_popup_attachment_detail)
        val confirm: TextView = view.findViewById(R.id.tv_confirm_as_bt_attachment_info)
        confirm.setOnClickListener { dismiss() }
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_attachment_info)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        return view
    }

    internal class Adapter(items: MutableList<Pair<String, String>>) :
        BaseQuickAdapter<Pair<String, String>, BaseViewHolder>(R.layout.rv_attachment_info, items) {
        override fun convert(holder: BaseViewHolder, item: Pair<String, String>) {
                holder.setText(R.id.tv_rv_attachment_info_title, item.first)
                holder.setText(R.id.tv_rv_attachment_info_content, item.second)
        }
    }
}