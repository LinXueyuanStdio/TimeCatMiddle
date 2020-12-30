package com.timecat.middle.setting

import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.timecat.layout.ui.layout.setShakelessClickListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/17
 * @description 新建
 * @usage null
 */
abstract class BaseNewActivity : BaseSettingActivity() {
    override fun layout(): Int = R.layout.middle_activity_new
    lateinit var btnOk: MaterialButton
    override fun initView() {
        super.initView()
        val textView = findViewById<TextView>(R.id.activity_title)
        textView.text = title()
        btnOk = findViewById(R.id.ok)
        btnOk.setShakelessClickListener {
            ok()
        }
    }

    abstract fun ok()
}