package com.timecat.middle.block.aggreement

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.timecat.component.router.app.NAV
import com.timecat.data.room.habit.DoingRecord
import com.timecat.data.room.habit.habitSchema
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.HABIT
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.R
import com.timecat.middle.block.temp.Def
import com.timecat.middle.block.util.DateTimeUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/17
 * @description null
 * @usage null
 */
object RecordManager {

    @JvmStatic
    fun showDetail(
        id: String,
        supportFragmentManager: FragmentManager
    ) {
        val obj = NAV.fragment(RouterHub.EDITOR_RecordDetailPanelFragment, "recordId", id)
        if (obj is DialogFragment) {
            val f = obj
            f.setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)
            f.show(supportFragmentManager, "detail$id")
        }
    }

    @JvmStatic
    fun startDoing(
        context: Context,
        model: RoomRecord
    ) {
        NAV.raw(context, RouterHub.ORG_StartDoingActivity)
            .withLong(Def.Communication.KEY_ID, model.id)
            .withInt(
                Def.Communication.KEY_POSITION,
                if (model.order < -1) -1 else model.order
            )
            .withInt(Def.Communication.KEY_COLOR, model.color)
            .withInt("start_type", DoingRecord.START_TYPE_USER)
            .withLong(
                Def.Communication.KEY_TIME,
                calculateHrTimeForHabit(context, model)
            )
            .navigation()
    }


    @JvmStatic
    fun calculateHrTimeForHabit(
        context: Context,
        mRoomRecord: RoomRecord
    ): Long {
        var hrTime: Long = -1
        if (mRoomRecord.subType == HABIT) {
            val habit = mRoomRecord.habitSchema
            if (habit != null) {
                val remindedTimes = habit.remindedTimes
                val recordedTimes = habit.record.length
                hrTime = if (remindedTimes == recordedTimes) {
                    // user want to do this thing for upcoming habit reminder.
                    habit.minHabitReminderTime
                } else if (remindedTimes > recordedTimes) {
                    // habit reminder is notified but user hasn't finished it yet.
                    val maxTime = habit.finalHabitReminder.notifyTime
                    DateTimeUtil.getHabitReminderTime(
                        habit.type,
                        maxTime,
                        -1
                    )
                } else { // remindedTimes < recordedTimes
                    // user finished habit some times in advance, now he decided to enlarge
                    // the advantage
                    habit.minHabitReminderTime
                }
            }
        }
        return hrTime
    }
}