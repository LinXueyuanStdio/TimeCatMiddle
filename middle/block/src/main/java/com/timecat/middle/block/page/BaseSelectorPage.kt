package com.timecat.middle.block.page

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.identity.service.ImageSelectService
import com.timecat.identity.service.ImageUploadService
import com.timecat.middle.block.ext.selectImg

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description 创建对话笔记
 * @usage null
 */
abstract class BaseSelectorPage : BaseNewPage() {

    val uploader: ImageUploadService? by lazy { NAV.service(ImageUploadService::class.java) }
    val selector: ImageSelectService? by lazy { NAV.service(ImageSelectService::class.java) }

    fun selectAvatar(context: Context, setImg: (String) -> Unit) {
        selectImg(context, true, setImg)
    }

    fun selectCover(context: Context, setImg: (String) -> Unit) {
        selectImg(context, false, setImg)
    }

    fun selectImg(context: Context, isAvatar: Boolean, setImg: (String) -> Unit) {
        selectImg(context, uploader, selector, isAvatar, setImg)
    }
}