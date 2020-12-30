package com.timecat.middle.setting

import android.content.Context
import android.text.InputType
import android.view.ViewGroup
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.*
import com.timecat.layout.ui.utils.ViewUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/25
 * @description Material 风格的表单
 * @usage null
 */
class MaterialForm(val windowContext: Context, val container: ViewGroup) {

    fun navTo(c: Context, path: String) {
        NAV.go(c, path)
    }

    fun letMeKnow(letMeKnow: String? = null) {
        letMeKnow?.let { LetMeKnow.report(it) }
    }

    //region 下一个
    fun Next(
        title: String,
        path: String
    ): NextItem = NextItem(windowContext).apply {
        this.title = title
        hint = null
        onNext {
            navTo(windowContext, path)
        }
    }.also {
        container.addView(it)
    }

    fun Next(
        title: String,
        onNext: (NextItem) -> Unit
    ): NextItem = NextItem(windowContext).apply {
        this.title = title
        hint = null
        onNext {
            onNext(this)
        }
    }.also {
        container.addView(it)
    }

    fun Next(
        title: String,
        text: String,
        onNext: (NextItem) -> Unit
    ): NextItem = NextItem(windowContext).apply {
        this.title = title
        this.text = text
        hint = null
        onNext {
            onNext(this)
        }
    }.also {
        container.addView(it)
    }

    fun Next(
        title: String,
        hint: String?,
        initialText: String? = null,
        letMeKnow: String? = null,
        go: (NextItem) -> Unit
    ): NextItem = NextItem(windowContext).apply {
        this.title = title
        this.hint = hint
        text = initialText
        onNext {
            letMeKnow(letMeKnow)
            go(this)
        }
    }.also {
        container.addView(it)
    }
    //endregion

    //region 滑块
    fun UnitSlide(
        title: String,
        unit: String,
        from: Float, to: Float, value: Float,
        SetValue: (Float) -> Unit
    ): SliderItem = SliderItem(windowContext).apply {
        valueFrom = from
        valueTo = to
        stepSize = 1f
        hint = null
        this.title = title
        this.value = value
        this.text = "${this.value} $unit"
        onSlide { value ->
            text = "$value $unit"
            SetValue(value)
        }
    }.also {
        container.addView(it)
    }

    fun Slide(
        title: String,
        unit: String,
        from: Float, to: Float, value: Float,
        SetValue: (Float) -> Unit
    ): SliderItem = SliderItem(windowContext).apply {
        valueFrom = from
        valueTo = to
        stepSize = 1f
        hint = null
        this.title = title
        this.value = value
        this.text = unit
        onSlide { value ->
            SetValue(value)
        }
    }.also {
        container.addView(it)
    }
    //endregion

    //region 开关
    fun Switch(
        title: String,
        getInitialCheck: () -> Boolean,
        check: (Boolean) -> Unit
    ): SwitchItem = Switch(title, null, getInitialCheck, check)

    fun Switch(
        title: String,
        hint: String?,
        getInitialCheck: () -> Boolean,
        check: (Boolean) -> Unit
    ): SwitchItem = SwitchItem(windowContext).apply {
        this.title = title
        this.hint = hint
        initCheck = getInitialCheck()
        onCheckChange = { check(isChecked) }
    }.also {
        container.addView(it)
    }
    //endregion

    //region 下拉选择
    fun <T> addSpinner(title: String, items: List<T>, onSelect: (data: T, index: Int) -> Unit): SpinnerItem {
        val d = SpinnerItem(windowContext).apply {
            this.title = title
            onItemSelected(items, onSelect)
        }
        container.addView(d)
        return d
    }
    //endregion

    //region 图片
    fun Image(title: String, icon: String, onSelectImage:(item: ImageItem) -> Unit) = ImageItem(windowContext).apply {
        this.title = title
        this.setImage(icon)
        onClick(onSelectImage)
    }.also {
        container.addView(it)
    }

    fun Image(title: String, onSelectImage:(item: ImageItem) -> Unit) = ImageItem(windowContext).apply {
        this.title = title
        this.setImage("R.drawable.ic_launcher")
        onClick(onSelectImage)
    }.also {
        container.addView(it)
    }
    //endregion

    //region DSL
    fun H1(title: String)=Head(title, ViewUtil.dp2px(20f))
    fun H2(title: String)=Head(title, ViewUtil.dp2px(18f))
    fun H3(title: String)=Head(title, ViewUtil.dp2px(16f))
    fun H4(title: String)=Head(title, ViewUtil.dp2px(14f))
    fun H5(title: String)=Head(title, ViewUtil.dp2px(13f))
    fun H6(title: String)=Head(title, ViewUtil.dp2px(12f))

    fun Head(title: String, textSize: Int): HeadItem = HeadItem(windowContext).apply {
        setText(title)
    }.also {
        container.addView(it)
    }

    fun Body(context: String): HeadItem = HeadItem(windowContext).apply {
        setText(context)
    }.also {
        container.addView(it)
    }

    fun Divider(): DividerItem = DividerItem(windowContext).also {
        container.addView(it)
        it.setup(windowContext)
    }

    fun OneLineInput(build: InputItem.() -> Unit) = InputItem(windowContext).apply(build).also {
        container.addView(it)
    }

    fun OneLineInput(
        title: String,
        prefill: String,
        onTextChange: (value: String?) -> Unit
    ) = InputItem(windowContext).apply {
        this.hint = title
        this.text = prefill
        this.onTextChange = onTextChange
    }.also {
        container.addView(it)
    }

    fun MultiLineInput(build: InputItem.() -> Unit) = InputItem(windowContext).apply(build).also {
        it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        container.addView(it)
    }

    fun MultiLineInput(
        title: String,
        prefill: String,
        onTextChange: (value: String?) -> Unit
    ) = InputItem(windowContext).apply {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        this.hint = title
        this.text = prefill
        this.onTextChange = onTextChange
    }.also {
        container.addView(it)
    }

    fun Spinner(build: SpinnerItem.() -> Unit) = SpinnerItem(windowContext).apply(build).also {
        container.addView(it)
    }

    fun Switch(build: SwitchItem.() -> Unit) = SwitchItem(windowContext).apply(build).also {
        container.addView(it)
    }

    fun Slider(build: SliderItem.() -> Unit) = SliderItem(windowContext).apply(build).also {
        container.addView(it)
    }

    fun Next(build: NextItem.() -> Unit) = NextItem(windowContext).apply(build).also {
        container.addView(it)
    }

    fun Image(build: ImageItem.() -> Unit) = ImageItem(windowContext).apply(build).also {
        container.addView(it)
    }
    //endregion
}
