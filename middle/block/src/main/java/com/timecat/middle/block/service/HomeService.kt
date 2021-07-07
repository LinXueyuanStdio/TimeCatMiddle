package com.timecat.middle.block.service

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import com.google.android.material.chip.Chip
import com.gturedi.views.StatefulLayout
import com.same.lib.core.ActionBarMenu
import com.same.lib.core.ActionBarMenuItem
import com.same.lib.core.BasePage
import com.timecat.data.room.AppRoomDatabase
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.record.RecordDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.tag.Tag
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.business.breadcrumb.Path
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
interface HomeService : PathContext, ItemGetterListener {
    fun actionMode(): ActionBarMenu
    fun statusMenu(): ActionBarMenuItem
    fun statefulView(): StatefulLayout?
    fun inputEditText(): EditText
    fun itemCommonListener(): ItemCommonListener

    fun reloadDatabase()
    fun reloadData()
    fun reloadData(data: List<BaseItem<*>>)

    fun restoreSelection()
    fun destroyActionMode()
    fun updateContextTitle(count: Int)

    fun currentPath(getPath: (Path) -> Unit)
}

interface PathContext {
    fun loadMenu(menuContext: MenuContext)
    fun loadHeader(headers: List<BaseItem<*>>)
    fun loadInputSend(inputContext: InputContext)
    fun loadCommand(commandContext: CommandContext)
    fun loadPanel(panelContext: PanelContext)
    fun loadChipButtons(buttons: List<Chip>)
    fun loadChipType(types: List<TypeChip>)
    fun setCurrentChipType(type: TypeChip?)
}

interface InputContext {
    fun send(selectedType: TypeChip?, text: String)
}

interface MenuContext {
    fun configActionMenu(actionMode: ActionBarMenu, actionModeViews: MutableList<View>, iconColor: Int)
    fun onActionMenuClick(actionMode: ActionBarMenu, id: Int)
    fun configStatusMenu(view: ActionBarMenuItem)
    fun onStatusMenuClick(view: ActionBarMenuItem)
}

class PanelIdentity(
    val title: String,
    val panelView: View
)

interface PanelContext {
    fun loadPanel(panels: List<PanelIdentity>)
    fun onPanelOpen(panel: PanelIdentity)
    fun onPanelClose(panel: PanelIdentity)
}

interface CommandContext {
    fun OnQuery(context: Context, query: CharSequence?, callback: CommandQueryCallback)
}

class CommandQueryCallback(
    var onError: (e: DataError) -> Unit = {},
    var onEmpty: () -> Unit = {},
    var onLoading: (progress: Int) -> Unit = {},
    var onSuccess: (records: List<BaseItem<*>>) -> Unit = {},
    var onDispatchClick: (ContextCommand) -> Unit = {}
)

open class ContextCommand(
    val onRun: (Editable) -> Boolean = { false },
)

abstract class TypeChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Chip(context, attrs, defStyleAttr) {
    abstract fun typeIcon(): Drawable

    init {
        id = View.generateViewId()
        isCheckedIconVisible = true
        isCheckable = true
        isChecked = true
    }
}

interface ItemCommonListener :
    ItemActionListener,
    ItemGetterListener

interface ItemActionListener {
    fun loadFor(record: RoomRecord)
    fun loadMore(lastPosition: Int, currentPage: Int)
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

    /**
     * 获取当前是否播放中
     */
    fun isPlayingAudio(message: RoomRecord): Boolean
    fun playAudio(url: String, record: RoomRecord, callback: PlayAudioCallback)

    fun onLongClick(selectPosition: Int)
    fun addAction(action: ThingAction)
    fun setTop(isTop: Boolean)
    fun setInitSortTypeAndSortAsc(sortType: Int, sortAsc: Boolean)
    fun getSortType(): Int
    fun getSortAsc(): Boolean
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
    fun setAudioSessionId(audioSessionId: Int)
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
    fun navigateTo(name: String, uuid: String, type: Int = -1)
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

    fun getAllLiveChildren(uuid: String, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun getAllLiveMessage(uuid: String, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun getAllRecords(order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun getAllTimeRecords(fromTs: Long, toTs: Long, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun getAllByTypeAndSubtype(type: Int, subType: Int, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord>
    fun searchAll(query: String, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord>

    fun getAllRecordData(all: List<RoomRecord>, listener: RecordDao.OnRecordDataLoaded)
    fun getAllData(all: List<RoomRecord>, listener: RecordDao.OnDataLoaded)

    fun getHabit(id: Long): Habit?

    fun getAllTags(uuid: List<String>): List<Tag>
    fun getAllTags(): List<Tag>
    fun insertTag(tag: Tag)
}
