package com.timecat.middle.block.service

import android.content.Context

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/14
 * @description null
 * @usage null
 */
interface CreateOnlineDbService {
    fun showImportOnlineDbDialog(context: Context, onSelect: (String) -> Unit)
    fun showCreateOnlineDbDialog(context: Context, onSelect: (String) -> Unit)
}