package com.timecat.middle.block.service

import androidx.appcompat.app.AppCompatActivity
import com.gturedi.views.StatefulLayout
import com.timecat.layout.ui.entity.BaseItem
import eu.davidea.flexibleadapter.helpers.ActionModeHelper

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/20
 * @description null
 * @usage null
 */
interface HomeService {
    fun databaseReload()
    fun reload()
    fun reload(data: List<BaseItem<*>>)
    fun navigateTo(name: String, uuid: String, type: Int = -1)
    fun statefulView(): StatefulLayout?
}
