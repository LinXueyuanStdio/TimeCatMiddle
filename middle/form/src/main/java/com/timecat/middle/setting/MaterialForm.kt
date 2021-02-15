package com.timecat.middle.setting

import android.view.ViewGroup
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.setting.NextItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/25
 * @description Material 风格的表单
 * @usage null
 */
fun ViewGroup.Next(
    title: String,
    path: String
): NextItem = Next(title) {
    NAV.go(context, path)
}