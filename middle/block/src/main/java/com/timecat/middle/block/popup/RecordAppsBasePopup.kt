package com.timecat.middle.block.fragment

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.room.record.RoomRecord
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.ConfigAdapter
import com.timecat.middle.block.adapter.ConfigItem
import razerdp.basepopup.BasePopupWindow

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class RecordAppsBasePopup(context: Context) : BasePopupWindow(
    context, MATCH_PARENT, WRAP_CONTENT, true
) {
    interface Listener {
        fun model(): RoomRecord

        fun autoSpeak()
        fun autoPage()
        fun settingMore()
    }

    constructor(
        context: Context,
        record: RoomRecord,
        listener: Listener
    ) : this(context) {
        this.listener = listener
        this.record = record
        delayInit()
    }

    private lateinit var listener: Listener
    private lateinit var record: RoomRecord
    private lateinit var rv: RecyclerView
    private lateinit var data: MutableList<ConfigItem>
    private lateinit var multipleItemAdapter: ConfigAdapter
    override fun onCreateContentView(): View {

        val view: View = createPopupById(R.layout.view_base_popup_record_apps)

        rv = view.findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(context, 4)
        data = getItems(record)
        multipleItemAdapter = ConfigAdapter(data)
        multipleItemAdapter.setGridSpanSizeLookup { _, _, position ->
            data[position].spanSize
        }

        rv.adapter = multipleItemAdapter
        return view
    }

    private fun refreshData() {
        rv.postDelayed({
            val a = listener.model()
            multipleItemAdapter.replaceData(getItems(a))
        }, 500)
    }

    private fun getItems(record: RoomRecord): MutableList<ConfigItem> {
        val a: MutableList<ConfigItem> = mutableListOf()
        a.add(ConfigItem.configText { "通用" }) // 0
        //重要紧急 9
        a.add(ConfigItem.configIcon(
            { true },
            { record.taskLabelColor() },
            { R.drawable.ic_ring_sound },
            { "朗读" },
            { "自动朗读" },
            { iv, tv -> listener.autoSpeak() }
        ))
        a.add(ConfigItem.configIcon(
            { true },
            { record.taskLabelColor() },
            { R.drawable.ic_ring_sound },
            { "朗读" },
            { "自动朗读" },
            { iv, tv -> listener.autoSpeak() }
        ))
        a.add(ConfigItem.configIcon(
            { true },
            { record.taskLabelColor() },
            { R.drawable.ic_ring_sound },
            { "朗读" },
            { "自动朗读" },
            { iv, tv -> listener.autoSpeak() }
        ))
        a.add(ConfigItem.configIcon(
            { true },
            { record.taskLabelColor() },
            { R.drawable.ic_ring_sound },
            { "朗读" },
            { "自动朗读" },
            { iv, tv -> listener.autoSpeak() }
        ))
        return a
    }
}