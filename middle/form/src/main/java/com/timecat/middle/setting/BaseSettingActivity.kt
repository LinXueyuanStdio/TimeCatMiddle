package com.timecat.middle.setting

import android.view.ViewGroup
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.*
import com.timecat.layout.ui.business.setting.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description 设置
 * @usage 继承本类，然后实现addSettingItems方法。按顺序写设置项即可。
 */
abstract class BaseSettingActivity : BaseToolbarActivity() {
    protected lateinit var container: ViewGroup
    override fun layout(): Int = R.layout.middle_activity_setting
    override fun initView() {
        container = findViewById(R.id.container)
        addSettingItems(container)
    }

    protected abstract fun addSettingItems(container: ViewGroup)

    protected fun letMeKnow(letMeKnow: String? = null) {
        letMeKnow?.let { LetMeKnow.report(it) }
    }

    //region 头部说明
    protected fun simpleHead(container: ViewGroup, title: String) {
        simpleDivider(container)
        container.addView(HeadItem(this).apply {
            setText(title)
        })
    }

    //endregion

    //region 下一个
    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        path: String
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            hint = null
            text = null
            onNext {
                NAV.go(this@BaseSettingActivity, path)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        hint: String,
        path: String
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            this.hint = hint
            text = null
            onNext {
                NAV.go(this@BaseSettingActivity, path)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        hint: String?,
        letMeKnow: String? = null,
        path: String
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            this.hint = hint
            text = null
            onNext {
                letMeKnow(letMeKnow)
                NAV.go(this@BaseSettingActivity, path)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        hint: String?,
        initialText: String? = null,
        letMeKnow: String? = null,
        go: (NextItem) -> Unit
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            this.hint = hint
            text = initialText
            onNext {
                letMeKnow(letMeKnow)
                go(this)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        letMeKnow: String? = null,
        go: (NextItem) -> Unit
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            this.hint = null
            text = null
            onNext {
                letMeKnow(letMeKnow)
                go(this)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleNext(
        container: ViewGroup,
        title: String,
        hint: String?,
        letMeKnow: String? = null,
        go: (NextItem) -> Unit
    ): NextItem {
        val s = NextItem(this).apply {
            this.title = title
            this.hint = hint
            text = null
            onNext {
                letMeKnow(letMeKnow)
                go(this)
            }
        }
        container.addView(s)
        return s
    }
    //endregion

    //region 滑块
    protected fun simpleUnitSlide(
        container: ViewGroup,
        title: String,
        unit: String,
        from: Float, to: Float, value: Float,
        SetValue: (Float) -> Unit
    ): SliderItem {
        val s = SliderItem(this).apply {
            valueFrom = from
            valueTo = to
            stepSize = 1f
            hint = null
            this.title = title
            this.value = when {
                value < from -> from
                value > to -> to
                else -> value
            }
            this.text = "${this.value} $unit"
            onSlide { value ->
                val trueValue = when {
                    value < from -> from
                    value > to -> to
                    else -> value
                }
                text = "$trueValue $unit"
                SetValue(trueValue)
            }
        }
        container.addView(s)
        return s
    }

    protected fun simpleSlide(
        container: ViewGroup,
        title: String,
        unit: String,
        from: Float, to: Float, value: Float,
        SetValue: (Float, SliderItem) -> Unit
    ): SliderItem {
        val s = SliderItem(this).apply {
            valueFrom = from
            valueTo = to
            stepSize = 1f
            hint = null
            this.title = title
            this.value = when {
                value < from -> from
                value > to -> to
                else -> value
            }
            this.text = unit
            onSlide { value ->
                SetValue(value, this)
            }
        }
        container.addView(s)
        return s
    }
    //endregion

    //region 开关
    protected fun simpleSwitch(
        container: ViewGroup,
        title: String,
        getInitialCheck: () -> Boolean,
        check: (Boolean) -> Unit
    ): SwitchItem {
        return simpleSwitch(container, title, null, getInitialCheck, check)
    }

    protected fun simpleSwitch(
        container: ViewGroup,
        title: String,
        hint: String?,
        getInitialCheck: () -> Boolean,
        check: (Boolean) -> Unit
    ): SwitchItem {
        val s = SwitchItem(this).apply {
            this.title = title
            this.hint = hint
            initCheck = getInitialCheck()
            onCheckChange = { check(isChecked) }
        }
        container.addView(s)
        return s
    }
    //endregion

    //region 容器
    protected fun simpleContainer(container: ViewGroup): ContainerItem {
        val c = ContainerItem(this)
        container.addView(c)
        c.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        c.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        return c
    }
    //endregion

    //region 分割线
    protected fun simpleDivider(container: ViewGroup) {
        val d = DividerItem(this)
        container.addView(d)
        d.setup(this)
    }
    //endregion

    //region image
    protected fun simpleImage(container: ViewGroup, title: String,
                              initialIcon: String? = "R.drawable.ic_launcher",
                              onClick:(item: ImageItem) -> Unit): ImageItem {
        val d = ImageItem(this).apply {
            this.title = title
            initialIcon?.let{
                setImage(it)
            }
            this.onClick(onClick)
        }
        container.addView(d)
        return d
    }
    //endregion
}