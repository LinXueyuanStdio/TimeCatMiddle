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
    fun init(context: Context, permission: CardPermission)

    fun getMenu(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, menuContext: (MenuContext) -> Unit)
    fun getHeader(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, headers: (List<BaseItem<*>>) -> Unit)
    fun getInputSend(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, inputContext: (InputContext) -> Unit)
    fun getCommand(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, commandContext: (CommandContext) -> Unit)
    fun getPanel(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, panelContext: (PanelContext) -> Unit)
    fun getChipButtons(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, buttons: (List<Chip>) -> Unit)
    fun getChipType(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService, types: (List<TypeChip>) -> Unit)

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