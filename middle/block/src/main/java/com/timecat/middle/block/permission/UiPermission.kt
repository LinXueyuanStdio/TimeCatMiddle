package com.timecat.middle.block.permission


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 本地计算
 * @usage null
 */

/**
 * 元权限
 *
 * 本质是一个 uri
 */
typealias MetaPermission = String

/**
 * 混权限
 *
 * 本质是一个 uri 的正则表达式
 */
class HunPermission(val checker: Regex, val uri: String, val why: Why) {
    override fun toString(): String {
        return "HunPermission(checker=$checker, uri='$uri', why=$why)\n"
    }
}

data class Why(var reason: String) {
    override fun toString(): String {
        return reason
    }
}

class PermissionCallback {
    /**
     * 允许获得资源
     */
    var onAllowed: ((why: Why) -> Unit)? = null

    /**
     * 拒绝获得资源
     */
    var onBanned: ((why: Why) -> Unit)? = null
}
