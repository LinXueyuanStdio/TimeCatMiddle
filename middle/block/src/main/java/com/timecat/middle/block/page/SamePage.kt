package com.timecat.middle.block.page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import com.same.lib.core.ActionBar
import com.same.lib.same.theme.BaseSamePage
import com.same.lib.util.ColorManager
import com.same.lib.util.KeyHub
import com.same.lib.util.Space

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/9
 * @description null
 * @usage null
 */
abstract class SamePage : BaseSamePage() {

    protected fun buildActionBar(context: Context): ActionBar {
        val actionBar = ActionBar(context)
        actionBar.setBackgroundColor(ColorManager.getColor(KeyHub.key_actionBarDefault))
        actionBar.setItemsBackgroundColor(ColorManager.getColor(KeyHub.key_actionBarDefaultSelector), false)
        actionBar.setItemsBackgroundColor(ColorManager.getColor(KeyHub.key_actionBarActionModeDefaultSelector), true)
        actionBar.setItemsColor(ColorManager.getColor(KeyHub.key_actionBarDefaultIcon), false)
        actionBar.setItemsColor(ColorManager.getColor(KeyHub.key_actionBarActionModeDefaultIcon), true)
        if (inPreviewMode) {
            actionBar.occupyStatusBar = false
        }
        if (Space.isTablet()) {
            actionBar.occupyStatusBar = false
        }
        actionBar.setAllowOverlayTitle(false)
        return actionBar
    }
    protected fun getThemedLayoutInflater(
        origin: Context,
        inflater: LayoutInflater
    ): LayoutInflater {
        val contextThemeWrapper: Context = ContextThemeWrapper(origin, origin.theme)
        val themeAwareInflater = inflater.cloneInContext(contextThemeWrapper)
        return themeAwareInflater
    }

    protected fun getThemedView(
        origin: Context,
        @LayoutRes layout: Int,
        inflater: LayoutInflater,
        container: ViewGroup? = null
    ): View {
        val themeAwareInflater = getThemedLayoutInflater(origin, inflater)
        return themeAwareInflater.inflate(layout, container, false)
    }
}