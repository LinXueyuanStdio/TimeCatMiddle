package com.timecat.middle.block.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
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
class TagsBasePopup(
    context: Context
) : BasePopupWindow(
    context, MATCH_PARENT, WRAP_CONTENT, true
) {
    interface Listener : TagsAdapter.Listener {
        fun onManageTags()
    }

    lateinit var rv: RecyclerView
    lateinit var manage_tags: TextView
    lateinit var items: MutableList<TagItem>
    lateinit var listener: Listener

    constructor(
        context: Context,
        items: MutableList<TagItem>,
        listener: Listener
    ) : this(context) {
        this.listener = listener
        this.items = items
        delayInit()
    }

    override fun onCreateContentView(): View {
        val view: View = createPopupById(R.layout.view_base_popup_tags)
        manage_tags = view.findViewById(R.id.manage_tags)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(context)
        val animationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_on_load)
        rv.layoutAnimation = animationController
        rv.adapter = TagsAdapter(items, listener)

        manage_tags.setOnClickListener { listener.onManageTags() }
        val back: View = view.findViewById(R.id.back)
        back.setOnClickListener { dismiss() }
        return view
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