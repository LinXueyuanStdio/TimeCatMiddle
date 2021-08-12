package com.timecat.middle.block.ext

import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.timecat.middle.block.R
import com.vmloft.develop.library.tools.utils.VMStr

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/12
 * @description null
 * @usage null
 */
data class FloatMenuItem(
    val title: String,
    val condition: () -> Boolean,
    val menuItemConfig: MenuItem.() -> Unit = {},
    var isSubMenu: Boolean = false,
    var subMenuItemConfig: SubMenu.() -> Unit = {},
    val icon: Int = 0,
)


fun simpleItem(
    icon: Int,
    title: String,
    condition: () -> Boolean,
    onClick: () -> Unit
): FloatMenuItem {
    return FloatMenuItem(title, condition, {
        setIcon(icon)
        setOnMenuItemClickListener {
            onClick()
            true
        }
    })
}

fun simpleItem(
    icon: Int,
    title: Int,
    condition: () -> Boolean,
    onClick: () -> Unit
): FloatMenuItem {
    return simpleItem(icon, VMStr.byRes(title), condition, onClick)
}

fun deleteItem(onClick: () -> Unit): FloatMenuItem {
    return FloatMenuItem(VMStr.byRes(R.string.delete), { true }, {
        setOnMenuItemClickListener {
            onClick()
            true
        }
    }, icon = R.drawable.ic_delete_red_24dp)
}

fun duplicateItem(onClick: () -> Unit): FloatMenuItem {
    return FloatMenuItem("复用", { true }, {
        setOnMenuItemClickListener {
            onClick()
            true
        }
    }, icon = R.drawable.ic_copy)
}

fun editItem(onClick: () -> Unit): FloatMenuItem {
    return FloatMenuItem(VMStr.byRes(R.string.edit), { true }, {
        setOnMenuItemClickListener {
            onClick()
            true
        }
    }, icon = R.drawable.ic_edit)
}

fun beVisibleItem(onClick: () -> Unit): FloatMenuItem {
    return FloatMenuItem("可见", { true }, {
        setOnMenuItemClickListener {
            onClick()
            true
        }
    }, icon = R.drawable.ic_eye)
}

fun beInVisibleItem(onClick: () -> Unit): FloatMenuItem {
    return FloatMenuItem("不可见", { true }, {
        setOnMenuItemClickListener {
            onClick()
            true
        }
    }, icon = R.drawable.ic_eye_off)
}


fun setupFloatMenu(
    clickTarget: View,
    anchor: View,
    items: () -> List<FloatMenuItem>
) {
    clickTarget.setOnLongClickListener {
        showFloatMenu(anchor, items)
    }
}

/**
 * 菜单
 */
fun showFloatMenu(
    anchor: View,
    items: () -> List<FloatMenuItem>
): Boolean {
    PopupMenu(anchor.context, anchor).apply {
        items().filter { it.condition() }.forEach {
            if (it.isSubMenu) {
                val m = menu.addSubMenu(it.title)
                if (it.icon != 0) {
                    m.setIcon(it.icon)
                }
                m.apply(it.subMenuItemConfig)
            } else {
                val m = menu.add(it.title)
                if (it.icon != 0) {
                    m.setIcon(it.icon)
                }
                m.apply(it.menuItemConfig)
            }
        }
        show()
    }
    return true
}
