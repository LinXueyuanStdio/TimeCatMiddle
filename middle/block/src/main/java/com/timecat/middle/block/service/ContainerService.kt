package com.timecat.middle.block.service

import android.content.Context
import com.timecat.layout.ui.entity.BaseItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/20
 * @description null
 * @usage null
 */
interface ContainerService {
    fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: LoadCallback)
    interface LoadCallback {
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }
}