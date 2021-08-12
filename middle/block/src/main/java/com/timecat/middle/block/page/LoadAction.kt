package com.timecat.middle.block.page

import android.content.Context
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/12
 * @description null
 * @usage null
 */
class LoadAction(var query: String = "", val process: (context: Context) -> Unit) {
    var searchTimer: Timer? = null
    fun load(context: Context) {
        try {
            searchTimer?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        searchTimer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    try {
                        searchTimer?.cancel()
                        searchTimer = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    process(context)
                }
            }, 100, 300)
        }
    }

    fun cancel() {
        searchTimer?.cancel()
    }
}
