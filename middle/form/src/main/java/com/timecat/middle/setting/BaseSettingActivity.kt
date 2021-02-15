package com.timecat.middle.setting

import android.view.ViewGroup
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.*
import com.timecat.layout.ui.business.setting.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description 设置
 * @usage 继承本类，然后实现addSettingItems方法。按顺序写设置项即可。
 */
abstract class BaseSettingActivity : BaseToolbarActivity() {
    protected lateinit var container: ViewGroup
    override fun layout(): Int = R.layout.middle_activity_setting
    override fun initView() {
        container = findViewById(R.id.container)
        addSettingItems(container)
    }

    protected abstract fun addSettingItems(container: ViewGroup)
}