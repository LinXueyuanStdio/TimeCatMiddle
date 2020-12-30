package com.timecat.middle.block.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.middle.block.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class AttachmentDetailPopup(
    var parent: View,
    var items: MutableList<Pair<String, String>>
) {

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_attachment_detail, null, false
    )
    private val popupWindow: PopupWindow

    init {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        popupWindow.animationStyle = R.style.popup_window_animation
        popupWindow.showAtLocation(parent, 17, 0, 0)
        val root: View = view.findViewById(R.id.root)
        root.setOnClickListener { popupWindow.dismiss() }

        val mAccentColor = view.context.resources.getColor(R.color.master_icon_view_special)
        val title: TextView = view.findViewById(R.id.tv_title_attachment_info)
        title.setTextColor(mAccentColor)
        val confirm: TextView = view.findViewById(R.id.tv_confirm_as_bt_attachment_info)
        confirm.setTextColor(mAccentColor)
        confirm.setOnClickListener { popupWindow.dismiss() }
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_attachment_info)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = Adapter(items)
    }

    internal class Adapter(items: MutableList<Pair<String, String>>) :
        BaseQuickAdapter<Pair<String, String>, BaseViewHolder>(R.layout.rv_attachment_info, items) {
        override fun convert(holder: BaseViewHolder, item: Pair<String, String>) {
                holder.setText(R.id.tv_rv_attachment_info_title, item.first)
                holder.setText(R.id.tv_rv_attachment_info_content, item.second)
        }
    }
}