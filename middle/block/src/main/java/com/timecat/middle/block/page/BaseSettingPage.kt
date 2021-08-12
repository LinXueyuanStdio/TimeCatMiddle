package com.timecat.middle.block.page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.gturedi.views.StatefulLayout
import com.timecat.middle.block.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/11
 * @description null
 * @usage null
 */
abstract class BaseSettingPage : ViewModelPage() {
    protected lateinit var container: ViewGroup
    protected lateinit var mStatefulLayout: StatefulLayout
    protected lateinit var scroll: ScrollView
    override fun createView(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val rootView = getThemedView(context, R.layout.base_page_setting, inflater)
        fragmentView = rootView
        mStatefulLayout = rootView.findViewById(R.id.ll_stateful)
        scroll = rootView.findViewById(R.id.scroll)
        container = rootView.findViewById(R.id.container)
        addSettingItems(context, container)
        return fragmentView
    }

    protected abstract fun addSettingItems(context: Context, container: ViewGroup)
}