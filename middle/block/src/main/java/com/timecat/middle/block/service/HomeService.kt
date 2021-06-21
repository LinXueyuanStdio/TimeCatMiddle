package com.timecat.middle.block.service

import android.view.View
import com.gturedi.views.StatefulLayout
import com.same.lib.core.BasePage
import com.timecat.data.room.AppRoomDatabase
import com.timecat.data.room.TimeCatRoomDatabase
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.item.BaseRecordItem
import com.timecat.middle.block.support.ChangeReminderService
import com.timecat.middle.block.support.HabitService
import com.timecat.middle.block.view.ThingAction
import eu.davidea.flexibleadapter.items.IFlexible

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
    fun itemCommonListener(): ItemCommonListener
}

interface ItemCommonListener :
    ItemActionListener,
    ItemGetterListener

interface ItemActionListener {
    fun loadFor(record: RoomRecord)
    fun onLongClick(selectPosition: Int)
    fun addAction(action: ThingAction)
    fun showMore(record: RoomRecord)
    /**
     * 打开图片 url
     * record是url的原始记录
     */
    fun showImage(url: String, record: RoomRecord)
    fun showAudio(url: String, record: RoomRecord)
    fun showVideo(url: String, record: RoomRecord)
    fun showUrl(url: String, record: RoomRecord)
    fun openPage(page: BasePage)
    fun openPage(page: BasePage, removeLast: Boolean)
    fun openPage(page: BasePage, removeLast: Boolean, forceWithoutAnimation: Boolean)
    fun loadHeader(headers: List<BaseItem<*>>)
    fun navigateTo(name: String, uuid: String, type: Int = -1)

    fun changeShowType(item: BaseRecordItem<*>, showType: Int, listener: ItemCommonListener)
    fun changeType(item: BaseRecordItem<*>, type: Int, subType: Int, listener: ItemCommonListener)
    fun addNewItemToEndOfList(record: RoomRecord, listener: ItemCommonListener)
    fun insert(position: Int, record: RoomRecord, listener: ItemCommonListener)

    fun configAdapter(enableEndless: Boolean, endlessPageSize: Int = 512, endlessScrollThreshold: Int = 4, noMoreItem: BaseItem<*>? = null)

    fun getSortType(): Int
    fun setSortType(type: Int)
    fun setTop(isTop: Boolean)
    fun focus(item: IFlexible<*>)
    fun close()
}

interface ItemGetterListener {
    fun adapter(): BaseAdapter
    fun roomClient(): TimeCatRoomDatabase
    fun roomClient(url: String): TimeCatRoomDatabase
    fun appDatabase(): AppRoomDatabase
    fun popupParentView(): View
    fun habitService(): HabitService?
    fun changeReminderService(): ChangeReminderService?
}
