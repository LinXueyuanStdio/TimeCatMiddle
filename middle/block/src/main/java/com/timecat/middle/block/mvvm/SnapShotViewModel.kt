package com.timecat.middle.block.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.timecat.data.room.RoomClient

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/31
 * @description null
 * @usage null
 */
class SnapShotViewModel(mApplication: Application) : AndroidViewModel(mApplication) {
    enum class ViewState {
        LOADING,
        LOADED,
        EMPTY,
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
            RoomClient.recordDao().getByUuid(p)
        }
    }
}