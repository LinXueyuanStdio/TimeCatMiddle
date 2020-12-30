package com.timecat.middle.block.fragment

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SaturationBar
import com.larswerkman.holocolorpicker.ValueBar
import com.timecat.middle.block.R
import com.timecat.middle.block.view.CircleDrawable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-07
 * @description null
 * @usage null
 */
class ColorPickerPopup(
    var parent: View,
    var listener: Listener
) {
    interface Listener {
        fun onOk(color: Int)
    }

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_color_picker, null, false
    )
    private val popupWindow: PopupWindow
    private var picker: ColorPicker
    private var valueBar: ValueBar
    private var saturationBar: SaturationBar
    private var rv: RecyclerView
    private var vf: ViewFlipper
    private var change: ImageView
    private var ok: TextView

    private var curColor: Int = Color.YELLOW
    private var adapter: ColorAdapter
    private var res = parent.context.resources

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

        picker = root.findViewById(R.id.cp_colors_panel)
        valueBar = root.findViewById(R.id.cp_color_value)
        saturationBar = root.findViewById(R.id.cp_color_saturation)
        picker.addValueBar(valueBar)
        picker.addSaturationBar(saturationBar)
        picker.color = curColor
        picker.setOnColorSelectedListener {
            curColor = it
        }

        rv = root.findViewById(R.id.rv)
        vf = root.findViewById(R.id.vf)
        change = root.findViewById(R.id.change)
        ok = root.findViewById(R.id.ok)

        change.setOnClickListener {
            vf.displayedChild = (vf.displayedChild + 1) % 2
        }

        ok.setOnClickListener {
            listener.onOk(curColor)
            popupWindow.dismiss()
        }

        adapter = ColorAdapter(getColors(view.context?.theme) { curColor = it })
        rv.layoutManager = GridLayoutManager(view.context, 6)
        rv.adapter = adapter
    }

    @ColorInt
    private fun color(@ColorRes colorRes: Int, theme: Resources.Theme?): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            res.getColor(colorRes, theme)
        } else {
            res.getColor(colorRes)
        }
    }

    private fun getColors(
        theme: Resources.Theme?,
        onClick: (Int) -> Unit
    ): MutableList<ColorIndex> = mutableListOf(
        ColorIndex(Color.YELLOW, onClick = onClick),
        ColorIndex(color(R.color.black, theme), onClick = onClick),
        ColorIndex(color(R.color.material_grey_850, theme), onClick = onClick),
        ColorIndex(color(R.color.material_blue_grey_800, theme), onClick = onClick),
        ColorIndex(color(R.color.material_blue_grey_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_purple_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_deep_purple_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_indigo_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_blue_900, theme), onClick = onClick),
        ColorIndex(color(R.color.material_blue_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_light_blue_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_cyan_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_cyan_800, theme), onClick = onClick),
        ColorIndex(color(R.color.material_teal_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_green_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_light_green_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_lime_800, theme), onClick = onClick),
        ColorIndex(color(R.color.material_yellow_600, theme), onClick = onClick),
        ColorIndex(color(R.color.material_yellow_900, theme), onClick = onClick),
        ColorIndex(color(R.color.material_orange_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_deep_orange_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_pink_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_red_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_red_900, theme), onClick = onClick),
        ColorIndex(color(R.color.material_brown_700, theme), onClick = onClick),
        ColorIndex(color(R.color.material_blue_grey_100, theme), onClick = onClick),
        ColorIndex(Color.parseColor("#f3adff"), onClick = onClick),
        ColorIndex(Color.parseColor("#a9b6f9"), onClick = onClick),
        ColorIndex(Color.parseColor("#97bdfc"), onClick = onClick),
        ColorIndex(color(R.color.material_cyan_accent_100, theme), onClick = onClick),
        ColorIndex(color(R.color.material_teal_accent_100, theme), onClick = onClick),
        ColorIndex(color(R.color.material_light_green_accent_100, theme), onClick = onClick),
        ColorIndex(color(R.color.material_lime_accent_100, theme), onClick = onClick),
        ColorIndex(color(R.color.material_yellow_accent_100, theme), onClick = onClick),
        ColorIndex(color(R.color.material_orange_accent_100, theme), onClick = onClick),
        ColorIndex(Color.parseColor("#ffb5ce"), onClick = onClick),
        ColorIndex(Color.parseColor("#ffb8b2"), onClick = onClick)
    )

    data class ColorIndex(var color: Int, var selected: Boolean = false, var onClick: (Int) -> Unit)

    class ColorAdapter(data: MutableList<ColorIndex>) :
        BaseQuickAdapter<ColorIndex, BaseViewHolder>(R.layout.layout_color_small, data) {
        override fun convert(holder: BaseViewHolder, item: ColorIndex) {
            val icon: ImageView = holder.getView(R.id.color_icon)
            holder.itemView.isSelected = item.selected
            icon.setImageResource(if (item.selected) R.drawable.ic_done_white_24dp else 0)
            icon.background = CircleDrawable(item.color)
            icon.setColorFilter(Color.WHITE)
            holder.itemView.setOnClickListener {
                data.forEach { c -> c.selected = false }
                item.selected = true
                notifyDataSetChanged()
                item.onClick(item.color)
            }
        }

    }
}