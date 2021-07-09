package com.timecat.middle.block.ext

import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.service.ItemCommonListener
import eu.davidea.flexibleadapter.FlexibleAdapter

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/9
 * @description null
 * @usage null
 */
fun configAdapterEndlessLoad(
    adapter: BaseAdapter,
    isTopEndless: Boolean,
    pageSize: Int,
    endlessScrollThreshold: Int,
    notMoreItem: BaseItem<*>,
    listener: ItemCommonListener
) {
    adapter.isTopEndless = isTopEndless
    adapter.endlessPageSize = pageSize
    adapter.setEndlessScrollThreshold(endlessScrollThreshold)
    adapter.setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
        override fun noMoreLoad(newItemsSize: Int) {
        }

        override fun onLoadMore(lastPosition: Int, currentPage: Int) {
            listener.loadMore(lastPosition, currentPage)
        }
    }, notMoreItem)
}

fun configAdapterNormal(adapter: BaseAdapter) {
    adapter.setEndlessProgressItem(null)
}
