package com.timecat.middle.block.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.timecat.middle.block.R
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/9
 * @description null
 * @usage null
 */
fun MaterialDialog.prepareShowInService() {
    if (windowContext is Activity) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
    }
}

class AnimBottomSheet : DialogBehavior by BottomSheet() {
    override fun getThemeRes(isDark: Boolean): Int {
        return if (isDark) {
            R.style.Theme_Design_BottomSheetDialog
        } else {
            R.style.Theme_Design_Light_BottomSheetDialog
        }
    }
}

fun Context?.showDialog(bottomSheet: Boolean = true, func: MaterialDialog.() -> Unit) {
    val context = this ?: return
    if (bottomSheet) {
        MaterialDialog(context, AnimBottomSheet()).show {
            lifecycleOwner(context as? LifecycleOwner)
            prepareShowInService()
            apply(func)
        }
    } else {
        MaterialDialog(context).show {
            lifecycleOwner(context as? LifecycleOwner)
            prepareShowInService()
            apply(func)
        }
    }
}

fun showSelectDirDialog(context: Context, text: String, onSelect: (File) -> Unit) {
    context.showDialog {
        title(text = text)
        message(text = "请选择目录")
        positiveButton(R.string.confirm)
        negativeButton(R.string.cancel)
        folderChooser(context) { _, file ->
            onSelect(file)
        }
    }
}

fun showSelectFileDialog(context: Context, text: String, onSelect: (File) -> Unit) {
    context.showDialog {
        title(text = text)
        message(text = "请选择文件")
        positiveButton(R.string.confirm)
        negativeButton(R.string.cancel)
        folderChooser(context) { _, file ->
            onSelect(file)
        }
    }
}
