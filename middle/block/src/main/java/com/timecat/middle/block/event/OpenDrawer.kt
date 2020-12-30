package com.timecat.middle.block.event

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-26
 * @description null
 * @usage null
 */
class OpenDrawer

data class NextPage(val hasPrevPage: (Boolean) -> Unit, val hasNextPage: (Boolean) -> Unit)
data class PrevPage(val hasPrevPage: (Boolean) -> Unit, val hasNextPage: (Boolean) -> Unit)