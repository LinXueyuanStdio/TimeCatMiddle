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
     * 加载前置上下文
     *
     * 这里需要加载的上下文包括
     * 1. contextDatabase:IDatabase（非空）
     * 2. ContextRecord:RoomRecord（可空）
     *
     * 该函数需要完成的任务解释：
     *
     * 1. contextDatabase:IDatabase 用来获取和保存 ContextRecord:RoomRecord
     *    加载好的 contextDatabase:IDatabase 将被传递给 homeService.itemCommonListener().primaryDb()
     *    加载好的 ContextRecord:RoomRecord 将被传递给 loadContext 函数的 record:RoomRecord 字段
     *
     * 2. 当 ContextRecord 定义了额外的 IDatabase（记为 subDatabase:IDatabase ）时，
     *    则在 loadContext 中使用 record:RoomRecord 构建其 subDatabase:IDatabase
     *    subDatabase:IDatabase 可用于 loadData / loadMoreData
     */
    fun loadContextRecord(
        path: Path,
        context: Context,
        parentUuid: String,
        homeService: HomeService
    )

    suspend fun loadContextRecordByDefault(
        path: Path,
        context: Context,
        parentUuid: String,
        homeService: HomeService
    ) {
        //这是默认实现。如果不需要，请务必不要调用 super
        if (parentUuid.isEmpty()) {
            homeService.loadContextRecord(null)
        } else {
            if (parentUuid.startsWith(DNS.SCHEMA)) {
                val (dbPath, recordId) = DNS.parsePath(parentUuid)
                val (permission, remoteDb) = homeService.secondaryDb(dbPath)
                homeService.loadDatabase(dbPath, permission, remoteDb)
                if (recordId.isEmpty()) {
                    homeService.loadContextRecord(null)
                } else if (permission.noAccess()){
                    homeService.loadContextRecord(null)
                } else {
                    val record = remoteDb.getByUuid(recordId)
                    homeService.loadContextRecord(record)
                }
            } else {
                val permission = homeService.getPermission()
                if (permission.noAccess()){
                    homeService.loadContextRecord(null)
                } else {
                    val currentDb = homeService.primaryDb()
                    val recordId = parentUuid
                    val record = currentDb.getByUuid(recordId)
                    homeService.loadContextRecord(record)
                }
            }
        }
    }

    /**
     * 加载上下文
     */
    fun loadContext(
        path: Path,
        context: Context,
        parentUuid: String,
        record: RoomRecord?,
        homeService: HomeService
    )

    /**
     * 加载列表项
     * 无尽加载模式时，则为加载第一批数据
     */
    fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: LoadCallback
    )

    interface LoadCallback {
        fun onEmpty(text: String, retry: () -> Unit)
        fun onError(text: String, retry: () -> Unit)
        fun onLoading(text: String, onCancel: () -> Unit)
        fun onVirtualLoadSuccess(items: List<BaseItem<*>>)
    }

    fun loadMoreForVirtualPath(
        context: Context,
        parentUuid: String,
        offset: Int,
        homeService: HomeService,
        callback: LoadMoreCallback
    )

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