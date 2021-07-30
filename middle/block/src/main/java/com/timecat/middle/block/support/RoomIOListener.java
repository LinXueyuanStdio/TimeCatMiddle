package com.timecat.middle.block.support;

import com.timecat.data.room.record.RoomRecord;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/25
 * @description null
 * @usage null
 */
public interface RoomIOListener {
    void save(@NotNull RoomRecord record);
}
