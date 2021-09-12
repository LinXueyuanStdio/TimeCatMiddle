package com.timecat.middle.block.service

import android.content.Context
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path

class FallbackContext : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        homeService.loadDatabase()
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        homeService.loadMenu(EmptyMenuContext())
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadHeader(listOf())
        homeService.loadChipButtons(listOf())
        homeService.reloadData()
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        callback.onEmpty("页面未实现") {}
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        callback.onEmpty("页面未实现")
    }

}
