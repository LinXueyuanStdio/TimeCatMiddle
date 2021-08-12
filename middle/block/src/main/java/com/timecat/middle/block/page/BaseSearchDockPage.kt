package com.timecat.middle.block.page

import android.content.Context
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.same.lib.base.AndroidUtilities
import com.same.lib.core.ActionBar
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.endless.NotMoreItem
import com.timecat.middle.block.ext.bindAdapter
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/23
 * @description null
 * @usage null
 */
abstract class BaseSearchDockPage : SearchablePage() {

    lateinit var dockRecycleView: RecyclerView

    val dockAdapter: BaseAdapter = BaseAdapter(null)
    private val dockNotMoreItem: NotMoreItem = NotMoreItem()
    var dockLoadOffset = 0

    //region action
    override fun createActionBar(context: Context): ActionBar {
        val actionBar = super.createActionBar(context)
        actionBar.setActionBarMenuOnItemClick(object : ActionBar.ActionBarMenuOnItemClick() {
            override fun onItemClick(id: Int) {
                onMenuItemClick(id)
            }
        })
        actionBar.setTitle(title())
        return actionBar
    }

    abstract fun onMenuItemClick(id: Int)
    abstract fun title(): String
    //endregion

    override fun createCenterView(context: Context): View {
        dockRecycleView = RecyclerView(context)
        bindAdapter(dockAdapter, dockRecycleView)
        dockRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val activity = parentActivity ?: return
                    AndroidUtilities.hideKeyboard(activity.currentFocus)
                }
            }
        })
        loadData(context)
        return dockRecycleView
    }

    open fun loadData(context: Context) {
        configAdapterEndlessLoad(dockAdapter, false, pageSize, 4, dockNotMoreItem) { lastPosition: Int, currentPage: Int ->
            dockLoadMoreAction.load(context)
        }
        dockLoadAction.load(context)
    }

    val dockLoadAction = LoadAction { context ->
        dockLoadOffset = 0
        lifecycleScope.launch(Dispatchers.IO) {
            val data = getDockData(context)
            withContext(Dispatchers.Main) {
                dockAdapter.reload(data)
            }
        }
    }
    val dockLoadMoreAction = LoadAction { context ->
        dockLoadOffset += dockAdapter.endlessPageSize
        lifecycleScope.launch(Dispatchers.IO) {
            val data = getDockMoreData(context)
            withContext(Dispatchers.Main) {
                dockAdapter.onLoadMoreComplete(data)
            }
        }
    }

    abstract suspend fun getDockData(context: Context): List<BaseItem<*>>
    open suspend fun getDockMoreData(context: Context): List<BaseItem<*>> = listOf()
}