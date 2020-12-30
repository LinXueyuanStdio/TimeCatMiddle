package com.timecat.middle.block.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.timecat.data.room.tag.Tag
import com.timecat.identity.data.randomColor
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.ConfigAdapter
import com.timecat.middle.block.adapter.ConfigItem
import com.timecat.middle.block.tags.FilterState
import com.timecat.middle.block.tags.FilterView
import com.timecat.middle.block.util.KeyboardUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/27
 * @description null
 * @usage null
 */
class SearchFilterSortView : CardView {
    var state: FilterState = FilterState()
    var listener: Listener? = null
    private lateinit var filterView: FilterView
    private lateinit var tagsFlexBox: FlexboxLayout
    private lateinit var searchBox: EditText
    private lateinit var rv: RecyclerView
    private lateinit var adapter: ConfigAdapter

    interface Listener {
        fun performSearch(state: FilterState)
        fun searchClose()
        fun searchBack()
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, layout: Int) : super(context) {
        init(context, layout)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context, layout: Int = R.layout.view_search_filter_sort) {
        LayoutInflater.from(context).inflate(layout, this)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = params

        val searchBackButton: View = findViewById(R.id.searchBackButton)
        val searchCloseIcon: View = findViewById(R.id.searchCloseIcon)
        tagsFlexBox = findViewById(R.id.tagsFlexBox)
        searchBox = findViewById(R.id.searchBox)
        rv = findViewById(R.id.rv)

        searchBackButton.setOnClickListener { listener?.searchBack() }
        searchCloseIcon.setOnClickListener {
            if (searchBox.text.isNotEmpty()) {
                searchBox.setText("")
                state.text = ""
            } else {
                listener?.searchClose()
            }
        }

        rv.layoutManager = GridLayoutManager(context, 4)

        searchBox.clearFocus()
        val data = getItems()
        adapter = ConfigAdapter(data)
        adapter.setGridSpanSizeLookup { _, _, position ->
            data[position].spanSize
        }
        rv.adapter = adapter
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                startSearch(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        filterView = FilterView(
            context,
            tagsFlexBox,
            state,
            { tag ->
                if (state.tags.any { it.uuid == tag.uuid }) {
                    state.tags.removeAll { it.uuid == tag.uuid }
                    startSearch(searchBox.text.toString())
                    filterView.notifyChanged()
                } else {
                    openTag(tag)
                    filterView.notifyChanged()
                }
            },
            { color ->
                if (state.colors.contains(color)) {
                    state.colors.remove(color)
                } else {
                    state.colors.add(color)
                }
                filterView.notifyChanged()
                startSearch(searchBox.text.toString())
            },
            { type ->
                if (state.domain.type.contains(type)) {
                    state.domain.type.remove(type)
                } else {
                    state.domain.type.add(type)
                }
                filterView.notifyChanged()
                startSearch(searchBox.text.toString())
            }
        )
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            enterSearchMode()
        } else {
            searchBox.clearFocus()
            KeyboardUtil.hideKeyboard(searchBox)
        }
    }

    private fun enterSearchMode() {
        searchBox.setText(state.text)
        KeyboardUtil.showKeyboard(searchBox)
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { filterView.reset() }
            filterView.notifyChanged()
        }
        searchBox.requestFocus()
    }

    private fun getItems(): MutableList<ConfigItem> {
        val a: MutableList<ConfigItem> = mutableListOf()
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isPined() },
                { "置顶" },
                { "置顶" },
                { state.domain.setPined(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isArchived() },
                { "归档" },
                { "归档" },
                { state.domain.setArchived(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isDeleted() },
                { "回收站" },
                { "回收站" },
                { state.domain.setDeleted(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isTemp() },
                { "暂存区" },
                { "暂存区" },
                { state.domain.setTemp(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isPrivate() },
                { "私有" },
                { "私有" },
                { state.domain.setPrivate(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isReadOnly() },
                { "只读" },
                { "只读" },
                { state.domain.setReadOnly(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isLocked() },
                { "封印" },
                { "封印" },
                { state.domain.setLocked(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isBlock() },
                { "黑名单" },
                { "黑名单" },
                { state.domain.setBlock(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.canFinish() },
                { "可完成" },
                { "可完成" },
                { state.domain.setCanFinish(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { !state.domain.all },
                { randomColor() },
                { state.domain.isFinished() },
                { "完成" },
                { "完成" },
                { state.domain.setFinished(it) }
            ))
        a.add(
            ConfigItem.configSwitcher(
                { true },
                { randomColor() },
                { state.domain.all },
                { "全部" },
                { "全部" },
                {
                    state.domain.all = it
                    rv.post {
                        adapter.notifyDataSetChanged()
                        filterView.notifyChanged()
                    }
                }
            ))
        return a
    }

    private fun startSearch(keyword: String) {
        state.text = keyword
        listener?.performSearch(state)
    }

    private fun openTag(tag: Tag) {
        state.tags.add(tag)
        listener?.performSearch(state)
    }

}