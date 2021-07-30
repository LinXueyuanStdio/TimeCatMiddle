package com.timecat.middle.block.support;

import android.content.Context;

import com.timecat.data.room.habit.ReminderSchema;
import com.timecat.data.room.record.RoomRecord;
import com.timecat.data.room.reminder.Reminder;
import com.timecat.identity.data.base.RecordKt;
import com.timecat.identity.data.base.ReminderStatus;
import com.timecat.identity.data.base.Type;
import com.timecat.middle.block.aggreement.ReminderManager;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-22
 * @description null
 * @usage null
 */
public class ThingManager {

    public static int getThingTypeAfter(
            Context context,
            @NotNull RoomRecord mRoomRecord,
            @NotNull ReminderHabitParams rhParams
    ) {
        if (!mRoomRecord.isUnderWay()) {
            return mRoomRecord.getSubType();
        }
        long time = rhParams.getReminderTime();
        Reminder mReminder = ReminderSchema.getReminder(mRoomRecord);
        if (mRoomRecord.isReminderOn()) {
            if (mReminder != null && mReminder.getNotifyTime() == time) {
                //和本来的提醒一致
                return mRoomRecord.getSubType();
            } else {
                if (rhParams.getHabitDetail() != null) {
                    return RecordKt.HABIT;
                } else {
                    long notifyTime = rhParams.getReminderTime();
                    long createTime = System.currentTimeMillis();
                    return ReminderManager.getType(notifyTime, createTime);
                }
            }
        } else {
            @Type int typeBefore = mRoomRecord.getSubType();
            if (typeBefore == RecordKt.REMINDER || typeBefore == RecordKt.GOAL) {
                if (mReminder == null) {
                    return typeBefore;
                }
                ReminderStatus reminderState = mReminder.getState();
                if ((reminderState.isReminded() || reminderState.isExpired())
                        && mReminder.getNotifyTime() == time) {
                    return typeBefore;
                } else {
                    return RecordKt.NOTE;
                }
            } else if (typeBefore == RecordKt.HABIT) {
                return RecordKt.NOTE;
            } else {
                return typeBefore;
            }
        }
    }

    public static boolean isReminderType(@Type int type) {
        return type == RecordKt.REMINDER || type == RecordKt.GOAL;
    }
}
