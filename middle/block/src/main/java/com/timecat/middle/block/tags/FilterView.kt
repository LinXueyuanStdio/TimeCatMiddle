package com.timecat.middle.block.tags

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.data.room.TimeCatRoomDatabase
import com.timecat.data.room.tag.Tag
import com.timecat.identity.data.block.type.*
import com.timecat.middle.block.R
import com.timecat.middle.block.view.ColorView
import kotlin.math.min

class FilterView(
    val context: Context,
    private val flexbox: FlexboxLayout,
    val state: FilterState,
    val onTagClick: (Tag) -> Unit,
    val onColorClick: (Int) -> Unit,
    val onSubTypeClick: (Int) -> Unit
) {

    val tags = emptySet<Tag>().toMutableSet()
    val colors = emptySet<Int>().toMutableSet()
    val types = emptySet<Int>().toMutableSet()

    fun reset() {
        tags.clear()
        tags.addAll(TimeCatRoomDatabase.forFile(context).tagDao().all)

        colors.clear()
        colors.addAll(
            TimeCatRoomDatabase.forFile(context).recordDao().getAll_BLOCK_RECORD().map { it.color })

        types.clear()
        types.addAll(
            listOf(
                BLOCK_CONTAINER,
                BLOCK_CONVERSATION,
                BLOCK_RECORD
            )
        )
    }

    fun notifyChanged() {
        setViews()
    }

    @Synchronized
    fun setViews() {
        flexbox.removeAllViews()
        setTags()
        setColors()
        setTypes()
    }

    private fun setTags() {
        tags.forEach {
                val tag = it
                val tagView = View.inflate(context, R.layout.layout_flexbox_tag_item, null) as View
                val text = tagView.findViewById<TextView>(R.id.tag_text)

                if (state.tags.any { stateTag -> stateTag.uuid == tag.uuid }) {
                    text.setBackgroundResource(R.drawable.flexbox_selected_tag_item_bg)
                    text.setTextColor(Attr.getPrimaryTextColor(context))
                }

                text.text = it.title
                tagView.setOnClickListener {
                    onTagClick(tag)
                }
                flexbox.addView(tagView)
            }
    }

    private fun setColors() {
        colors.forEach {
                val color = it
                val colorView = ColorView(context, R.layout.layout_color)
                colorView.setColor(color, state.colors.contains(color))
                colorView.setOnClickListener {
                    onColorClick(color)
                }
                flexbox.addView(colorView)
            }
    }

    private fun setTypes() {
        types.forEach { type ->
                val subTypeName = when (type) {
                    BLOCK_CONTAINER -> "文件夹"
                    BLOCK_RECORD -> "时间笔记"
                    BLOCK_CONVERSATION -> "对话笔记"
                    BLOCK_LINK -> "指针"
                    BLOCK_BUTTON -> "按钮"
                    BLOCK_MEDIA -> "媒体块"
                    BLOCK_PATH -> "传送门"
                    else -> "未知类型"
                }
                val subTypeIcon = when (type) {
                    BLOCK_CONTAINER -> R.drawable.ic_collect
                    BLOCK_RECORD -> R.drawable.every_drawer_note
                    BLOCK_CONVERSATION -> R.drawable.ic_comment
                    BLOCK_LINK -> R.drawable.ic_arrow_right
                    BLOCK_BUTTON -> R.drawable.ic_toolbox_24dp
                    BLOCK_MEDIA -> R.drawable.ic_audio_white_24dp
                    BLOCK_PATH -> R.drawable.ic_pin
                    else -> R.drawable.every_drawer_note
                }
                val c = Chip(context)
                c.setTextColor(Attr.getColor(context, R.color.teal_700))
                c.setChipBackgroundColorResource(R.color.teal_50)
                c.setChipIconTintResource(R.color.teal_600)
                c.setCheckedIconResource(R.drawable.ic_done_white_24dp)
                c.setChipIconSizeResource(R.dimen.icon_size)

                val check = state.domain.type.any { it == type }
                c.setChipIconResource(if (!check) subTypeIcon else R.drawable.ic_done_white_24dp)
                c.text = subTypeName
                c.isChecked = state.domain.type.any { it == type }
                c.setOnClickListener {
                    onSubTypeClick(type)
                }

                val container = LinearLayout(context)
                container.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val dim = context.resources.getDimensionPixelSize(R.dimen.spacing_xsmall)
                container.setPadding(dim, 0, dim, 0)
                container.addView(c)
                c.updateLayoutParams<LinearLayout.LayoutParams> {
                    gravity = Gravity.CENTER
                }
                flexbox.addView(container)
            }
    }
}