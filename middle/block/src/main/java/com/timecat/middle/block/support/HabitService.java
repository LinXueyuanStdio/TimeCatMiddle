package com.timecat.middle.block.support;

import android.content.Context;

import com.timecat.data.room.record.RoomRecord;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/2
 * @description 习惯服务
 * @usage null
 */
public interface HabitService {
    /**
     * 暂停习惯打卡
     * @param context 上下文
     * @param record 有习惯的record
     */
    void pauseHabit(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
    /**
     * 恢复习惯打卡
     * @param context 上下文
     * @param record 有习惯的record
     */
    void resumeHabit(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
    /**
     * 打卡一次习惯
     * @param context 上下文
     * @param record 有习惯的record
     */
    void finishHabitOneTime(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
    /**
     * 已养成该习惯 / 已达到目标 / 已完成提醒
     * @param context 上下文
     * @param record 有习惯/目标/提醒的record
     */
    void finish(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
    /**
     * 删除
     * @param context 上下文
     * @param record 有习惯/目标/提醒的record
     */
    void delete(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
    /**
     * 从回收站恢复
     * @param context 上下文
     * @param record 有习惯/目标/提醒的record
     */
    void restore(
            @NotNull Context context,
            @NotNull RoomRecord record,
            @NotNull RoomIOListener listener
    );
}
