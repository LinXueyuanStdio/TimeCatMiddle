package com.timecat.middle.block.service

import android.content.Context
import com.google.android.material.chip.Chip
import com.timecat.layout.ui.entity.BaseItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/20
 * @description null
 * @usage null
 */
interface ContainerService {
    /**
     * 加载列表项
     * 无尽加载模式时，则为加载第一批数据
     */
    fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: LoadCallback)
    interface LoadCallback {
        fun onEmpty(text: String, retry:()->Unit)
        fun onError(text: String, retry:()->Unit)
        fun onLoading(text: String, onCancel:()->Unit)
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }

    /**
     * 加载容器按钮
     */
    fun loadContainerButton(context: Context, parentUuid: String, homeService: HomeService, callback: LoadButton)
    interface LoadButton {
        fun onEmpty(text: String, retry:()->Unit)
        fun onError(text: String, retry:()->Unit)
        fun onLoading(text: String, onCancel:()->Unit)
        fun onLoadSuccess(items: List<Chip>)
    }

    fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: LoadMoreCallback)
    interface LoadMoreCallback {
        fun onEmpty(text: String)
        fun onError(text: String)
        fun onLoading(text: String)
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }
}