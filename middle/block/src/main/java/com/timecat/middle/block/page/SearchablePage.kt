package com.timecat.middle.block.page

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.same.lib.base.AndroidUtilities
import com.same.lib.core.ActionBar
import com.same.lib.core.ActionBarMenu
import com.same.lib.core.ActionBarMenuItem.ActionBarMenuItemSearchListener
import com.same.lib.drawable.MenuDrawable
import com.same.lib.helper.LayoutHelper
import com.same.lib.same.view.EmptyTextProgressView
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.R
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
abstract class SearchablePage : ViewModelPage() {

    lateinit var emptyView: EmptyTextProgressView
    lateinit var recycleView: RecyclerView
    lateinit var centerView: View

    val searchAdapter: BaseAdapter = BaseAdapter(null)
    private val searchNotMoreItem: NotMoreItem = NotMoreItem()
    var searchingInPrepare = false
    var searchingInProgress = false
    var searchOffset = 0
    val pageSize: Int = 10
    val searchAction = LoadAction { context ->
        searchOffset = 0
        lifecycleScope.launch(Dispatchers.IO) {
            val searchResult = getSearchResults(context)
            withContext(Dispatchers.Main) {
                if (searchResult.isEmpty()) {
                    emptyView.beVisible()
                    recycleView.beGone()
                } else {
                    emptyView.beGone()
                    recycleView.beVisible()
                }
                searchAdapter.reload(searchResult)
            }
        }
    }
    val searchMoreAction = LoadAction { context ->
        searchOffset += searchAdapter.endlessPageSize
        lifecycleScope.launch(Dispatchers.IO) {
            val searchResult = getMoreSearchResults(context)
            searchingInProgress = false
            withContext(Dispatchers.Main) {
                searchAdapter.onLoadMoreComplete(searchResult)
            }
        }
    }

    //region action
    override fun createActionBar(context: Context): ActionBar {
        val actionBar = buildActionBar(context)

        val menuDrawable = MenuDrawable()
        menuDrawable.setRotateToBack(true)
        actionBar.setBackButtonDrawable(menuDrawable)

        val iconColor = Attr.getIconColor(context)
        val menu = actionBar.createMenu()
        menu.addItem(MENU_SEARCH, R.drawable.ic_search).apply {
            setIconColor(iconColor)
            setIsSearchField(true)
            setActionBarMenuItemSearchListener(object : ActionBarMenuItemSearchListener() {
                override fun onSearchExpand() {
                    searchingInPrepare = true
                    centerView.beGone()
                }

                override fun onSearchCollapse() {
                    emptyView.beGone()
                    recycleView.beGone()
                    centerView.beVisible()
                    searchingInPrepare = false
                    searchingInProgress = false
                    searchAction.cancel()
                    searchMoreAction.cancel()
                }

                override fun onTextChanged(editText: EditText) {
                    val text = editText.text.toString()
                    if (text.isEmpty()) {
                        searchingInProgress = false
                    } else {
                        searchingInProgress = true

                        searchAction.query = text
                        searchMoreAction.query = text

                        searchAction.load(context)
                        searchMoreAction.cancel()
                    }
                }
            })
        }
        configMenu(context, menu)
        return actionBar
    }

    open fun configMenu(context: Context, menu: ActionBarMenu) {}

    abstract suspend fun getSearchResults(context: Context): List<BaseItem<*>>
    open suspend fun getMoreSearchResults(context: Context): List<BaseItem<*>> = listOf()
    //endregion

    override fun createView(context: Context): View {
        val frameLayout = createSearchableContainer(context)
        fragmentView = frameLayout
        centerView = createCenterView(context)
        frameLayout.addView(
            centerView,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT.toFloat())
        )
        return fragmentView
    }

    protected fun createSearchableContainer(context: Context): FrameLayout {
        val frameLayout = FrameLayout(context)

        emptyView = EmptyTextProgressView(context)
        emptyView.setText(context.getString(R.string.NoResult))
        emptyView.showTextView()
        emptyView.setShowAtCenter(true)
        frameLayout.addView(
            emptyView,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT.toFloat())
        )

        recycleView = RecyclerView(context)
        frameLayout.addView(
            recycleView,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT.toFloat())
        )
        bindAdapter(searchAdapter, recycleView)
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val activity = parentActivity ?: return
                    AndroidUtilities.hideKeyboard(activity.currentFocus)
                }
            }
        })
        configAdapterEndlessLoad(searchAdapter, false, 10, 4, searchNotMoreItem) { lastPosition: Int, currentPage: Int ->
            searchMoreAction.load(context)
        }
        return frameLayout
    }

    abstract fun createCenterView(context: Context): View

    companion object {
        const val MENU_SEARCH = 1000
    }
}