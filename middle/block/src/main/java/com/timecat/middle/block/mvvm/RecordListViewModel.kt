package com.timecat.middle.block.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.timecat.data.room.RoomClient
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.GOAL
import com.timecat.identity.data.base.HABIT
import com.timecat.identity.data.base.REMINDER
import com.timecat.identity.data.block.type.BLOCK_LINK
import com.timecat.identity.data.block.type.BLOCK_RECORD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/2
 * @description null
 * @usage null
 */
open class RecordListViewModel(mApplication: Application) : AndroidViewModel(mApplication) {

    var allData: MutableLiveData<MutableList<RoomRecord>> = MutableLiveData(mutableListOf())

    val allRecords = Transformations.map(allData) {
        it.filter { it.type == BLOCK_RECORD }
    }
    val allLinks = Transformations.map(allData) {
        it.filter { it.type == BLOCK_LINK }
    }

    val allTimeRecords = Transformations.map(allRecords) {
        it.filter {
            it.subType == REMINDER || it.subType == HABIT || it.subType == GOAL
        }.filter {
            it.canFinish()
        }
    }

    protected fun databaseReload(context: CoroutineContext, parent: String = "") {
        GlobalScope.launch(context) {
            val items = RoomClient.recordDao().getAllChildren(parent)
            allData.postValue(items)
        }
    }

    init {
        //初始化 allRecordItems
        databaseReload(Dispatchers.IO, "")
    }

}