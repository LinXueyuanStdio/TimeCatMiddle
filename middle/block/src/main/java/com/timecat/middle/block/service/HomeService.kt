package com.timecat.middle.block.service

import android.content.Context
import android.view.View
import com.gturedi.views.StatefulLayout
import com.same.lib.core.BasePage
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
    fun openPage(page: BasePage)
    fun openPage(page: BasePage, removeLast:Boolean)
    fun openPage(page: BasePage, removeLast:Boolean, forceWithoutAnimation:Boolean)

    fun setTop(isTop: Boolean)
    fun changeShowType(item: BaseRecordItem<*>, showType: Int)
    fun changeType(item: BaseRecordItem<*>, type: Int, subType: Int)
    fun addNewItemToEndOfList(record: RoomRecord)
    fun insert(position: Int, record: RoomRecord)
    fun duplicate(item: BaseRecordItem<*>): Boolean
    fun getSortType(): Int
    fun setSortType(type: Int)
    fun configAdapter(enableEndless: Boolean, endlessPageSize: Int = 512, endlessScrollThreshold: Int = 4, noMoreItem: BaseItem<*>? = null)

    fun focus(item: IFlexible<*>)
    fun close()
}

interface ItemGetterListener {
    fun adapter(): BaseAdapter
    fun popupParentView(): View
    fun habitService(): HabitService?
    fun changeReminderService(): ChangeReminderService?
}
