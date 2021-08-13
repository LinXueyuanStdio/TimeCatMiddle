package com.timecat.middle.block.ext

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LifecycleOwner
import com.afollestad.vvalidator.DestroyLifecycleObserver
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormBuilder
import com.same.lib.drawable.DrawableManager

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/22
 * @description null
 * @usage null
 */
fun Context.form(form: View, builder: FormBuilder): Form {
    val activity = this
    val container = object : ValidationContainer(activity) {
        override fun <T : View> findViewById(id: Int): T? = form.findViewById(id)
    }
    return Form(container)
        .apply(builder)
        .also {
            if (this is LifecycleOwner) {
                lifecycle.addObserver(DestroyLifecycleObserver(it))
            }
        }
        .start()
}
