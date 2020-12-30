package com.timecat.middle.block.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.timecat.middle.block.R
import com.timecat.middle.block.tags.FilterState
import com.timecat.middle.block.view.SearchFilterSortView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description null
 * @usage null
 */
class SearchFilterSortPopup(
    var parent: View,
    var state: FilterState = FilterState(),
    var listener: Listener
) {
    interface Listener {
        fun performSearch(state: FilterState)
    }

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_search_filter_sort, null, false
    )
    private val popupWindow: PopupWindow

    init {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.animationStyle = R.style.popup_window_animation
        popupWindow.showAtLocation(parent, 17, 0, 0)
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        val root: View = view.findViewById(R.id.root)
        root.setOnClickListener { popupWindow.dismiss() }
        val searchToolbar: SearchFilterSortView = view.findViewById(R.id.searchToolbar)
        searchToolbar.listener = object :SearchFilterSortView.Listener{

            override fun performSearch(state: FilterState) {
                listener.performSearch(state)
            }

            override fun searchClose() {
                popupWindow.dismiss()
            }

            override fun searchBack() {
                popupWindow.dismiss()
            }
        }
    }
}