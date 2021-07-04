package com.timecat.middle.block.service

import android.view.View
import com.gturedi.views.StatefulLayout
import com.same.lib.core.BasePage
import com.timecat.data.room.AppRoomDatabase
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.record.RecordDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.tag.Tag
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

    /**
     * 获取当前是否播放中
     */
    fun isPlayingAudio(message: RoomRecord): Boolean
    fun playAudio(url: String, record: RoomRecord, callback:PlayAudioCallback)

    fun configAdapter(enableEndless: Boolean, endlessPageSize: Int = 512, endlessScrollThreshold: Int = 4, noMoreItem: BaseItem<*>? = null)
    fun onLongClick(selectPosition: Int)
    fun addAction(action: ThingAction)
    fun setTop(isTop: Boolean)
    fun getSortType(): Int
    fun focus(item: IFlexible<*>)
    fun close()

    fun changeShowType(item: BaseRecordItem<*>, showType: Int, listener: ItemCommonListener)
    fun changeType(item: BaseRecordItem<*>, type: Int, subType: Int, listener: ItemCommonListener)
    fun addNewItemToEndOfList(record: RoomRecord, listener: ItemCommonListener)
    fun insert(position: Int, record: RoomRecord, listener: ItemCommonListener)
    fun setSortType(type: Int, listener: ItemCommonListener)
    fun setSortAsc(ascending: Boolean, listener: ItemCommonListener)
}

interface PlayAudioCallback {
    fun setAudioSessionId( audioSessionId:Int)
    fun pause()
    fun stop()
    fun start()
}

interface ItemGetterListener {
    fun adapter(): BaseAdapter
    fun primaryDb(): IDatabase
    fun secondaryDb(url: String): IDatabase
    fun appDatabase(): AppRoomDatabase
    fun popupParentView(): View
    fun habitService(): HabitService?
    fun changeReminderService(): ChangeReminderService?
}

interface IDatabase {
    fun updateRecord(record: RoomRecord)
    fun insertRecord(record: RoomRecord)
    fun deleteRecord(record: RoomRecord)
    fun replaceRecord(record: RoomRecord)
    fun hardDeleteBatch(uuids: List<String>)
    fun deleteBatch(uuids: List<String>)
    fun archiveBatch(uuids: List<String>)
    fun updateRoomRecords(vararg record: RoomRecord)
    fun updateRoomRecords(records: List<RoomRecord>) = updateRoomRecords(*records.toTypedArray())
    fun getByUuid(uuid: String): RoomRecord?
    fun getAllLiveChildren(uuid: String): MutableList<RoomRecord>
    fun getAllRecords(): MutableList<RoomRecord>
    fun getAllTimeRecords(fromTs: Long, toTs: Long): MutableList<RoomRecord>
    fun getAllByTypeAndSubtype(type: Int, subType: Int): MutableList<RoomRecord>

    fun getAllRecordData(all: List<RoomRecord>, listener: RecordDao.OnRecordDataLoaded)
    fun getAllData(all: List<RoomRecord>, listener: RecordDao.OnDataLoaded)
    fun searchAll(query: String, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun getHabit(id: Long): Habit?

    fun getAllTags(uuid: List<String>): List<Tag>
    fun getAllTags(): List<Tag>
    fun insertTag(tag: Tag)
}
