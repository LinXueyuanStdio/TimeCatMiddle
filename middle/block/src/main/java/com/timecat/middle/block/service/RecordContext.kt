package com.timecat.middle.block.service

import android.content.Context
import com.google.android.material.chip.Chip
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.entity.BaseItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
interface RecordContext {
    suspend fun init(context: Context, permission: CardPermission, homeService: HomeService)

    suspend fun setInitSortTypeAndSortAsc(homeService: HomeService, sortType: Int, sortAsc: Boolean)

    suspend fun getMenu(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): MenuContext?
    suspend fun getHeader(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): List<BaseItem<*>>
    suspend fun getInputSend(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): InputContext?
    suspend fun getCommand(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): CommandContext?
    suspend fun getPanel(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): PanelContext?
    suspend fun getChipButtons(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): List<Chip>
    suspend fun getChipType(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService): List<TypeChip>

    fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        recordId: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    )

    fun loadMoreForVirtualPath(
        context: Context,
        parentUuid: String,
        recordId: String,
        offset: Int,
        homeService: HomeService,
        callback: ContainerService.LoadMoreCallback
    )
}