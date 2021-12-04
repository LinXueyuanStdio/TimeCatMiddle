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
    NoAccess("无权限"),
    ReadOnly("仅可读"),
    Interactive("可交互"),
    Editable("可编辑"),
    FullAccess("完全权限");

    fun noAccess(): Boolean = !canRead()
    fun canRead(): Boolean = this != NoAccess
    fun canInteract(): Boolean = this == Interactive || this == Editable || this == FullAccess
    fun canSave(): Boolean = this == Editable || this == FullAccess
    fun canDelete(): Boolean = this == Editable || this == FullAccess
}
