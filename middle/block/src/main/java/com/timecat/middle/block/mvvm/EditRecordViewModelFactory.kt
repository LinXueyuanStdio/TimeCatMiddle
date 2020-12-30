package com.timecat.middle.block.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timecat.data.room.record.RecordDao

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-26
 * @description null
 * @usage null
 */
class EditRecordViewModelFactory(
    private val dataRepository: RecordDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EditRecordViewModel(dataRepository) as T
    }

    companion object {
        fun forRecord(dataRepository: RecordDao): ViewModelProvider.Factory {
            return EditRecordViewModelFactory(dataRepository)
        }
    }
}