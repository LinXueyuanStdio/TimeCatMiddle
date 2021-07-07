package com.timecat.middle.block.service

import android.content.Context
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path
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
     * 加载上下文
     */
    fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService)

    /**
     * 加载列表项
     * 无尽加载模式时，则为加载第一批数据
     */
    fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: LoadCallback)
    interface LoadCallback {
        fun onEmpty(text: String, retry: () -> Unit)
        fun onError(text: String, retry: () -> Unit)
        fun onLoading(text: String, onCancel: () -> Unit)
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }

    fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: LoadMoreCallback)
    interface LoadMoreCallback {
        fun onEmpty(text: String)
        fun onError(text: String)
        fun onLoading(text: String)
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }
    /**
     * 回收当前上下文
     */
    fun onDestroy(context: Context) {}
    fun onPause(context: Context) {}
    fun onResume(context: Context) {}
}