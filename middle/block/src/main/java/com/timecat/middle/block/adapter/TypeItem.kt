package com.timecat.module.master.adapter.item.virtual

import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/24
 * @description null
 * @usage null
 */
class TypeItem(
    val type: Int,
    val title: String,
    val desc: String,
    var expand: Boolean = true
)

open class SubItem(
    val type: Int,
    val subType: Int,
    val title: String,
    val desc: String,
    val icon: String,
    val typeName: String,
    val helpUrl: String,
    var uuid: String = "",
    var isDir: Boolean = false,
)