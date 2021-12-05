package com.timecat.middle.block.service

import android.content.Context

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/2
 * @description null
 * @usage null
 */
interface RunnerService {
    fun runButtonById(context: Context, spaceId: String, recordId: String)
}