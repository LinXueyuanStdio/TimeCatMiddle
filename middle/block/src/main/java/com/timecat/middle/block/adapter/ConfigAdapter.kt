package com.timecat.middle.block.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.identity.data.randomColor
import com.timecat.middle.block.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description null
 * @usage null
 */
class ConfigAdapter(data: MutableList<ConfigItem>) : BaseMultiItemQuickAdapter<ConfigItem, BaseViewHolder>(data) {

    init {
        addItemType(ConfigItem.SWITCHER_TYPE, R.layout.view_text_switch)
        addItemType(ConfigItem.ICON_TYPE, R.layout.view_text_icon)
        addItemType(ConfigItem.TEXT_TYPE, R.layout.view_text)
    }

    private fun isBright(color: Int): Boolean {
        val grayLevel = ((Color.red(color) * 30 + Color.green(color) * 59 + Color.blue(color) * 11) / 100).toDouble()
        return grayLevel >= 215
    }
    override fun convert(holder: BaseViewHolder, item: ConfigItem) {
        when (item.itemType) {
            ConfigItem.SWITCHER_TYPE -> {
                val card = holder.getView<CardView>(R.id.card)
                val backgroundColor = item.getSwitcherColor()
                card.setCardBackgroundColor(backgroundColor)
                val child = holder.getView<ViewGroup>(R.id.child)
                child.setOnLongClickListener {
                    showToolTip(it, item.getDescription())
                }

                val textColor = if (isBright(backgroundColor)) {
                    Attr.getIconColor(context)
                } else {
                    Attr.getIconColorReverse(context)
                }
                //1. text
                val text: TextView = child.getChildAt(0) as TextView
                text.text = item.getSwitchText(item.getCheck())
                text.setTextColor(textColor)
                //2. switcher
                val switcher: Switch = child.getChildAt(1) as Switch
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    switcher.thumbTintList = ColorStateList.valueOf(textColor)
                }
                switcher.isChecked = item.getCheck()
                switcher.isEnabled = item.enable()
                child.setShakelessClickListener {
                    val check = !switcher.isChecked
                    switcher.isChecked = check
                    item.listenerCheck(check)
                    text.text = item.getSwitchText(check)
                }
            }
            ConfigItem.ICON_TYPE -> {
                val card = holder.getView<CardView>(R.id.card)
                card.setCardBackgroundColor(randomColor())
                val child = holder.getView<ViewGroup>(R.id.child)

                //1. text
                val text: TextView = child.getChildAt(1) as TextView
                text.text = item.getText()
                //2. icon
                val icon: ImageView = child.getChildAt(0) as ImageView
                icon.setImageResource(item.getIconDrawableRes())
                icon.imageTintList = ColorStateList.valueOf(item.getIconColor())
                child.setOnLongClickListener {
                    showToolTip(it, item.getDescription())
                }
                child.setShakelessClickListener {
                    if (item.enable()) {
                        item.listenerClick(icon, text)
                    }
                }
            }
            ConfigItem.TEXT_TYPE -> {
                holder.setText(R.id.title, item.getDescription())
            }
            else -> {
            }
        }
    }

    private fun showToolTip(v: View, s: String): Boolean {
        ToastUtils.showLong(s)
//        ViewTooltip.on(v)
//            .autoHide(true, 1000)
//            .clickToHide(true)
//            .position(ViewTooltip.Position.BOTTOM)
//            .text(s)
//            .corner(10)
//            .arrowWidth(15)
//            .arrowHeight(15)
//            .show()
        return true
    }

}