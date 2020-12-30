package com.timecat.middle.block.aggreement;

import android.content.Context;

import com.timecat.data.room.reminder.Reminder;
import com.timecat.identity.data.base.RecordKt;
import com.timecat.identity.data.base.ReminderStatus;
import com.timecat.identity.data.base.TaskStatus;
import com.timecat.middle.block.R;
import com.timecat.middle.block.util.DateTimeUtil;
import com.timecat.middle.block.util.LocaleUtil;

import java.util.Calendar;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-17
 * @description null
 * @usage null
 */
public class ReminderManager {

    public static int getFinishType(Reminder reminder, long thingFinishTime, boolean isGoal) {
        if (thingFinishTime == -1L || thingFinishTime == 0L) {
            return -1; // unfinished yet
        }
        if (!isGoal) {
            if (thingFinishTime < reminder.getNotifyTime()) {
                return 0;
            } else if (thingFinishTime == reminder.getNotifyTime()) {
                return 1;
            } else {
                return 2;
            }
        } else {
            int finishDays = DateTimeUtil.calculateTimeGap(reminder.getUpdateTime(), thingFinishTime, Calendar.DATE);
            int goalDays = DateTimeUtil.calculateTimeGap(reminder.getUpdateTime(), reminder.getNotifyTime(), Calendar.DATE);
            if (finishDays < goalDays) {
                return 0;
            } else if (finishDays == goalDays) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    public static String getCelebrationText(Reminder reminder, Context context) {
        String part1 = context.getString(R.string.celebration_goal_part_1);
        String day = context.getString(R.string.days);
        String part2 = context.getString(R.string.celebration_goal_part_2);
        String part3 = context.getString(R.string.celebration_goal_part_3);
        int gap = DateTimeUtil.calculateTimeGap(reminder.getUpdateTime(), System.currentTimeMillis(), Calendar.DATE);
        String gapStr;
        if (gap == 0) {
            gapStr = "<1";
        } else {
            gapStr = String.valueOf(gap);
        }

        boolean isChinese = LocaleUtil.isChinese(context);
        if (gap > 1 && !isChinese) {
            day += "s";
        }
        return part1 + " " + gapStr + " " + day + (isChinese ? "" : " ") + part2 + "\n" + part3;
    }

    public static int getType(long notifyTime, long createTime) {
        int days = DateTimeUtil.calculateTimeGap(createTime, notifyTime, Calendar.DATE);
        if (Math.abs(days) > Reminder.GOAL_DAYS) {
            return RecordKt.GOAL;
        } else {
            return RecordKt.REMINDER;
        }
    }

    public static String getStateDescription(TaskStatus thingState, ReminderStatus reminderState, Context context) {
        String result;
        if (reminderState.isReminded()) {
            result = context.getString(R.string.reminder_reminded);
        } else if (reminderState.isExpired()) {
            result = context.getString(R.string.reminder_expired);
        } else {
            if (thingState.isUnderWay()) {
                result = "";
            } else if (thingState.isFinished()) {
                result = context.getString(R.string.reminder_needless);
            } else {
                result = context.getString(R.string.reminder_unavailable);
            }
        }
        return result;
    }
}
