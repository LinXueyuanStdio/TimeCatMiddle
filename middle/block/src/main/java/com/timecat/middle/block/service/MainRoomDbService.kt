package com.timecat.middle.block.service

import android.content.Context

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/20
 * @description null
 * @usage null
 */
interface MainRoomDbService {
    fun build(context: Context): IDatabase
}