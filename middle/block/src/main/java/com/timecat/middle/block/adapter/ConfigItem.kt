package com.timecat.middle.block.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description null
 * @usage null
 */
data class ConfigItem(
    override val itemType: Int,
    var spanSize: Int,
    var getDescription: () -> String,

    var getText: () -> String = { "" },
    var getIconColor: () -> Int = { 0 },
    var getIconDrawableRes: () -> Int = { 0 },
    var listenerClick: (ImageView, TextView) -> Unit = { _, _ -> },

    var getSwitchText: (Boolean) -> String = { _ -> "" },
    var getSwitcherColor: () -> Int = { 0 },
    var getCheck: () -> Boolean = { false },
    var listenerCheck: (Boolean) -> Unit = {},

    var enable: () -> Boolean = { true }
) : MultiItemEntity {

    companion object {
        const val SWITCHER_TYPE = 0
        const val ICON_TYPE = 1
        const val TEXT_TYPE = 2
        fun configSwitcher(
            enable: () -> Boolean,
            getSwitcherColor: () -> Int,
            getCheck: () -> Boolean,
            getText: (Boolean) -> String,
            getDescription: () -> String,
            listenerCheck: (Boolean) -> Unit
        ): ConfigItem = ConfigItem(
            itemType = SWITCHER_TYPE,
            spanSize = 2,
            enable = enable,
            getSwitcherColor = getSwitcherColor,
            getCheck = getCheck,
            getSwitchText = getText,
            getDescription = getDescription,
            listenerCheck = listenerCheck
        )

        fun configIcon(
            enable: () -> Boolean,
            getIconColor: () -> Int,
            getIconDrawableRes: () -> Int,
            getText: () -> String,
            getDescription: () -> String,
            listenerClick: (ImageView, TextView) -> Unit
        ): ConfigItem = ConfigItem(
            itemType = ICON_TYPE,
            spanSize = 2,
            enable = enable,
            getIconColor = getIconColor,
            getIconDrawableRes = getIconDrawableRes,
            getText = getText,
            getDescription = getDescription,
            listenerClick = listenerClick
        )

        fun configText(getDescription: () -> String): ConfigItem = ConfigItem(
            itemType = TEXT_TYPE,
            spanSize = 4,
            getDescription = getDescription
        )
    }
}