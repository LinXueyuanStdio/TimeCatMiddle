package com.timecat.middle.block.service

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.chip.Chip
import com.gturedi.views.StatefulLayout
import com.same.lib.core.ActionBarMenu
import com.same.lib.core.ActionBarMenuItem
import com.same.lib.core.BasePage
import com.timecat.data.room.AppRoomDatabase
import com.timecat.data.room.record.RoomRecord
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.item.BaseRecordItem
import com.timecat.middle.block.support.ChangeReminderService
import com.timecat.middle.block.support.HabitService
import com.timecat.middle.block.view.ThingAction
import eu.davidea.flexibleadapter.items.IFlexible
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/20
 * @description null
 * @usage null
 */
interface HomeService : DatabaseContext, PathContext, ItemGetterListener {
    fun actionMode(): ActionBarMenu
    fun statusMenu(): ActionBarMenuItem
    fun statefulView(): StatefulLayout?
    fun inputEditText(): EditText
    fun itemCommonListener(): ItemCommonListener
    fun viewModelStoreOwner(): ViewModelStoreOwner

    fun reloadDatabase()
    fun reloadData()
    fun reloadData(data: List<BaseItem<*>>)

    fun closeDrawerIfCan()
    fun restoreSelection()
    fun destroyActionMode()
    fun updateContextTitle(count: Int)

    fun currentPath(getPath: (Path) -> Unit)
}

interface DatabaseContext {
    fun loadDatabase(url: String, database: IDatabase)
    fun loadContextRecord(record: RoomRecord?)
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

open class EmptyInputContext : InputContext {
    override fun send(selectedType: TypeChip?, text: String) {}
}

open class EmptyMenuContext : MenuContext {
    override fun configActionMenu(
        actionMode: ActionBarMenu,
        actionModeViews: MutableList<View>,
        iconColor: Int
    ) {
    }

    override fun onActionMenuClick(actionMode: ActionBarMenu, id: Int) {}
    override fun configStatusMenu(view: ActionBarMenuItem) {}
    override fun onStatusMenuClick(view: ActionBarMenuItem) {}
    override fun updateRoomRecords(record: List<RoomRecord>) {}
}

open class EmptyPanelContext : PanelContext {
    override fun loadPanel(): List<PanelIdentity> = emptyList()
    override fun onPanelOpen(panel: PanelIdentity) {}
    override fun onPanelClose(panel: PanelIdentity) {}
}

open class EmptyCommandContext : CommandContext {
    override fun OnQuery(context: Context, query: CharSequence?, callback: CommandQueryCallback) {}
}

interface InputContext {
    fun send(selectedType: TypeChip?, text: String)
}

interface MenuContext {
    fun configActionMenu(
        actionMode: ActionBarMenu,
        actionModeViews: MutableList<View>,
        iconColor: Int
    )

    fun onActionMenuClick(actionMode: ActionBarMenu, id: Int)
    fun configStatusMenu(view: ActionBarMenuItem)
    fun onStatusMenuClick(view: ActionBarMenuItem)
    fun updateRoomRecords(record: List<RoomRecord>)
}

class PanelIdentity(
    val title: String,
    val panelView: View
)

interface PanelContext {
    fun loadPanel(): List<PanelIdentity>
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
    fun secondaryDb(url: String, callback: LoadDbCallback)
    fun secondaryDb(url: String, callback: (IDatabase) -> Unit) {
        secondaryDb(url, object : SimpleLoadDbCallback() {
            override fun onSuccess(remoteDb: IDatabase) {
                callback(remoteDb)
            }
        })
    }

    suspend fun secondaryDb(url: String): IDatabase? = suspendCoroutine {
        secondaryDb(url, object : LoadDbCallback {
            override fun onSuccess(remoteDb: IDatabase) {
                it.resume(remoteDb)
            }

            override fun onFail(text: String, retry: () -> Unit) {
                it.resume(null)
            }
        })
    }

    fun contextRecord(): RoomRecord?
    fun appDatabase(): AppRoomDatabase
    fun popupParentView(): View
    fun habitService(): HabitService?
    fun changeReminderService(): ChangeReminderService?
    fun navigateTo(name: String, uuid: String, type: Int = -1)
    fun resetTo(path: Path)
}

interface LoadDbCallback {
    fun onSuccess(remoteDb: IDatabase)
    fun onFail(text: String, retry: () -> Unit)
}

abstract class SimpleLoadDbCallback : LoadDbCallback {
    override fun onFail(text: String, retry: () -> Unit) {
        ToastUtil.e_long(text)
    }
}

interface IDatabase {
    fun deleteBatch(record: List<RoomRecord>) {
        record.forEach {
            it.setDeleted(true)
        }
        updateRoomRecords(record)
    }

    fun archiveBatch(record: List<RoomRecord>) {
        record.forEach {
            it.setArchived(true)
        }
        updateRoomRecords(record)
    }

    fun updateRoomRecords(vararg record: RoomRecord) = updateRoomRecords(record.toList())

    fun updateRecord(record: RoomRecord)
    suspend fun insertRecord(record: RoomRecord): RoomRecord? = suspendCoroutine { cb ->
        insertRecord(record) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(null)
            }
            onError = {
                cb.resume(null)
            }
        }
    }

    fun insertRecord(
        record: RoomRecord,
        callback: RequestSingleOrNullCallback<RoomRecord>.() -> Unit
    )

    fun deleteRecord(record: RoomRecord)
    fun replaceRecord(record: RoomRecord)
    fun hardDeleteBatch(record: List<RoomRecord>)
    fun updateRoomRecords(records: List<RoomRecord>)

    suspend fun getByUuid(uuid: String): RoomRecord? = suspendCoroutine { cb ->
        getByUuid(uuid) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(null)
            }
            onError = {
                cb.resume(null)
            }
        }
    }

    fun getByUuid(
        uuid: String,
        callback: RequestSingleOrNullCallback<RoomRecord>.() -> Unit
    )

    suspend fun getByUuids(uuid: List<String>): List<RoomRecord> = suspendCoroutine { cb ->
        getByUuids(uuid) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getByUuids(
        uuid: List<String>,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun getAllLiveChildren(
        uuid: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        getAllLiveChildren(uuid, order, asc, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getAllLiveChildren(
        uuid: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun getAllLiveMessage(
        uuid: String,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        getAllLiveMessage(uuid, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getAllLiveMessage(
        uuid: String,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun getAllRecords(
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        getAllRecords(order, asc, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getAllRecords(
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun getAllTimeRecords(
        fromTs: Long, toTs: Long,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        getAllTimeRecords(fromTs, toTs, order, asc, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getAllTimeRecords(
        fromTs: Long, toTs: Long,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun getAllByTypeAndSubtype(
        type: Int, subType: Int,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        getAllByTypeAndSubtype(type, subType, order, asc, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun getAllByTypeAndSubtype(
        type: Int, subType: Int,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )

    suspend fun searchAll(
        query: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
    ): List<RoomRecord> = suspendCoroutine { cb ->
        searchAll(query, order, asc, offset, pageSize) {
            onSuccess = {
                cb.resume(it)
            }
            onEmpty = {
                cb.resume(listOf())
            }
            onError = {
                cb.resume(listOf())
            }
        }
    }

    fun searchAll(
        query: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    )
}

open class RequestListCallback<T> {
    var onError: (DataError) -> Unit = {}
    var onEmpty: () -> Unit = {}
    var onSuccess: (MutableList<T>) -> Unit = {}
}

open class RequestSingleOrNullCallback<T> {
    var onError: (DataError) -> Unit = {}
    var onEmpty: () -> Unit = {}
    var onSuccess: (T) -> Unit = {}
}

const val ONLINE_SCHEMA = "online://block/"
