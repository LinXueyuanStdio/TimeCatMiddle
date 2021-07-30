package com.timecat.middle.block.support;

import android.content.Context;

import com.timecat.data.room.record.RoomRecord;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/2
 * @description null
 * @usage null
 */
public interface ChangeReminderService {
    void onChangeReminder(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull ReminderHabitParams rhParams,
            @NotNull RoomIOListener listener);
}
