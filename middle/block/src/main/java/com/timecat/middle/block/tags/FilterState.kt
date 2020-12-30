package com.timecat.middle.block.tags

import com.blankj.utilcode.util.LogUtils
import com.timecat.data.room.RoomClient
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.tag.Tag
import com.timecat.middle.block.ext.isNoteLockedButAppUnlocked
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description 用于搜索的状态模型
 * @usage null
 */
class FilterState(
    var text: String = "",
    var parent: String? = null,
    var domain: DomainState = DomainState(),
    var colors: MutableList<Int> = mutableListOf(),
    var tags: MutableList<Tag> = mutableListOf()
) {
    override fun toString(): String {
        return "$text ($parent) $domain [$colors] [$tags]"
    }

    fun hasFilter(): Boolean {
        return parent != null
                || tags.isNotEmpty()
                || domain.status == 0L
                || colors.isNotEmpty()
                || text.isNotBlank()
    }

    fun clear(): FilterState {
        text = ""
        domain.status = 0L
        colors.clear()
        tags.clear()
        parent = null
        return this
    }

    fun clearSearchBar(): FilterState {
        text = ""
        domain.status = 0L
        colors.clear()
        tags.clear()
        return this
    }

    fun copy(): FilterState {
        return FilterState(text, parent, domain, colors, tags)
    }
}

fun getSortingState(): SortingState {
    return SortingState.LAST_MODIFIED
}

suspend fun asyncSearch(state: FilterState): List<RoomRecord> {
    val allItems = mutableListOf<RoomRecord>()
    if (state.parent != null) {
        val all = GlobalScope.async(Dispatchers.IO) {
            unifiedSearchSynchronous(state)
        }
        allItems.addAll(all.await())
        LogUtils.e(allItems)
        return allItems
    }

    val allNotes = unifiedSearchWithoutFolder(state)
    allItems.addAll(RoomClient.db().recordDao().getAllContainer())
    allItems.addAll(filterOutFolders(allNotes))

    return allItems
}


fun unifiedSearchSynchronous(state: FilterState): List<RoomRecord> {
    val sorting = getSortingState()
    val notes = unifiedSearchWithoutFolder(state)
        .filter {
            val currentFolder = state.parent
            if (currentFolder == null)
                it.parent.isBlank()
            else
                currentFolder == it.parent
        }
    return sort(notes, sorting)
}

fun filterFolder(notes: List<RoomRecord>, folder: RoomRecord): List<RoomRecord> {
    val sorting = getSortingState()
    val filteredNotes = notes.filter { it.parent == folder.uuid }
    return sort(filteredNotes, sorting)
}

fun filterOutFolders(notes: List<RoomRecord>): List<RoomRecord> {
    val allFoldersUUIDs = RoomClient.recordDao().getAllContainer().map { it.uuid }
    val sorting = getSortingState()
    val filteredNotes = notes.filter { !allFoldersUUIDs.contains(it.parent) }
    return sort(filteredNotes, sorting)
}

fun unifiedSearchWithoutFolder(state: FilterState): List<RoomRecord> {
    return (getForDomain(state) ?: arrayListOf())
        .filter {
            state.colors.isEmpty() || state.colors.contains(it.color)
        }
        .filter { note ->
            state.tags.isEmpty() || state.tags.any { note.tags.contains(it.uuid) }
        }
        .filter {
            when {
                state.text.isBlank() -> true
                it.isLocked() && !it.isNoteLockedButAppUnlocked() -> false
                else -> it.getCanSearchText().contains(state.text, true)
            }
        }
}

fun getForDomain(state: FilterState): List<RoomRecord>? {
    return RoomClient.recordDao().getByDomain(state.domain.type, state.domain.status)
}