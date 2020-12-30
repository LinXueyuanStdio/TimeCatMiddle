package com.timecat.middle.block.event;

import com.timecat.data.room.record.RoomRecord;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
public class NotifyFloatWindowItemContentChangedEvent {
    private final RoomRecord record;

    public NotifyFloatWindowItemContentChangedEvent(RoomRecord roomRecord) {
        this.record = roomRecord;
    }

    public final RoomRecord getRecord() {
        return this.record;
    }
}
