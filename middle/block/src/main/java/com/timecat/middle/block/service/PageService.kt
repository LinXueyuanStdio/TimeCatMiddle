package com.timecat.middle.block.service

import android.content.Context
import com.same.lib.core.BasePage
import com.timecat.identity.readonly.RouterHub

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/11
 * @description 打开页面
 * @usage null
 */
interface CommonPageService {
    fun openPage(context: Context, pageId: String): BasePage
}

interface BrowserPageService {
    fun openPage(context: Context, url: String, isIncognito: Boolean): BasePage
}

const val GLOBAL_BrowserPageServiceImpl: String = RouterHub.GLOBAL + "browser" + RouterHub.SERVICE + "/BrowserPageService"

const val GLOBAL_CommonPageServiceImpl: String = RouterHub.GLOBAL + "browser" + RouterHub.SERVICE + "/CommonPageService"

