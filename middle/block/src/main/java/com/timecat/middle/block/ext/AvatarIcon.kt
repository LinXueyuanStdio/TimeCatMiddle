package com.timecat.middle.block.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.layout.ui.utils.ViewUtil
import com.timecat.middle.block.R
import eu.davidea.flipview.FlipView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/2
 * @description null
 * @usage null
 */
fun FlipView.bindIcon(icon: String, color: Int) {
    initialLayoutAnimationDuration = 0
    rearImageAnimationDuration = 80
    mainAnimationDuration = 80

    bindFrontIcon(icon)
    val padding = ViewUtil.dp2px(context, 3f)
    bindFrontIconBackground(color, padding)
    bindRearIcon(color, padding)
}

fun FlipView.bindSelected(isSelected: Boolean, icon: String, color: Int) {
    initialLayoutAnimationDuration = 0
    rearImageAnimationDuration = 80
    mainAnimationDuration = 80

    bindFrontIcon(icon)
    val padding = ViewUtil.dp2px(context, 3f)
    bindFrontIconBackground(color, padding)
    bindRearIcon(color, padding)

    flipSilently(isSelected)
}

fun FlipView.bindSelected(isSelected: Boolean, icon: Drawable, color: Int) {
    initialLayoutAnimationDuration = 0
    rearImageAnimationDuration = 80
    mainAnimationDuration = 80

    bindFrontIcon(icon)
    val padding = ViewUtil.dp2px(context, 3f)
    bindFrontIconBackground(color, padding)
    bindRearIcon(color, padding)

    flipSilently(isSelected)
}

fun FlipView.bindRearIcon(color: Int, padding: Int) {
    rearImageView.setImageResource(R.drawable.ic_check_24dp)
    rearImageView.setPadding(padding, padding, padding, padding)
    rearImageView.imageTintList = ColorStateList.valueOf(color)
    rearImageView.updateLayoutParams<FrameLayout.LayoutParams> {
        width = FrameLayout.LayoutParams.MATCH_PARENT
        height = FrameLayout.LayoutParams.MATCH_PARENT
    }
}

fun FlipView.bindFrontIconBackground(color: Int, padding: Int) {
    val drawable = Attr.tintDrawable(context, R.drawable.shape_rectangle_with_radius, color)
    frontImageView.background = drawable
    frontImageView.setPadding(padding, padding, padding, padding)
    frontImageView.updateLayoutParams<FrameLayout.LayoutParams> {
        width = FrameLayout.LayoutParams.MATCH_PARENT
        height = FrameLayout.LayoutParams.MATCH_PARENT
    }
}

fun FlipView.bindFrontIcon(icon: String) {
    IconLoader.loadDefaultRoundIcon(context, frontImageView, icon)
    frontImageView.imageTintList = null
}

fun FlipView.bindFrontIcon(icon: Drawable) {
    frontImageView.setImageDrawable(icon)
    frontImageView.imageTintList = null
}
