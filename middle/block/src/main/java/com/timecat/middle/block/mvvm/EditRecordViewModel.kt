package com.timecat.middle.block.mvvm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.timecat.data.room.record.RecordDao
import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-26
 * @description null
 * @usage null
 */
class EditRecordViewModel(
    private val dataRepository: RecordDao
) : ViewModel() {

    //region loaded page
    enum class ViewState {
        LOADING,
        LOADED,
        DOES_NOT_EXIST
    }

    val viewState = MutableLiveData(ViewState.LOADING)

    fun setViewState(state: ViewState) {
        viewState.value = state
    }

    val recordId = MutableLiveData("")

    val data = Transformations.map(recordId) { p ->
        if (p.isBlank()) {
            null
        } else {
            dataRepository.getByUuid(p)
        }
    }

    data class UpdateData(val uuid:String, val title:String, val content:String, val updateTime:Long)

    /**
     * 用于刷新书的页列表
     */
    val updatedRecordData = MutableLiveData<UpdateData>(null)

    /**
     * 聊天列表
     */
    val updatedConvUuid = MutableLiveData("")
    val bookId = MutableLiveData("")
    val book = Transformations.switchMap(bookId) { id ->
        MediatorLiveData<RoomRecord?>().apply {
            if (id.isBlank()) {
                value = null
            } else {
                addSource(dataRepository.getLiveData(id)) {
                    value = it
                }
            }
        }
    }

}