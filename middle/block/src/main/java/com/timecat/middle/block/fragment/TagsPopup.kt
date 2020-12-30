package com.timecat.middle.block.fragment

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.TextView
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
class TagsPopup(
    var parent: View,
    var items: MutableList<TagItem>,
    var listener: Listener
) {
    interface Listener : TagsAdapter.Listener {
        fun onManageTags()
    }

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_tags, null, false
    )
    private val popupWindow: PopupWindow

    private val rv: RecyclerView
    private val manage_tags: TextView

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
        rv = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(parent.context)
        val tagsAdapter = TagsAdapter(items, listener)
        val animationController =
            AnimationUtils.loadLayoutAnimation(parent.context, R.anim.layout_on_load)
        rv.layoutAnimation = animationController
        rv.adapter = tagsAdapter

        manage_tags = view.findViewById(R.id.manage_tags)
        manage_tags.setOnClickListener { listener.onManageTags() }
        val back: View = view.findViewById(R.id.back)
        back.setOnClickListener { popupWindow.dismiss() }
    }

    data class TagItem(
        var check: Boolean,
        var color: Int,
        val name: String
    )

    class TagsAdapter(items: MutableList<TagItem>, val listener: Listener) :
        BaseQuickAdapter<TagItem, BaseViewHolder>(R.layout.rv_item_tags, items) {
        override fun convert(holder: BaseViewHolder, item: TagItem) {
                holder.setText(R.id.name, item.name)
                val c: CheckBox = holder.getView(R.id.checkbox)
                c.setOnCheckedChangeListener(null)
                c.isChecked = item.check
                c.setOnCheckedChangeListener { _, check ->
                    item.check = c.isChecked
                    listener.tags(data.filter { it.check }.map { it.name })
                }
                holder.itemView.setOnClickListener { c.isChecked = !c.isChecked }
                c.buttonTintList = ColorStateList.valueOf(item.color)
        }

        interface Listener {
            fun tags(tag: List<String>)
        }
    }

}