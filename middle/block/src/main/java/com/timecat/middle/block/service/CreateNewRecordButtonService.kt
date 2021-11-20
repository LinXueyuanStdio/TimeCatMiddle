package com.timecat.middle.block.service

import android.content.Context
import com.google.android.material.chip.Chip
import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/2
 * @description null
 * @usage null
 */
interface CreateNewRecordButtonService {
    fun createNewRecordButton(
        context: Context,
        iconColor: Int,
        backgroundColor: Int,
        folder: RoomRecord? = null,
        listener: ItemCommonListener
    ): Chip
}