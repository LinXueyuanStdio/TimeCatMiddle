package com.timecat.middle.block.service

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/13
 * @description
 * 用户权限在卡片上的映射
 * 每种权限需要构建一类卡片
 * @usage null
 */
enum class CardPermission(val title: String) {
    ReadOnly("仅可读"),
    Interactive("可交互"),
    Editable("可编辑"),
    FullAccess("完全权限"),
}
