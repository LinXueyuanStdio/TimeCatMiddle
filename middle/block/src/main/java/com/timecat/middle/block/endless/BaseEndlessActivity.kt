package com.timecat.middle.block.endless

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.timecat.element.alert.ToastUtil
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.identity.data.service.DataError
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.Payload

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
abstract class BaseEndlessActivity : BaseRefreshListActivity() {
    private val notMoreItem: NotMoreItem = NotMoreItem()
    lateinit var adapter: BaseAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        adapter = BaseAdapter(null)
        adapter.addScrollableFooter(NotMoreItem())
        return adapter
    }

    /**
     * 在校验付费后再调用
     */
    fun initViewAfterCheck() {
        adapter.setDisplayHeadersAtStartUp(false) //Show Headers at startUp!
            .setStickyHeaders(false) //Make headers sticky
            .setLoadingMoreAtStartUp(false)
            .setEndlessPageSize(pageSize)  //if newItems < 7
            .setEndlessScrollThreshold(4) //Default=1
            .setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
                override fun noMoreLoad(newItemsSize: Int) {
                }

                override fun onLoadMore(lastPosition: Int, currentPage: Int) {
                    loadMore()
                }
            }, notMoreItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.onRestoreInstanceState(savedInstanceState)
    }

    override fun loadData() {
        load()
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = false
    }

    var errorCallback: (DataError) -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showError("查询失败") {
            onRefresh()
        }
        ToastUtil.e("查询失败")
        it.printStackTrace()
    }
    var emptyCallback: () -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showContent()
        adapter.updateItem(notMoreItem, Payload.NO_MORE_LOAD)
    }

    var pageSize = 10
    var offset = 0

    fun load() {
        offset = 0
        loadFirst()
    }

    abstract fun loadFirst()

    abstract fun loadMore()
}