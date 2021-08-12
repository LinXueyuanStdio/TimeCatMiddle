package com.timecat.middle.block.page

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.android.material.button.MaterialButton
import com.same.lib.core.ActionBar
import com.same.lib.drawable.BackDrawable
import com.same.lib.helper.LayoutHelper
import com.same.lib.util.Space
import com.timecat.layout.ui.layout.*
import com.timecat.middle.block.R
import com.timecat.middle.block.page.BaseSettingPage

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/11
 * @description null
 * @usage null
 */
abstract class BaseNewPage : BaseSettingPage() {
    lateinit var btnOk: MaterialButton
    override fun createActionBar(context: Context): ActionBar {
        val actionBar = buildActionBar(context)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                finishFragment(true)
            }
        })

        actionBar.setBackButtonDrawable(BackDrawable(true))
        actionBar.setTitle(title())

        btnOk = MaterialButton(context)
        btnOk.setText(R.string.ok)
        btnOk.setShakelessClickListener {
            ok()
        }

        val btnContainer = RelativeLayout(context)
        btnContainer.addView(
            btnOk,
            LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                0, 0, 16, 0).apply {
                addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }
        )
        actionBar.addView(
            btnContainer,
            0,
            LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT,
                Gravity.LEFT or Gravity.TOP)
        )
        (btnContainer.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            topMargin = Space.statusBarHeight
        }
        return actionBar
    }
    abstract fun ok()
    abstract fun title():String
}