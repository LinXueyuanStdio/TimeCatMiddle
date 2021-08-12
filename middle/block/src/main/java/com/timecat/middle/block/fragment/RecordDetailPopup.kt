package com.timecat.middle.block.fragment

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.identity.Attr
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR
import com.timecat.middle.block.R
import com.timecat.middle.block.adapter.ConfigAdapter
import com.timecat.middle.block.adapter.ConfigItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class RecordDetailPopup(
    var id: String,
    var parent: View,
    var listener: Listener
) {
    interface Listener {
        fun getRecordById(id: String, callback:(RoomRecord)->Unit)

        fun pin(pin: Boolean)
        fun archive(archive: Boolean)
        fun lock(lock: Boolean)
        fun setPrivate(isPrivate: Boolean)
        fun readOnly(readOnly: Boolean)
        fun block(block: Boolean)
        fun delete(delete: Boolean)
        fun temp(temp: Boolean)
        fun label(iv: ImageView, tv: TextView)
        fun color(iv: ImageView, tv: TextView)
        fun forceDelete()

        fun canFinish(canFinish: Boolean)
        fun finish(finish: Boolean)
        fun remind(remind: Boolean)
        fun tile(iv: ImageView, tv: TextView)
        fun floatingWindow(iv: ImageView, tv: TextView)
        fun notify(iv: ImageView, tv: TextView)

        fun copy(iv: ImageView, tv: TextView)
        fun nlp(iv: ImageView, tv: TextView)
        fun translate(iv: ImageView, tv: TextView)
        fun search(iv: ImageView, tv: TextView)
        fun share(iv: ImageView, tv: TextView)
        fun speakLoud(iv: ImageView, tv: TextView)
    }

    private var rv: RecyclerView
    private lateinit var record: RoomRecord
    private lateinit var data: MutableList<ConfigItem>
    private lateinit var multipleItemConfigAdapter: ConfigAdapter
    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_detail_record, null, false
    )
    private val popupWindow: PopupWindow
    private val context: Context = parent.context

    init {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        popupWindow.animationStyle = R.style.popup_window_animation
        popupWindow.showAtLocation(parent, 17, 0, 0)


        val root: View = view.findViewById(R.id.root)
        root.setOnClickListener { popupWindow.dismiss() }
        val back: View = view.findViewById(R.id.back)
        back.setOnClickListener { popupWindow.dismiss() }

        rv = view.findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(view.context, 4)

        listener.getRecordById(id) {
            record = it
            data = getItems(record)
            multipleItemConfigAdapter = ConfigAdapter(data)
            multipleItemConfigAdapter.setGridSpanSizeLookup { _, _, position ->
                data[position].spanSize
            }

            val v = View(view.context)
            val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)
            v.layoutParams = p
            multipleItemConfigAdapter.addFooterView(v)

            rv.adapter = multipleItemConfigAdapter
        }
    }

    private fun refreshData() {
        rv.postDelayed({
            listener.getRecordById(id) {
                record = it
                multipleItemConfigAdapter.replaceData(getItems(record))
            }
        }, 500)
    }

    private fun getItems(record: RoomRecord): MutableList<ConfigItem> {
        val a: MutableList<ConfigItem> = mutableListOf()
        a.add(ConfigItem.configText { "通用" }) // 0
        //置顶 1
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Color.WHITE },
            { record.isPined() },
            { if (it) "已置顶" else "未置顶" },
            { "置顶，用于管理" },
            { listener.pin(it) }
        ))
        //归档 2
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { TASK_LABEL_IMPORTANT_NOT_URGENT_COLOR },
            { record.isArchived() },
            { if (it) "已归档" else "未归档" },
            { "存档区，用于管理" },
            { listener.archive(it) }
        ))
        //回收站 3
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Color.WHITE },
            { record.isDeleted() },
            { if (it) "已回收" else "回收站" },
            { "回收站，用于管理" },
            { listener.delete(it) }
        ))
        //暂存区 4
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Color.WHITE },
            { record.isTemp() },
            { if (it) "已暂存" else "暂存区" },
            { "暂存区，用于管理" },
            { listener.temp(it) }
        ))
        //私有 5
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Color.YELLOW },
            { record.isPrivate() },
            { if (it) "已私有" else "非私有" },
            { "私有状态下，需要所有者输入密码才能查看" },
            { listener.setPrivate(it) }
        ))
        //只读 6
        a.add(ConfigItem.configSwitcher(
            { true },
            { Color.BLUE },
            { record.isReadOnly() },
            { if (it) "已只读" else "非只读" },
            { "只读状态下无法强制删除，可允许其他与修改内容无关的操作。比拉黑状态更加严格。仅所有者可以设置。" },
            {
                listener.readOnly(it)
                refreshData()
            }
        ))
        //封印 7
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Color.WHITE },
            { record.isLocked() },
            { if (it) "已封印" else "未封印" },
            { "封印状态下，仅所有者用密码才能查看" },
            { listener.lock(it) }
        ))
        //黑名单 8
        a.add(ConfigItem.configSwitcher(
            { !record.isBlock() },
            { Color.RED },
            { record.isBlock() },
            { if (it) "已拉黑" else "黑名单" },
            { "拉黑状态下只能强制删除，及其他与修改内容无关的操作。" },
            {
                listener.block(it)
                refreshData()
            }
        ))
        //重要紧急 9
        a.add(ConfigItem.configIcon(
            { !(record.isReadOnly() || record.isBlock()) },
            { record.taskLabelColor() },
            { R.drawable.ic_feed_list_photo_special_24dp },
            { record.taskLabelStr() },
            { "优先级，共 4 种：(不)重要(不)紧急。用于分类管理" },
            { iv, tv -> listener.label(iv, tv) }
        ))
        //颜色 10
        a.add(ConfigItem.configIcon(
            { !(record.isReadOnly() || record.isBlock()) },
            { record.color },
            { R.drawable.ic_circle_small },
            { "颜色" },
            { "记录的颜色，类似于标签，用于分类管理" },
            { iv, tv -> listener.color(iv, tv) }
        ))
        //强制删除 11
        a.add(ConfigItem.configIcon(
            { !(record.isBlock()) },
            { Attr.getIconColor(context) },
            { R.drawable.ic_delete_red_24dp },
            { "强制删除" },
            { "无视拉黑状态，强制删除本记录。被封印状态仅所有者可以强制删除。只读状态无法强制删除。" },
            { _, _ ->
                popupWindow.dismiss()
                listener.forceDelete()
            }
        ))
        a.add(ConfigItem.configText { "任务" }) //12
        //可完成 13
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Attr.getIconColor(context) },
            { record.canFinish() },
            { if (it) "可完成" else "不可完成" },
            { "\'不可完成\'表示不可以打勾了" },
            {
                listener.canFinish(it)
                refreshData()
            }
        ))
        //完成 14
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) && record.canFinish() },
            { Attr.getIconColor(context) },
            { record.isFinished() },
            { if (it) "已完成" else "未完成" },
            { "完成" },
            { listener.finish(it) }
        ))
        //提醒 15
        a.add(ConfigItem.configSwitcher(
            { !(record.isReadOnly() || record.isBlock()) },
            { Attr.getIconColor(context) },
            { record.isReminderOn() },
            { if (it) "提醒中" else "无提醒" },
            { "\'提醒中\'意味着您按照时间规划进行工作，时光猫按照时间规划进行提醒" },
            { listener.remind(it) }
        ))
        //磁贴通知 16
        a.add(ConfigItem.configIcon(
            { !(record.isReadOnly() || record.isBlock()) },
            { Attr.getIconColor(context) },
            { R.drawable.ic_notification_white_24dp },
            { "磁贴" },
            { "发送为磁贴" },
            { iv, tv -> listener.tile(iv, tv) }
        ))
        //悬浮窗 17
        a.add(ConfigItem.configIcon(
            { !(record.isReadOnly() || record.isBlock()) },
            { Attr.getIconColor(context) },
            { R.drawable.ic_notification_white_24dp },
            { "悬浮窗" },
            { "作为悬浮窗" },
            { iv, tv -> listener.floatingWindow(iv, tv) }
        ))
        //暂驻通知栏 18
        a.add(ConfigItem.configIcon(
            { !(record.isReadOnly() || record.isBlock()) },
            { Attr.getIconColor(context) },
            { R.drawable.ic_notification_white_24dp },
            { "通知栏" },
            { "发送到通知栏" },
            { iv, tv -> listener.notify(iv, tv) }
        ))
        a.add(ConfigItem.configText { "内容" })
        //复制 19
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_copy_special_24dp },
            { "复制" },
            { "复制" },
            { iv, tv -> listener.copy(iv, tv) }
        ))
        //分词 20
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_nlp },
            { "分词" },
            { "分词" },
            { iv, tv -> listener.nlp(iv, tv) }
        ))
        //翻译 21
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_translate_special_24dp },
            { "翻译" },
            { "翻译" },
            { iv, tv -> listener.translate(iv, tv) }
        ))
        //搜索 22
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_search_special_24dp },
            { "搜索" },
            { "搜索" },
            { iv, tv -> listener.search(iv, tv) }
        )
        )
        //分享 23
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_share },
            { "分享" },
            { "分享" },
            { iv, tv -> listener.share(iv, tv) }
        ))
        //朗读 24
        a.add(ConfigItem.configIcon(
            { true },
            { Attr.getIconColor(context) },
            { R.drawable.ic_ring_sound },
            { "朗读" },
            { "朗读" },
            { iv, tv -> listener.speakLoud(iv, tv) }
        ))

        return a
    }


}