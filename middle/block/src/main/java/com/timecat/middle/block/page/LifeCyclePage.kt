package com.timecat.middle.block.page

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/9
 * @description null
 * @usage null
 */
abstract class LifeCyclePage : SamePage(), LifecycleOwner {

    private val mDispatcher = PageLifecycleDispatcher(this)

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }

    override fun onFragmentCreate(): Boolean {
        mDispatcher.onPagePreSuperOnCreate()
        mDispatcher.onPagePreSuperOnStart()
        return super.onFragmentCreate()
    }

    override fun onPause() {
        mDispatcher.onPagePreSuperOnPause()
        super.onPause()
    }

    override fun onResume() {
        mDispatcher.onPagePreSuperOnResume()
        super.onResume()
    }

    override fun onFragmentDestroy() {
        mDispatcher.onPagePreSuperOnDestroy()
        super.onFragmentDestroy()
    }
}