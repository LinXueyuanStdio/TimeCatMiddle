package com.timecat.middle.block.aggreement

import android.content.Context
import com.timecat.data.room.habit.Habit
import com.timecat.middle.block.R
import com.timecat.middle.block.util.DateTimeUtil
import com.timecat.middle.block.util.LocaleUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/26
 * @description null
 * @usage null
 */
object HabitManager {

    fun getNextReminderDescription(context: Context, habit: Habit): String {
        val nextTime = habit.minHabitReminderTime
        return getNextReminderDescription(context, nextTime)
    }

    fun getNextReminderDescription(context: Context, nextTime: Long): String {
        return DateTimeUtil.getDateTimeStrAt(nextTime, context, true)
    }

    fun getSummary(context: Context, habit: Habit): String {
        return getSummary(context, habit.type, habit.habitReminders.size)
    }

    fun getSummary(context: Context, habitType: Int, timesEveryT: Int): String {
        val timeTypeEveryT = DateTimeUtil.getTimeTypeStr(habitType, context)
        return if (LocaleUtil.isChinese(context)) {
            val every = context.getString(R.string.every)
            val timesStr = context.getString(R.string.times)
            "$every$timeTypeEveryT $timesEveryT $timesStr"
        } else {
            val timesStr = LocaleUtil.getTimesStr(context, timesEveryT)
            "$timesStr a $timeTypeEveryT"
        }
    }

    /**
     * Precondition: Habit should be in underway.
     */
    fun getStateDescription(context: Context, habit: Habit): String {
        return if (habit.isPaused) {
            context.getString(R.string.habit_paused)
        } else ""
    }

    fun getCelebrationText(context: Context, habit: Habit): String {
        val sb = StringBuilder()
        val part1 = context.getString(R.string.celebration_habit_part_1)
        val piT = habit.persistInT
        sb.append(part1).append(" ").append(if (piT < 1) "<1" else piT.toString()).append(" ")
            .append(DateTimeUtil.getTimeTypeStr(habit.type, context))
        if (piT > 1 && !LocaleUtil.isChinese(context)) {
            sb.append("s")
        }
        sb.append(context.getString(R.string.celebration_habit_part_2))
            .append(" ")
            .append(LocaleUtil.getTimesStr(context, habit.finishedTimes))
            .append(if (LocaleUtil.isChinese(context)) "" else " ")
            .append(context.getString(R.string.celebration_habit_part_3))
            .append("\n")
            .append(context.getString(R.string.celebration_habit_part_4))
        return sb.toString()
    }
}